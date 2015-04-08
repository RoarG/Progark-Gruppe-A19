package com.tdt4240.a19.mazegame.user;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.maze.Maze;
import com.tdt4240.a19.mazegame.scenes.GameScene;
import com.tdt4240.a19.mazegame.scenes.SceneManager;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.List;

/**
 * Created by Runar on 06.03.2015.
 */
public class User extends Sprite {

    private Body body;

    private long startTime;
    private long endTime;

    private boolean canMoveNorth = true;
    private boolean canMoveSouth = true;
    private boolean canMoveEast = true;
    private boolean canMoveWest = true;

    public User(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager, PhysicsWorld physicsWorld) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
        createPhysics(physicsWorld);
    }

    private void createPhysics(PhysicsWorld physicsWorld) {

        body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyDef.BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
        body.setUserData("player");
        body.setBullet(true);

        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false) {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
            }
        });
    }

    public Body getBody() {
        return body;
    }

    public boolean canMoveWest() {
        return canMoveWest;
    }

    public boolean canMoveEast() {
        return canMoveEast;
    }

    public boolean canMoveNorth() {
        return canMoveNorth;
    }

    public boolean canMoveSouth() {
        return canMoveSouth;
    }
}
