package com.tdt4240.a19.mazegame.scenes;

import android.util.Log;
import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.GameState;
import com.tdt4240.a19.mazegame.maze.MazeLayer;
import com.tdt4240.a19.mazegame.user.User;
import com.tdt4240.a19.mazegame.user.UserLayer;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSCounter;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

import java.util.Vector;

/**
 * Created by Runar on 06.03.2015.
 */
public class GameScene extends Scene implements ContactListener {

    private PhysicsWorld physicsWorld;

    private UserLayer userLayer;
    private MazeLayer mazeLayer;

    private Vector2 pressed = new Vector2();

    /**
     * mazeSize, int size 1, 2 or 3. (1: 10x15, 2: 20x30, 3: 30x45)
     */
    private int mazeSize;

    public GameScene() {
        mazeLayer = new MazeLayer();
        userLayer = new UserLayer();

    }

    public void init() {
        GameActivity game = GameState.getInstance().getGameActivity();

        physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, 0), false);
        registerUpdateHandler(physicsWorld);

        setMazeSize("large");

        physicsWorld.setContactListener(this);


        mazeLayer.init(game.CAMERA_WIDTH/2, game.CAMERA_HEIGHT/2, 10*mazeSize, 15*mazeSize);  // pCenterX, pCenterY, mazeX, mazeY
        userLayer.init();

        setBackground(new Background(new Color(0.09804f, 0.6274f, 0.8784f)));

        attachChild(mazeLayer);
        attachChild(userLayer);

        setupFPSCounter(game);
    }

    private void setupFPSCounter(GameActivity game) {
        final FPSCounter fpsCounter = new FPSCounter();
        registerUpdateHandler(fpsCounter);

        final Text fpsText = new Text(0, 0, game.getFontHandler().getBasicFont(), "FPS:", "FPS: XXXXX".length(), game.getVertexBufferObjectManager());

        attachChild(fpsText);

        registerUpdateHandler(new TimerHandler(1 / 20.0f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                fpsText.setText("FPS: " + String.format("%.2f", fpsCounter.getFPS()));
            }
        }));
    }

    @Override
    public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
        User user = (User)userLayer.getUser();

        switch (pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                pressed = new Vector2(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
                break;
            case TouchEvent.ACTION_UP:
                float deltaX = pSceneTouchEvent.getX() - pressed.x;
                float deltaY = pSceneTouchEvent.getY() - pressed.y;
                if (Math.abs(deltaX) > Math.abs(deltaY))
                    user.getBody().setLinearVelocity(new Vector2(deltaX / 100, 0));
                else
                    user.getBody().setLinearVelocity(new Vector2(0, deltaY / 100));
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

    @Override
    public void beginContact(Contact contact) {
        final Body a = contact.getFixtureA().getBody();
        final Body b = contact.getFixtureB().getBody();

        if (a.getUserData().equals("player")) {
            if (b.getUserData().equals("vertical")) {
                if (a.getPosition().x <= b.getPosition().x) {
                    GameState.getInstance().getGameActivity().runOnUpdateThread(new Runnable() {

                        @Override
                        public void run() {
                            a.setTransform(a.getPosition().x - 0.01f, a.getPosition().y, 0);
                        }
                    });
                } else {
                    GameState.getInstance().getGameActivity().runOnUpdateThread(new Runnable() {

                        @Override
                        public void run() {
                            a.setTransform(a.getPosition().x + 0.01f, a.getPosition().y, 0);
                        }
                    });
                }
            } else if (b.getUserData().equals("horizontal")) {
                if (a.getPosition().y <= b.getPosition().y) {
                    GameState.getInstance().getGameActivity().runOnUpdateThread(new Runnable() {

                        @Override
                        public void run() {
                            a.setTransform(a.getPosition().x, a.getPosition().y - 0.01f, 0);
                        }
                    });
                } else {
                    GameState.getInstance().getGameActivity().runOnUpdateThread(new Runnable() {

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
                    GameState.getInstance().getGameActivity().runOnUpdateThread(new Runnable() {

                        @Override
                        public void run() {
                            b.setTransform(b.getPosition().x - 0.01f, b.getPosition().y, 0);
                        }
                    });
                } else {
                    GameState.getInstance().getGameActivity().runOnUpdateThread(new Runnable() {

                        @Override
                        public void run() {
                            b.setTransform(b.getPosition().x + 0.01f, b.getPosition().y, 0);
                        }
                    });
                }
            } else if (a.getUserData().equals("horizontal")) {
                if (b.getPosition().y <= a.getPosition().y) {
                    GameState.getInstance().getGameActivity().runOnUpdateThread(new Runnable() {

                        @Override
                        public void run() {
                            b.setTransform(b.getPosition().x, b.getPosition().y - 0.01f, 0);
                        }
                    });
                } else {
                    GameState.getInstance().getGameActivity().runOnUpdateThread(new Runnable() {

                        @Override
                        public void run() {
                            b.setTransform(b.getPosition().x, b.getPosition().y + 0.01f, 0);
                        }
                    });
                }
            }
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
