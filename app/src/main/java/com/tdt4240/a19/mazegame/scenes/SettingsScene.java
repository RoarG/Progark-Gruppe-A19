package com.tdt4240.a19.mazegame.scenes;

import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.assetsHandler.ResourcesManager;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;
import org.andengine.entity.sprite.ButtonSprite;

/**
 * Created by Znf on 26.03.2015.
 */
public class SettingsScene extends BaseScene {

    private ButtonSprite sounds;

    @Override
    public void createScene() {
        GameActivity game = ResourcesManager.getInstance().gameActivity;
        setBackground(new Background(new Color(0.09804f, 0.6274f, 0.8784f)));

        float x = 165.0f;
        float y = 300.0f;

        sounds = new ButtonSprite(x, y + 150.0f, ResourcesManager.getInstance().spriteHandler.getButtonSprite(), game.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);

                //Mute/unmute the sound of the game on buttonclick
                return true;
            }
        };
        attachChild(sounds);
        registerTouchArea(sounds);

        Text infoText = new Text(45.0f, 5.0f, ResourcesManager.getInstance().fontHandler.getBasicFont(), "Sounds On", game.getVertexBufferObjectManager());
        sounds.attachChild(infoText);
    }

    @Override
    public void onBackKeyPressed() {

    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_SETTINGSSCENE;
    }

    @Override
    public void disposeScene() {

    }
}
