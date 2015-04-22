package com.tdt4240.a19.mazegame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.android.gms.plus.Plus;

import com.google.example.games.basegameutils.BaseGameUtils;
import com.google.example.games.basegameutils.GBaseGameActivity;
import com.tdt4240.a19.mazegame.assetsHandler.FontHandler;
import com.tdt4240.a19.mazegame.assetsHandler.ResourcesManager;
import com.tdt4240.a19.mazegame.assetsHandler.SpriteHandler;
import com.tdt4240.a19.mazegame.scenes.CountdownScene;
import com.tdt4240.a19.mazegame.scenes.GameScene;
import com.tdt4240.a19.mazegame.scenes.SceneManager;
import com.tdt4240.a19.mazegame.scenes.GameRoomScene;


import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.ui.IGameInterface;
import org.andengine.ui.activity.BaseGameActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GameActivity extends GBaseGameActivity implements ConnectionCallbacks, OnConnectionFailedListener, RoomUpdateListener, RealTimeMessageReceivedListener, RoomStatusUpdateListener, OnInvitationReceivedListener {

    public static final int CAMERA_WIDTH = 480;
    public static final int CAMERA_HEIGHT = 800;

    private Camera camera;

    private ResourcesManager resourcesManager;

    /*
    API integration Basic
    */

    private static final String TAG = "MultiMazed";

    // Request codes for the UIs that we show with startActivityForResult:
    final static int RC_SELECT_PLAYERS = 10000;
    final static int RC_INVITATION_INBOX = 10001;
    final static int RC_WAITING_ROOM = 10002;

    // Request code used to invoke sign in user interactions.
    private static final int RC_SIGN_IN = 9001;

    // Client used to interact with Google APIs.
    private GoogleApiClient mGoogleApiClient;

    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;

    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = false;

    // Set to true to automatically start the sign in flow when the Activity starts.
    // Set to false to require the user to click the button in order to sign in.
    private boolean mAutoStartSignInFlow = false;

    /*
    API integration MP
    */

    // Room ID where the currently active game is taking place; null if we're
    // not playing.
    String mRoomId = null;

    // Are we playing in multiplayer mode?
    boolean mMultiplayer = false;

    Invitation mInvitation;
    // The participants in the currently active game
    ArrayList<Participant> mParticipants = null;

    // My participant ID in the currently active game
    String mMyId = null;

    int mEndtime = 0;

    //The DisplayName of the person who has invited me to a game
    String mInviterName = " ";
    // If non-null, this is the id of the invitation we received via the
    // invitation listener
    String mIncomingInvitationId = null;

    // Message buffer for sending messages
    byte[] mMsgBuf = new byte[5];
    byte[] mMsgBufPos = new byte[3];
    byte[] mSeedBuf = new byte [1];

    private float xpos;
    private float ypos;


    boolean isFinished = false;



    public int mMytime = 0;


    private final byte[] seeds = {123,24,3,4};
    private long mSeed= 123;

    @Override
    public EngineOptions onCreateEngineOptions() {
        BoundCamera camera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        //camera.setBounds(-400, -500, 2000, 800);
        //camera.setBoundsEnabled(true);
        this.camera = camera;
        Log.w(TAG, "onCreateEngineOptions");
        EngineOptions options = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
        options.getAudioOptions().setNeedsMusic(true);
        return options;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
        Log.w(TAG, "onCreateResources : " + pOnCreateResourcesCallback);
        /*BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        Log.w(TAG, "onCreateResources");
        spriteHandler = new SpriteHandler();
        spriteHandler.setupAtlases();
        spriteHandler.setupSprites();
        spriteHandler.buildAtlases();
        spriteHandler.loadAtlases();
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("fonts/");
        fontHandler = new FontHandler();
        fontHandler.createFonts();
        fontHandler.loadFonts();*/

        ResourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
        resourcesManager = ResourcesManager.getInstance();
        pOnCreateResourcesCallback.onCreateResourcesFinished();



    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);


        // Create the Google Api Client with access to Plus and Games
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
        Log.w(TAG, "onCreateScene mGoogleApiClient = " + mGoogleApiClient.getClass());

    }

    // TODO: Se om det er behov for å kalle connect. Står på auto connect atm.
    @Override
    protected void onStart() {
        super.onStart();
//        if (!mResolvingError) {  // more about this later
//            mGoogleApiClient.connect();
//        }
    }
    public void setInvitation(Invitation invitation){
        this.mInvitation = invitation;
    }
    public Invitation getInvitation(){
        return this.mInvitation;
    }


    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                mGoogleApiClient.connect();
                SceneManager.getInstance().createMenuScene();

                // load menu resources, create menu scene
                // set menu scene using scene manager
                // disposeSplashScene();
                // READ NEXT ARTICLE FOR THIS PART.
            }
        }));
        pOnPopulateSceneCallback.onPopulateSceneFinished();
        Log.w(TAG, "onPopulateScene");

    }

    @Override
    protected void onPause() {
            super.onPause();
            ResourcesManager.getInstance().music.pause();
            ResourcesManager.getInstance().music.setLooping(false);

    }

    @Override
    protected synchronized void onResume() {
        super.onResume();
        System.gc();
        if(this.isGameLoaded()) {
            ResourcesManager.getInstance().music.play();
            ResourcesManager.getInstance().music.setLooping(true);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(TAG, "onConnected() called. Sign in successful!");

        Log.d(TAG, "connectionHint: " + connectionHint);

        // register listener so we are notified if we receive an invitation to play
        // while we are in the game
        Games.Invitations.registerInvitationListener(mGoogleApiClient, this);
        if (connectionHint != null) {
            Log.d(TAG, "onConnected: connection hint provided. Checking for invite.");
            Invitation inv = connectionHint
                    .getParcelable(Multiplayer.EXTRA_INVITATION);
            if (inv != null && inv.getInvitationId() != null) {
                // retrieve and cache the invitation ID
                Log.d(TAG, "onConnected: connection hint has a room invite!");
                acceptInviteToRoom(inv);
                return;
            }
        }

        // TODO: switchToMainScreen
        /*switchToMainScreen();*/

    }

    public String getMyId(){
        return this.mMyId;
    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended() called. Trying to reconnect.");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed() called, result: " + connectionResult);

        if (mResolvingConnectionFailure) {
            Log.d(TAG, "onConnectionFailed() ignoring connection failure; already resolving.");
            return;
        }

        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            // TODO: string: signin_other_error
            mResolvingConnectionFailure = BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient,
                    connectionResult, RC_SIGN_IN, getString(R.string.signin_other_error));
        }
        // TODO: SwitchToScreen her String screen_sign_in
        /*switchToScreen(R.id.screen_sign_in);*/
    }

    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
        }
        return false;
    }

    public void invitePlayer() {
        Log.d(TAG, "Inviteplayer()called.");
        Intent intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(mGoogleApiClient, 1, 10);
        startActivityForResult(intent, RC_SELECT_PLAYERS);
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode,
                                 Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);

        switch (requestCode) {
            case RC_SELECT_PLAYERS:
                // we got the result from the "select players" UI -- ready to create the room
                handleSelectPlayersResult(responseCode, intent);
                break;
            case RC_INVITATION_INBOX:
                // we got the result from the "select invitation" UI (invitation inbox). We're
                // ready to accept the selected invitation:
                handleInvitationInboxResult(responseCode, intent);
                break;
            case RC_WAITING_ROOM:
                // we got the result from the "waiting room" UI.
                if (responseCode == Activity.RESULT_OK) {
                    // ready to start playing
                    Log.d(TAG, "Starting game (waiting room returned OK).");
                    startGame(true);
                } else if (responseCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                    // player indicated that they want to leave the room
                    leaveRoom();
                } else if (responseCode == Activity.RESULT_CANCELED) {
                    // Dialog was cancelled (user pressed back key, for instance). In our game,
                    // this means leaving the room too. In more elaborate games, this could mean
                    // something else (like minimizing the waiting room UI).
                    leaveRoom();

                }
                break;
            case RC_SIGN_IN:
                Log.d(TAG, "onActivityResult with requestCode == RC_SIGN_IN, responseCode="
                        + responseCode + ", intent=" + intent);
                mSignInClicked = false;
                mResolvingConnectionFailure = false;
                if (responseCode == RESULT_OK) {
                    mGoogleApiClient.connect();
                } else {
                    BaseGameUtils.showActivityResultError(this, requestCode, responseCode, R.string.signin_other_error);
                }
                break;
        }
        super.onActivityResult(requestCode, responseCode, intent);
    }

    // Handle the result of the "Select players UI" we launched when the user clicked the
    // "Invite friends" button. We react by creating a room with those players.
    private void handleSelectPlayersResult(int response, Intent data) {
        if (response != Activity.RESULT_OK) {
            Log.w(TAG, "*** select players UI cancelled, " + response);
            // TODO: Switch to main screen
            //switchToMainScreen();
            return;
        }

        Log.d(TAG, "Select players UI succeeded.");

        // get the invitee list
        final ArrayList<String> invitees = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);
        Log.d(TAG, "Invitee count: " + invitees.size());

        // get the automatch criteria
        Bundle autoMatchCriteria = null;
        int minAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
        int maxAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);
        if (minAutoMatchPlayers > 0 || maxAutoMatchPlayers > 0) {
            autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
                    minAutoMatchPlayers, maxAutoMatchPlayers, 0);
            Log.d(TAG, "Automatch criteria: " + autoMatchCriteria);
        }

        // create the room
        Log.d(TAG, "Creating room...");
        RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
        rtmConfigBuilder.addPlayersToInvite(invitees);
        rtmConfigBuilder.setMessageReceivedListener(this);
        rtmConfigBuilder.setRoomStatusUpdateListener(this);
        if (autoMatchCriteria != null) {
            rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
        } else if(autoMatchCriteria==null) {

        }
        // TODO: Sett loadingscreen
        //switchToScreen(R.id.screen_wait);
        resetGameVars();
        Games.RealTimeMultiplayer.create(mGoogleApiClient, rtmConfigBuilder.build());

        Log.d(TAG, "Room created, waiting for it to be ready...");
    }
    private int randomSeed(){
        int max =3,min=0;
        Random random = new Random();
        int randomSeed = random.nextInt((max-min)+1)+min;
        return randomSeed;
    }
    public Long getSeed(){
        return this.mSeed;
    }
    private void setSeed(int seed){
        this.mSeed = seeds[seed];
    }

    // Handle the result of the invitation inbox UI, where the player can pick an invitation
    // to accept. We react by accepting the selected invitation, if any.
    private void handleInvitationInboxResult(int response, Intent data) {
        if (response != Activity.RESULT_OK) {
            Log.w(TAG, "*** invitation inbox UI cancelled, " + response);
            // TODO: Gå til mainscreen
            //switchToMainScreen();
            return;
        }

        Log.d(TAG, "Invitation inbox UI succeeded.");
        Invitation inv = data.getExtras().getParcelable(Multiplayer.EXTRA_INVITATION);

        // accept invitation
        acceptInviteToRoom(inv);
    }

    // Accept the given invitation.
    public void acceptInviteToRoom(Invitation inv) {
        // accept the invitation
        Log.d(TAG, "Accepting invitation: " + inv.getInvitationId());
        RoomConfig.Builder roomConfigBuilder = RoomConfig.builder(this);
        roomConfigBuilder.setInvitationIdToAccept(inv.getInvitationId())
                .setMessageReceivedListener(this)
                .setRoomStatusUpdateListener(this);
        // TODO: Loading screen
        //switchToScreen(R.id.screen_wait);
        resetGameVars();
        Games.RealTimeMultiplayer.join(mGoogleApiClient, roomConfigBuilder.build());

        setInviterName(inv);
        SceneManager.getInstance().createGameRoomScene(mEngine);
    }


    // Leave the room.
    void leaveRoom() {
        Log.d(TAG, "Leaving room.");
        mSecondsLeft = 0;
        stopKeepingScreenOn();
        if (mRoomId != null) {
            Games.RealTimeMultiplayer.leave(mGoogleApiClient, this, mRoomId);
            mRoomId = null;
            // TODO: Laoding wait screen
            // switchToScreen(R.id.screen_wait);
        } else {
            // TODO: Sett main screen
            //switchToMainScreen();
        }
    }

    // Show the waiting room UI to track the progress of other players as they enter the
    // room and get connected.
    void showWaitingRoom(Room room) {
        // minimum number of players required for our game
        // For simplicity, we require everyone to join the game before we start it
        // (this is signaled by Integer.MAX_VALUE).
        final int MIN_PLAYERS = Integer.MAX_VALUE;
        Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(mGoogleApiClient, room, MIN_PLAYERS);

        // show waiting room UI
        startActivityForResult(i, RC_WAITING_ROOM);
    }

    // Called when we get an invitation to play a game. We react by showing that to the user.
    @Override
    public void onInvitationReceived(Invitation invitation) {
        // We got an invitation to play a game! So, store it in
        // mIncomingInvitationId
        // and show the popup on the screen.
        // TODO: Kan vise noe i UI Vis pop!
        mIncomingInvitationId = invitation.getInvitationId();
        Log.d(TAG, "onInvitationReceived()called." + mIncomingInvitationId);
/*        ((TextView) findViewById(R.id.incoming_invitation_text)).setText(
                invitation.getInviter().getDisplayName() + " " +
                        getString(R.string.is_inviting_you));*/
//        switchToScreen(mCurScreen); // This will show the invitation popup
        setInvitation(invitation);
        setInviterName(invitation);
        SceneManager.getInstance().createGameRoomScene(mEngine);


    }

    //NullObject Pattern
    public void setInviterName(Invitation invitation) {
        if (invitation.getInviter().getDisplayName() != null)
            this.mInviterName = invitation.getInviter().getDisplayName();
        else
            this.mInviterName = "TEST";

    }

    public String getInviterName() {
        if (this.mInviterName != null)
            return this.mInviterName;
        else
            return "Something failed";
    }
    public String getInvId() {
        return mIncomingInvitationId;
    }

    @Override
    public void onInvitationRemoved(String invitationId) {
        if (mIncomingInvitationId.equals(invitationId)) {
            mIncomingInvitationId = null;
            // TODO: Hide the inv
            // switchToScreen(mCurScreen); // This will hide the invitation popup
        }

    }

    // TODO: Legg til knapp for denne funksjonen
    public void startQuickGame() {
        Log.d(TAG, "StartQuickGame()called.");
        // quick-start a game with 1 randomly selected opponent
        final int MIN_OPPONENTS = 1, MAX_OPPONENTS = 10;
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_OPPONENTS,
                MAX_OPPONENTS, 0);
        RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
        rtmConfigBuilder.setMessageReceivedListener(this);
        rtmConfigBuilder.setRoomStatusUpdateListener(this);
        rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
        // TODO: Add loading screen while waiting
        resetGameVars();
        Games.RealTimeMultiplayer.create(mGoogleApiClient, rtmConfigBuilder.build());

    }
    /*
     * GAME LOGIC SECTION. Methods that implement the game's rules.
     */

    // Current state of the game:
    int mSecondsLeft = -1; // how long until the game ends (seconds)
    final static int GAME_DURATION = 90; // game duration, seconds.
    int mScore = 0; // user's current score

    // Reset game variables in preparation for a new game.
    void resetGameVars() {
        mSecondsLeft = GAME_DURATION;
        mScore = 0;
        mParticipantScore.clear();
        mFinishedParticipants.clear();
    }

    // Start the gameplay phase of the game.
    void startGame(boolean multiplayer) {
        mMultiplayer = multiplayer;
        // TODO: Sett tid update score
        //updateScoreDisplay();
        broadcastScore(false);
        // TODO: Sett game screen
        //switchToScreen(R.id.screen_game);
        SceneManager.getInstance().loadGameScene(resourcesManager.engine);
        // TODO: Slett dette
        // findViewById(R.id.button_click_me).setVisibility(View.VISIBLE);

        // run the gameTick() method every 5 second to update the game.
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                // if (mSecondsLeft <= 0)
                //   return;
                // TODO: Set if Når spillet er over.
                // Broadcast every 5 sec
                // Log.d(TAG, "Broadcast() called.");
                scoreOnePoint();
                h.postDelayed(this, 5000);
            }
        }, 5000);
    }

    // indicates the player scored one point
    void scoreOnePoint() {
        //  if (mSecondsLeft <= 0)
        //     return; // too late!
        // TODO: for testing
        ++mScore;
        xpos = SceneManager.getInstance().getGameScene().getUserLayer().getUser().getX();
        ypos = SceneManager.getInstance().getGameScene().getUserLayer().getUser().getY();
        // Log.d(TAG, "xpos: " + xpos + "ypos: " + ypos);

        // TODO: update view
        //updateScoreDisplay();
        //updatePeerScoresDisplay();

        // broadcast our new score to our peers
        if (!isDone()){
            broadcastPos(false);
        }

    }

    // updates the screen with the scores from our peers
    public String updatePeerScoresDisplay() {
        String Result = "";
        int i = 0;
        String[][] scoreMatrix = new String[8][2];

        if (mParticipants != null) {
            for (Participant p : mParticipants) {
                String pid = p.getParticipantId();
                if (pid.equals(mMyId)) {
                    scoreMatrix[i][0] = mMytime + "";
                    scoreMatrix[i][1] = p.getDisplayName();
                } else {
                    int score = mParticipantScore.containsKey(pid) ? mParticipantScore.get(pid) : 0;
                    if (score != 0) {
                        scoreMatrix[i][0] = score + "";
                        scoreMatrix[i][1] = p.getDisplayName();
                    }
                    if (p.getStatus() != Participant.STATUS_JOINED)
                        continue;
                    ++i;
                }
            }/*
        Arrays.sort(scoreMatrix, new Comparator<String[]>(){
            @Override
            public int compare(final String[] entry1,final String[] entry2){
                final String score1 = entry1[1];
                final String score2 = entry2[1];
                return score1.compareTo(score2);
            }
        });*/
            int k=0;
            for (int j = 0; j < mParticipants.size(); j++) {
                String name=scoreMatrix[j][1] ;
                Result = Result + (k + 1) + ". " + name + " : " + getEndTime(scoreMatrix[j][0]) + "\n";
                k++;

            }
            }
            else if(mParticipants==null){
                Result = "ScoreBoard not available \n in singleplayer";
        }
        return Result;
    }
    public String getEndTime(String time){
        String scoreTime;
        if(time==null){
            return "Time Unavailable";
        }
        int endTime = Integer.parseInt(time);
        if(endTime==0){
            return "Not Finished";
        }
        int min = endTime/1000;
        int sec = endTime/10;
        int ms = endTime%10;

        scoreTime =(min < 10 ? "" + min : min) + ":" + (sec < 10 ? "0" + sec : sec) + ":" + ms;
        return scoreTime;
    }
    public String endResult(){
        String Result = "";
        int i = 1;
        broadcastScore(true);
        if(mParticipants==null)
            return "HighScore list not available in singlePlayer";
        for(i=0;i< mParticipants.size();i++){
            Result = Result + i +". " + mParticipants.get(i).getDisplayName() + " Tid: " + mParticipantScore.get(mParticipants.get(i).getParticipantId())  + "\n";
        }
        return Result;
    }
    // TODO: Trenger vi denne til å holde views?
//    // This array lists all the individual screens our game has. GooglePlay
//    final static int[] SCREENS = {
//            R.id.screen_game, R.id.screen_main, R.id.screen_sign_in,
//            R.id.screen_wait
//    };
//    int mCurScreen = -1;

//    void switchToScreen(int screenId) {
//        // make the requested screen visible; hide all others.
//        for (int id : SCREENS) {
//            findViewById(id).setVisibility(screenId == id ? View.VISIBLE : View.GONE);
//        }
//        mCurScreen = screenId;
//
//        // should we show the invitation popup?
//        boolean showInvPopup;
//        if (mIncomingInvitationId == null) {
//            // no invitation, so no popup
//            showInvPopup = false;
//        } else if (mMultiplayer) {
//            // if in multiplayer, only show invitation on main screen
//            showInvPopup = (mCurScreen == R.id.screen_main);
//        } else {
//            // single-player: show on main screen and gameplay screen
//            showInvPopup = (mCurScreen == R.id.screen_main || mCurScreen == R.id.screen_game);
//        }
//        findViewById(R.id.invitation_popup).setVisibility(showInvPopup ? View.VISIBLE : View.GONE);
//    }

    // Show error message about game being cancelled and return to main screen.
    void showGameError() {
        BaseGameUtils.makeSimpleDialog(this, getString(R.string.game_problem));
        // TODO: To mainscreen
        //switchToMainScreen();
    }

    @Override
    public void onRoomCreated(int statusCode, Room room) {
        Log.d(TAG, "onRoomCreated(" + statusCode + ", " + room + ")");
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomCreated, status " + statusCode);
            showGameError();
            return;
        }
        int randomSeed=randomSeed();
        setSeed(randomSeed);
        broadcastSeed(randomSeed);

        // show the waiting room UI
        showWaitingRoom(room);
    }

    // Called when we are connected to the room. We're not ready to play yet! (maybe not everybody
    // is connected yet).
    @Override
    public void onConnectedToRoom(Room room) {
        Log.d(TAG, "onConnectedToRoom.");

        // get room ID, participants and my ID:
        mRoomId = room.getRoomId();
        mParticipants = room.getParticipants();
        mMyId = room.getParticipantId(Games.Players.getCurrentPlayerId(mGoogleApiClient));

        // print out the list of participants (for debug purposes)
        Log.d(TAG, "Room ID: " + mRoomId);
        Log.d(TAG, "My ID " + mMyId);
        Log.d(TAG, "<< CONNECTED TO ROOM>>");
    }

    // Called when we get disconnected from the room. We return to the main screen.
    @Override
    public void onDisconnectedFromRoom(Room room) {
        mRoomId = null;
        showGameError();
    }

    // Called when room is fully connected.
    @Override
    public void onRoomConnected(int statusCode, Room room) {
        Log.d(TAG, "onRoomConnected(" + statusCode + ", " + room + ")");
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
            showGameError();
            return;
        }
        updateRoom(room);
    }

    void updateRoom(Room room) {
        if (room != null) {
            mParticipants = room.getParticipants();
        }
        if (mParticipants != null) {
            // TODO: Update score main screen
            //updatePeerScoresDisplay();
        }
    }


    @Override
    public void onJoinedRoom(int statusCode, Room room) {
        Log.d(TAG, "onJoinedRoom(" + statusCode + ", " + room + ")");
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
            showGameError();
            return;
        }

        // show the waiting room UI
        showWaitingRoom(room);
    }

    // Called when we've successfully left the room (this happens a result of voluntarily leaving
    // via a call to leaveRoom(). If we get disconnected, we get onDisconnectedFromRoom()).
    @Override
    public void onLeftRoom(int statusCode, String roomId) {
        // we have left the room; return to main screen.
        Log.d(TAG, "onLeftRoom, code " + statusCode);
        // TODO: Mainscreen
        //switchToMainScreen();
    }


    // We treat most of the room update callbacks in the same way: we update our list of
    // participants and update the display. In a real game we would also have to check if that
    // change requires some action like removing the corresponding player avatar from the screen,
    // etc.
    @Override
    public void onRoomAutoMatching(Room room) {
        updateRoom(room);
    }

    @Override
    public void onRoomConnecting(Room room) {
        updateRoom(room);
    }


    @Override
    public void onPeerInvitedToRoom(Room room, List<String> arg1) {
        updateRoom(room);
    }

    public void onPeerDeclined(Room room, List<String> arg1) {
        updateRoom(room);
    }

    @Override
    public void onPeerJoined(Room room, List<String> arg1) {
        updateRoom(room);
    }

    public void onPeerLeft(Room room, List<String> peersWhoLeft) {
        updateRoom(room);
    }

    @Override
    public void onPeersConnected(Room room, List<String> peers) {
        updateRoom(room);
    }

    @Override
    public void onPeersDisconnected(Room room, List<String> peers) {
        updateRoom(room);
    }

    @Override
    public void onP2PConnected(String s) {

    }

    @Override
    public void onP2PDisconnected(String s) {

    }


     /*
     * COMMUNICATIONS SECTION. Methods that implement the game's network
     * protocol.
     */

    // Score of other participants. We update this as we receive their scores
    // from the network.
    Map<String, Integer> mParticipantScore = new HashMap<String, Integer>();

    // Participants who sent us their final score.
    Set<String> mFinishedParticipants = new HashSet<String>();


    // Called when we receive a real-time message from the network.
    // Messages in our game are made up of 2 bytes: the first one is 'F' or 'U'
    // indicating
    // whether it's a final or interim score. The second byte is the score.
    // There is also the
    // 'S' message, which indicates that the game should start.
    @Override
    public void onRealTimeMessageReceived(RealTimeMessage rtm) {
        /**
         * TODO: Add ping echo
         * Should echo on message received (counter on echoNotReceived)
         * Lag egen send message med tag E.
         */

        byte[] buf = rtm.getMessageData();
        String sender = rtm.getSenderParticipantId();

        // Log.d(TAG, "Message received: " + (char) buf[0] + "/" + (int) buf[1]);
        if(buf.length<=1) {
            setSeed(seeds[(int) buf[0]]);
            Log.d(TAG, "Message = " + buf[0] + " Seed Received");
        } else if (buf.length > 3) {
            Log.d(TAG, "Message received: " + (char) buf[0] + "/" + (int) buf[1] + "/" + (int) buf[2] + "/" + (int) buf[3] + "/" + (int) buf[4]);
        }else
        Log.d(TAG, "Message received: " + (char) buf[0] + "/" + (int) buf[1]);

        if (buf[0] == 'F' || buf[0] == 'U') {
            // score update.
            int existingScore = mParticipantScore.containsKey(sender) ?
                    mParticipantScore.get(sender) : 0;
            int thisScore;
            if(buf.length>=4){
                thisScore = ((buf[1]*1000) + (buf[2]*100) + (buf[3]*10) + buf[4]);
            }else
                thisScore = (int) buf[1];
            if (thisScore > existingScore) {
                // this check is necessary because packets may arrive out of
                // order, so we
                // should only ever consider the highest score we received, as
                // we know in our
                // game there is no way to lose points. If there was a way to
                // lose points,
                // we'd have to add a "serial number" to the packet.
                mParticipantScore.put(sender, thisScore);
            }

            // update the scores on the screen
            // TODO: Update score view
            updatePeerScoresDisplay();

            // if it's a final score, mark this participant as having finished
            // the game
            if ((char) buf[0] == 'F') {
                mFinishedParticipants.add(rtm.getSenderParticipantId());
            }
        } if (buf[0] == 'P'){
            int x = (int) buf[1];
            int y = (int) buf[2];
            Log.w(TAG, "Position received: " + (char) buf[0] + " x: " + x + " y: " + y);
            SceneManager.getInstance().getGameScene().updateGhosts(x, y, sender);
        }
    }


    public void endTime(int finalTime){
        mScore = finalTime;
        broadcastScore(true);
        broadcastPos(true);
        isFinished = true;
    }

    void broadcastSeed(int seed){
        if(!mMultiplayer) {
            Log.d(TAG, "MULTIPLAYER NOT SET");
            return;
        }
        Log.d(TAG, "Seed = " + seed);
        mSeedBuf[0] = (byte) seed;
        for (Participant p : mParticipants) {
            if (p.getParticipantId().equals(mMyId))
                continue;
            if (p.getStatus() != Participant.STATUS_JOINED)
                continue;
            else {
                // it's an interim score notification, so we can use unreliable
                Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient,null, mSeedBuf, mRoomId,
                        p.getParticipantId());
            }
        }
    }
    // Broadcast my score to everybody else.
    void broadcastScore(boolean finalScore) {
        if (!mMultiplayer)
            return; // playing single-player mode

        // First byte in message indicates whether it's a final score or not
        mMsgBuf[0] = (byte) (finalScore ? 'F' : 'U');


        long ms = (long)mScore;
        long sec = ms/1000;
        long min = sec /60;
        ms=ms%1000;
        sec = sec%60;
        int tempScoreX = (int)min;
        int tempScoreXX = (int)sec/10;
        int tempScoreXXX = (int)sec%10;
        int tempScoreXXXX = (int) ms/100;

        Log.d(TAG, "mScore: " + mScore);
        Log.d(TAG, "ms: " + ms + "sec: " + sec + "min: " + min );
        // Second byte is the score. 0 - 1800
        mMsgBuf[1] = (byte) tempScoreX;
        mMsgBuf[2] = (byte) tempScoreXX;
        mMsgBuf[3] = (byte) tempScoreXXX;
        mMsgBuf[4] = (byte) tempScoreXXXX;

        mMytime = (tempScoreX*1000) + (tempScoreXX*100) + (tempScoreXXX*10) + tempScoreXXXX;
        Log.d(TAG, "Mytime: " + mMytime);

        // Send to every other participant.
        for (Participant p : mParticipants) {
            if (p.getParticipantId().equals(mMyId))
                continue;
            if (p.getStatus() != Participant.STATUS_JOINED)
                continue;
            if (finalScore) {
                    // final score notification must be sent via reliable message
                    Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, mMsgBuf,
                            mRoomId, p.getParticipantId());
            } else {
                // it's an interim score notification, so we can use unreliable
                Games.RealTimeMultiplayer.sendUnreliableMessage(mGoogleApiClient, mMsgBuf, mRoomId,
                        p.getParticipantId());
            }
        }
    }

    // Broadcast my score to everybody else.
    void broadcastPos(boolean finalBroadcast) {
        if (!mMultiplayer)
            return; // playing single-player mode

        // First byte in message indicates whether it's a final score or not
        mMsgBufPos[0] = (byte) 'P';

        mMsgBufPos[1] = (byte) ((int)(xpos / 20));

        // Second byte is the score.
        mMsgBufPos[2] = (byte) ((int)(ypos / 30));

        // Send to every other participant.
        for (Participant p : mParticipants) {
            if (p.getParticipantId().equals(mMyId))
                continue;
            if (p.getStatus() != Participant.STATUS_JOINED)
                continue;
            if (isDone()){
                Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, mMsgBuf,
                        mRoomId, p.getParticipantId());
            } else {
                Games.RealTimeMultiplayer.sendUnreliableMessage(mGoogleApiClient, mMsgBufPos, mRoomId, p.getParticipantId());
            }

            /**
            if (finalScore) {
                // final score notification must be sent via reliable message

            } else {
                // it's an interim score notification, so we can use unreliable
                Games.RealTimeMultiplayer.sendUnreliableMessage(mGoogleApiClient, mMsgBufPos, mRoomId,
                        p.getParticipantId());
            }
             */
        }
    }

    // TODO: Mangler UI section


        /*
     * MISC SECTION. Miscellaneous methods.
     */

    // TODO: Inv popup?
    // Sets the flag to keep this screen on. It's recommended to do that during
    // the
    // handshake when setting up a game, because if the screen turns off, the
    // game will be
    // cancelled.
    void keepScreenOn() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    // Clears the flag that keeps the screen on.
    void stopKeepingScreenOn() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public boolean isMultiplayer(){ return mMultiplayer; }

    public ArrayList<Participant> getParticipants(){ return mParticipants; }

    public boolean isDone(){ return isFinished; }

}