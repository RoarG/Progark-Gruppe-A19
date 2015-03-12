package com.tdt4240.a19.mazegame.scenes;

import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.GameState;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

/**
 * Created by runar on 3/5/15.
 */
public class WelcomeScene extends Scene {

    private ButtonSprite startButton;

    public void init() {
        GameActivity game = GameState.getInstance().getGameActivity();

        setBackground(new Background(new Color(0.09804f, 0.6274f, 0.8784f)));

        float x = 165.0f;
        float y = 300.0f;

        startButton = new ButtonSprite(x, y + 150.0f, game.getSpriteHandler().getButtonSprite(), game.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                return true;
            }
        };
        attachChild(startButton);
        registerTouchArea(startButton);

        Text playText = new Text(45.0f, 5.0f, game.getFontHandler().getBasicFont(), "Play", game.getVertexBufferObjectManager());
        startButton.attachChild(playText);
    }
}
