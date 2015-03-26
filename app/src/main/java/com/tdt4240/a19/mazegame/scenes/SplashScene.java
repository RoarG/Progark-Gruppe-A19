package com.tdt4240.a19.mazegame.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.color.Color;

/**
 * Created by mathi_000 on 25.03.2015.
 */
public class SplashScene extends BaseScene{


    @Override
    public void createScene() {
        setBackground(new Background(new Color(0.09804f, 0.6274f, 0.8784f)));
    }

    @Override
    public void onBackKeyPressed() {

    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_SPLASHSCENE;
    }

    @Override
    public void disposeScene()
    {
        this.detachSelf();
        this.dispose();
    }

}
