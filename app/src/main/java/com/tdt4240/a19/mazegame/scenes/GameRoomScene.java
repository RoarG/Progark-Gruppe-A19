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
 * Created by Runar on 06.03.2015.
 */
public class GameRoomScene extends Scene {

    private boolean singlePlayer = false;
    private ButtonSprite startButton;

    public void initScene() {
        //asd
        GameActivity game = GameState.getInstance().getGameActivity();

        setBackground(new Background(new Color(0.09804f, 0.6274f, 0.8784f)));

        float x = 165.0f;
        float y = 300.0f;

        startButton = new ButtonSprite(x, y + 150.0f, game.getSpriteHandler().getButtonSprite(), game.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                //GameState.getInstance().getSettingsScene();
               // startButton.setColor(new Color(0.09804f, 0.6274f, 0.8784f));
                return true;
            }
        };
        attachChild(startButton);
        registerTouchArea(startButton);

        Text startText = new Text(45.0f, 5.0f, game.getFontHandler().getBasicFont(), "Start game", game.getVertexBufferObjectManager());
        startButton.attachChild(startText);


    }
}
