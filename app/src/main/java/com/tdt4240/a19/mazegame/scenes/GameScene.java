package com.tdt4240.a19.mazegame.scenes;

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

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
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

    public GameScene() {
        mazeLayer = new MazeLayer();
        userLayer = new UserLayer();

    }

    public void init() {
        physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, 0), false);
        registerUpdateHandler(physicsWorld);
        physicsWorld.setContactListener(this);

        GameActivity game = GameState.getInstance().getGameActivity();

        mazeLayer.init();
        userLayer.init();

        setBackground(new Background(new Color(0.09804f, 0.6274f, 0.8784f)));

        attachChild(mazeLayer);
        attachChild(userLayer);
    }

    @Override
    public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
        switch (pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                pressed = new Vector2(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
                break;
            case TouchEvent.ACTION_UP:
                float deltaX = pSceneTouchEvent.getX() - pressed.x;
                float deltaY = pSceneTouchEvent.getY() - pressed.y;
                if (Math.abs(deltaX) > Math.abs(deltaY))
                    ((User)userLayer.getUser()).getBody().setLinearVelocity(new Vector2(deltaX / 100, 0));
                    //userLayer.getUser().setPosition(userLayer.getUser().getX() + deltaX, userLayer.getUser().getY());
                else
                    ((User)userLayer.getUser()).getBody().setLinearVelocity(new Vector2(0, deltaY / 100));
                    //userLayer.getUser().setPosition(userLayer.getUser().getX(), userLayer.getUser().getY() + deltaY);
                break;
            case TouchEvent.ACTION_MOVE:
                //userLayer.getUser().setPosition(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
                break;
        }
        return false;
    }

    @Override
    public void beginContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        switch (a.getUserData()) {

        }
    }

    @Override
    public void endContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public PhysicsWorld getPhysicsWorld() {
        return physicsWorld;
    }
}
