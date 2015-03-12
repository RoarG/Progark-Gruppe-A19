package com.tdt4240.a19.mazegame.user;

import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.GameState;
import com.tdt4240.a19.mazegame.assetsHandler.SpriteHandler;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Runar on 06.03.2015.
 */
public class UserLayer extends Entity {

    private Sprite user;

    public UserLayer() {

    }

    public void init() {
        GameActivity game = GameState.getInstance().getGameActivity();

        user = new User(100.0f, 100.0f, game.getSpriteHandler().getUserSprite(), game.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                return true;
            }
        };

        attachChild(user);
    }
}
