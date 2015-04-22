package com.tdt4240.a19.mazegame.scenes;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.google.android.gms.games.multiplayer.Participant;
import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.assetsHandler.ResourcesManager;
import com.tdt4240.a19.mazegame.maze.Ghost;
import com.tdt4240.a19.mazegame.maze.MazeLayer;
import com.tdt4240.a19.mazegame.user.User;
import com.tdt4240.a19.mazegame.user.UserLayer;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSCounter;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;
import org.andengine.util.system.SystemUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.Vector;

/**
 * Created by Runar on 06.03.2015.
 */
public class GameScene extends BaseScene implements ContactListener {

    private PhysicsWorld physicsWorld;

    private UserLayer userLayer;
    private MazeLayer mazeLayer;

    private Vector2 pressed = new Vector2();
    private long pressedTime;

    private long endTime = 0;
    private long startTime;
    int exitGameValue = 0;
    boolean ghostHidden = false;

    private ArrayList<Ghost> ghosts;

    GameActivity game;

    /**
     * mazeSize, int size 1, 2 or 3. (1: 10x15, 2: 20x30, 3: 30x45)
     */
    private int mazeSize;

    @Override
    public void createScene() {
        this.game = ResourcesManager.getInstance().gameActivity;
        mazeLayer = new MazeLayer();
        userLayer = new UserLayer();
        physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, 0), false);
        registerUpdateHandler(physicsWorld);
        physicsWorld.setContactListener(this);

        setMazeSize("small");

        mazeLayer.init(game.CAMERA_WIDTH/2, game.CAMERA_HEIGHT/2, 20, 30, physicsWorld);  // pCenterX, pCenterY, mazeX, mazeY
        userLayer.init(mazeLayer, physicsWorld);

        setBackground(new Background(new Color(0.09804f, 0.6274f, 0.8784f)));


        if (game.isMultiplayer()){
            Log.w("MultiMazed", "MULTIPLAYER");
            setupGhosts();
            setupGhostTimer();
        } else {
            Log.w("MultiMazed", "SINGLEPLAYER");
            // testPosition();
        }

        attachChild(mazeLayer);
        attachChild(userLayer);

        //setupFPSCounter(game);

        startTime = System.currentTimeMillis();
        endTime = 0;

        setupTimer();


        /*
        registerUpdateHandler(new TimerHandler(10.0f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                testUpdateGhosts(20, 0, "ac");
            }
        }));
         */
    }

    @Override
    public void onBackKeyPressed() {
        if(exitGameValue==1)
             SceneManager.getInstance().loadMenuScene();
        else
            exitGameValue++;
    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_GAMESCENE;
    }

    @Override
    public void disposeScene() {

    }

    private void setupFPSCounter(GameActivity game) {
        final FPSCounter fpsCounter = new FPSCounter();
        registerUpdateHandler(fpsCounter);

        final Text fpsText = new Text(0, 0, ResourcesManager.getInstance().fontHandler.getBasicFont(), "FPS:", "FPS: XXXXX".length(), game.getVertexBufferObjectManager());

        attachChild(fpsText);

        registerUpdateHandler(new TimerHandler(1 / 20.0f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                fpsText.setText("FPS: " + String.format("%.2f", fpsCounter.getFPS()));
            }
        }));
    }
    public String getEndTime(){
        String endTimeString="";
        long ms = endTime - startTime;
        long sec = ms / 1000;
        long min = sec / 60;
        ms = ms % 1000;
        sec = sec % 60;
        min = min % 60;
        endTimeString =(min < 10 ? "0" + min : min) + ":" + (sec < 10 ? "0" + sec : sec) + ":" + ms;
        return endTimeString;
    }

    private void setupTimer() {
        final Text timerText = new Text(0, 0, ResourcesManager.getInstance().fontHandler.getBasicFont(), "00:00.000", "000:000.0000".length(), ResourcesManager.getInstance().vertexBufferObjectManager);
        timerText.setPosition(200 - timerText.getWidth()/2, 28);
        attachChild(timerText);
        registerUpdateHandler(new TimerHandler(1 / 20.0f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                long ms = System.currentTimeMillis() - startTime;
                long sec = ms / 1000;
                long min = sec / 60;
                ms = ms % 1000;
                sec = sec % 60;
                min = min % 60;
                timerText.setText((min < 10 ? "0" + min : min) + ":" + (sec < 10 ? "0" + sec : sec) + "." + ms);
            }
        }));
    }

    private void setupGhostTimer(){
        registerUpdateHandler(new TimerHandler(5.0f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                showGhosts();
            }
        }));

        registerUpdateHandler(new TimerHandler(4.0f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                hideGhosts();
            }
        }));
    }

    private void testPosition(){
        registerUpdateHandler(new TimerHandler(5.0f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                Log.w("POS", "X:" + (int) (userLayer.getUser().getX()/20) + ". Y: " + (int) (userLayer.getUser().getY()/30) + ". Exact x:" + userLayer.getUser().getX() + ". Exact y: " + userLayer.getUser().getY());
            }
        }));
    }




    @Override
    public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
        User user = (User)userLayer.getUser();

        switch (pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                pressed = new Vector2(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
                pressedTime = System.currentTimeMillis();
                break;
            case TouchEvent.ACTION_UP:
                long deltaMS = System.currentTimeMillis() - pressedTime;
                float deltaX = (pSceneTouchEvent.getX() - pressed.x) / deltaMS;
                float deltaY = (pSceneTouchEvent.getY() - pressed.y) / deltaMS;
                if (Math.abs(deltaX) > Math.abs(deltaY))
                    user.getBody().setLinearVelocity(new Vector2(deltaX * 2, 0));
                else
                    user.getBody().setLinearVelocity(new Vector2(0, deltaY * 2));
                break;
            case TouchEvent.ACTION_MOVE:
                //userLayer.getUser().setPosition(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
                break;
        }
        return false;
    }

    /**
     * Sets the mazeSize
     * @param size
     *
     * small = 1
     * medium = 2
     * large = 3
     */
    public void setMazeSize(String size){
        size = size.toLowerCase();
        switch (size){
            case "small":
                mazeSize = 1;
                break;
            case "medium":
                mazeSize = 2;
                System.out.println("Set layout to: 2");
                break;
            case "large":
                mazeSize = 3;
                break;
            default:
                mazeSize = 1;
                System.out.println("Set layout to: default");
        }
    }

    public PhysicsWorld getPhysicsWorld() {
        return physicsWorld;
    }

    public MazeLayer getMazeLayer() {
        return mazeLayer;
    }

    public UserLayer getUserLayer() { return userLayer; }

    @Override
    public void beginContact(Contact contact) {
        final Body a = contact.getFixtureA().getBody();
        final Body b = contact.getFixtureB().getBody();

        if ((a.getUserData().equals("goal") && b.getUserData().equals("player")) || (b.getUserData().equals("goal") && a.getUserData().equals("player"))) {
            if (endTime == 0) {
                endTime = System.currentTimeMillis();
                // TODO: Stemmer tiden her
                game.endTime((int)(endTime - startTime));

                SharedPreferences prefs = ResourcesManager.getInstance().gameActivity.getSharedPreferences("hiscores", Context.MODE_PRIVATE);
                int score = prefs.getInt("seed" + mazeLayer.getMaze().getSeed(), 0);

                if ((endTime - startTime) < score) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("seed" + mazeLayer.getMaze().getSeed(), (int) (endTime - startTime));
                    editor.commit();
                }

                SceneManager.getInstance().createVictoryScene();
                SceneManager.getInstance().disposePopupScene();
            }
        }

        if (a.getUserData().equals("player")) {
            if (b.getUserData().equals("vertical")) {
                if (a.getPosition().x <= b.getPosition().x) {
                    ResourcesManager.getInstance().gameActivity.runOnUpdateThread(new Runnable() {

                        @Override
                        public void run() {
                            a.setTransform(a.getPosition().x - 0.01f, a.getPosition().y, 0);
                        }
                    });
                } else {
                    ResourcesManager.getInstance().gameActivity.runOnUpdateThread(new Runnable() {

                        @Override
                        public void run() {
                            a.setTransform(a.getPosition().x + 0.01f, a.getPosition().y, 0);
                        }
                    });
                }
            } else if (b.getUserData().equals("horizontal")) {
                if (a.getPosition().y <= b.getPosition().y) {
                    ResourcesManager.getInstance().gameActivity.runOnUpdateThread(new Runnable() {

                        @Override
                        public void run() {
                            a.setTransform(a.getPosition().x, a.getPosition().y - 0.01f, 0);
                        }
                    });
                } else {
                    ResourcesManager.getInstance().gameActivity.runOnUpdateThread(new Runnable() {

                        @Override
                        public void run() {
                            a.setTransform(a.getPosition().x, a.getPosition().y + 0.01f, 0);
                        }
                    });
                }
            }
        }

        if (b.getUserData().equals("player")) {
            if (a.getUserData().equals("vertical")) {
                if (b.getPosition().x <= a.getPosition().x) {
                    ResourcesManager.getInstance().gameActivity.runOnUpdateThread(new Runnable() {

                        @Override
                        public void run() {
                            b.setTransform(b.getPosition().x - 0.01f, b.getPosition().y, 0);
                        }
                    });
                } else {
                    ResourcesManager.getInstance().gameActivity.runOnUpdateThread(new Runnable() {

                        @Override
                        public void run() {
                            b.setTransform(b.getPosition().x + 0.01f, b.getPosition().y, 0);
                        }
                    });
                }
            } else if (a.getUserData().equals("horizontal")) {
                if (b.getPosition().y <= a.getPosition().y) {
                    ResourcesManager.getInstance().gameActivity.runOnUpdateThread(new Runnable() {

                        @Override
                        public void run() {
                            b.setTransform(b.getPosition().x, b.getPosition().y - 0.01f, 0);
                        }
                    });
                } else {
                    ResourcesManager.getInstance().gameActivity.runOnUpdateThread(new Runnable() {

                        @Override
                        public void run() {
                            b.setTransform(b.getPosition().x, b.getPosition().y + 0.01f, 0);
                        }
                    });
                }
            }
        }
    }

    void setupGhosts(){
        ghosts = new ArrayList<Ghost>();
        for (Participant p : game.getParticipants()){
            if (!p.getParticipantId().equals(game.getMyId())){
                Log.w("MultiMazed", "Created new ghost: " + p.getParticipantId() + "on position: x: " + userLayer.getStartX() + ". y: " + userLayer.getStartY());
                ghosts.add(new Ghost(userLayer.getStartX(), userLayer.getStartY(), ResourcesManager.getInstance().spriteHandler.getUserSprite(), game.getVertexBufferObjectManager(), p.getParticipantId()));
            }
        }
        for (Ghost ghost : ghosts){
            mazeLayer.getMazeBackground().attachChild(ghost);
        }
        hideGhosts();
    }

    void testSetupGhosts(){
        ghosts = new ArrayList<Ghost>();
        ArrayList<String> participantStrings = new ArrayList<String>();
        participantStrings.add("ab");
        participantStrings.add("bc");
        participantStrings.add("ac");
        for (String aids : participantStrings){
            Log.w("MultiMazed", aids);
            ghosts.add(new Ghost(45, 105, ResourcesManager.getInstance().spriteHandler.getUserSprite(), game.getVertexBufferObjectManager(), aids));
        }
        for (Ghost ghost : ghosts){
            // ghost.setScale(2.0f);
            mazeLayer.getMazeBackground().attachChild(ghost);
        }
        hideGhosts();
    }

    public void updateGhosts(int ghostX, int ghostY, String mParticipantID){
        Log.w("MultiMazed", "updateGhosts called");
        for (Ghost ghost : ghosts){
            Log.w("MultiMazed", "Checking id: getParticipantID: " + mParticipantID + ". ghostID: " + ghost.getParticipantID() + " bool: " + ghost.getParticipantID().equals(mParticipantID));
            if (ghost.getParticipantID().equals(mParticipantID)){
                Log.w("MultiMazed", "Position change: newX: " + ghostX + " newY: " + ghostY);
                ghost.setPosition(ghostX*20, ghostY*30);
            }
        }

    }

    void testUpdateGhosts(int ghostX, int ghostY, String id){
        Log.w("MultiMazed", "updateGhost");
        for (Ghost ghost : ghosts){
            if (ghost.getParticipantID() == id){
                Log.w("MultiMazed", "ParticipantID: " + ghost.getParticipantID() + ". New posX: " + (ghost.getX()+ghostX));
                ghost.setPosition(ghost.getX()+ ghostX, ghost.getY());
            }
        }
    }

    public void hideGhosts(){
        Log.w("MultiMazed", "HIDE GHOST");
        if (!ghostHidden){
            for (Ghost ghost : ghosts){
                ghost.setVisible(false);
            }
            ghostHidden = true;
        }
    }

    public void showGhosts(){
        if (ghostHidden){
        Log.w("MultiMazed", "SHOW GHOST");
            for (Ghost ghost : ghosts){
                ghost.setVisible(true);
            }
            ghostHidden = false;
        }
    }


    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
