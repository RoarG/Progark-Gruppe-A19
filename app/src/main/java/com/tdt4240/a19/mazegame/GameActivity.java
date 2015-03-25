package com.tdt4240.a19.mazegame;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;

import com.google.example.games.basegameutils.BaseGameUtils;
import com.google.example.games.basegameutils.GBaseGameActivity;
import com.tdt4240.a19.mazegame.assetsHandler.FontHandler;
import com.tdt4240.a19.mazegame.assetsHandler.SpriteHandler;
import com.tdt4240.a19.mazegame.scenes.GameScene;
import com.tdt4240.a19.mazegame.scenes.WelcomeScene;
import com.tdt4240.a19.mazegame.scenes.GameRoomScene;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.ui.activity.BaseGameActivity;

public class GameActivity extends GBaseGameActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int CAMERA_WIDTH = 480;
    public static final int CAMERA_HEIGHT = 800;

    private Camera camera;

    private SpriteHandler spriteHandler;
    private FontHandler fontHandler;

    /*
    API integration
    */

    private static final String TAG = "MultiMazed";

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

    @Override
    public EngineOptions onCreateEngineOptions() {
        BoundCamera camera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        //camera.setBounds(-400, -500, 2000, 800);
        //camera.setBoundsEnabled(true);
        GameState.getInstance().setGameActivity(this);
        this.camera = camera;
        Log.w(TAG, "onCreateEngineOptions");
        return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        Log.w(TAG, "onCreateResources");
        spriteHandler = new SpriteHandler();
        spriteHandler.setupAtlases();
        spriteHandler.setupSprites();
        spriteHandler.buildAtlases();
        spriteHandler.loadAtlases();

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("fonts/");

        fontHandler = new FontHandler();
        fontHandler.createFonts();
        fontHandler.loadFonts();

        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        pOnCreateSceneCallback.onCreateSceneFinished(GameState.getInstance().getGameScene());

        // Create the Google Api Client with access to Plus and Games
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
        Log.w(TAG, "onCreateScene mGoogleApiClient = " + mGoogleApiClient.getClass());
        /*mGoogleApiClient.connect();*/
    }

/*
    @Override
    protected void onStart() {
        super.onStart();
     */
/*   mGoogleApiClient.connect();*//*

    }
*/

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        if (pScene instanceof WelcomeScene)
            ((WelcomeScene) pScene).init();
        else if (pScene instanceof GameScene)
            ((GameScene) pScene).init();
        else if (pScene instanceof GameRoomScene)
            ((GameRoomScene) pScene).initScene();
        pOnPopulateSceneCallback.onPopulateSceneFinished();
        Log.w(TAG, "onPopulateScene");
        //Connect google API

        //mGoogleApiClient.connect();

    }

    public SpriteHandler getSpriteHandler() {
        return spriteHandler;
    }

    public FontHandler getFontHandler() {
        return fontHandler;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected() called. Sign in successful!");

        Log.d(TAG, "Sign-in succeeded.");

       /* // register listener so we are notified if we receive an invitation to play
        // while we are in the game
        Games.Invitations.registerInvitationListener(mGoogleApiClient, this);

        if (connectionHint != null) {
            Log.d(TAG, "onConnected: connection hint provided. Checking for invite.");
            Invitation inv = connectionHint
                    .getParcelable(Multiplayer.EXTRA_INVITATION);
            if (inv != null && inv.getInvitationId() != null) {
                // retrieve and cache the invitation ID
                Log.d(TAG,"onConnected: connection hint has a room invite!");
                acceptInviteToRoom(inv.getInvitationId());
                return;
            }
        }*/

        // TODO: switchToMainScreen
        /*switchToMainScreen();*/

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
}
