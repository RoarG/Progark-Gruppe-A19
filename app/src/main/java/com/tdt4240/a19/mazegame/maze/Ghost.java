package com.tdt4240.a19.mazegame.maze;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Bakkan on 21.04.2015.
 */
public class Ghost extends Sprite {

    private String participantID;
    private boolean updated;


    public Ghost (float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager,String mParticipantId){
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
        this.participantID = mParticipantId;
        this.updated = false;
    }

    public String getParticipantID(){ return participantID; }

    public boolean isUpdated() { return updated; }
}
