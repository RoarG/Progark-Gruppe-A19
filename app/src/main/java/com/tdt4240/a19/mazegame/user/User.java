package com.tdt4240.a19.mazegame.user;

import com.tdt4240.a19.mazegame.GameActivity;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Runar on 06.03.2015.
 */
public class User extends Sprite {

    private long startTime;
    private long endTime;

    public User(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
    }
}
