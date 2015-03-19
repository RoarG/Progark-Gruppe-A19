package com.tdt4240.a19.mazegame.user;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.GameState;
import com.tdt4240.a19.mazegame.scenes.GameScene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Runar on 06.03.2015.
 */
public class User extends Sprite {

    private Body body;

    private long startTime;
    private long endTime;

    public User(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
        createPhysics();
    }

    private void createPhysics() {
        PhysicsWorld physicsWorld = ((GameScene)GameState.getInstance().getGameScene()).getPhysicsWorld();

        body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyDef.BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

        body.setUserData("player");
        body.setFixedRotation(true);

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
}
