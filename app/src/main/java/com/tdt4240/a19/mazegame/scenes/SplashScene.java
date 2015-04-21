package com.tdt4240.a19.mazegame.scenes;

import com.tdt4240.a19.mazegame.assetsHandler.ResourcesManager;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;
import org.andengine.util.color.Color;

/**
 * Created by mathi_000 on 25.03.2015.
 */
public class SplashScene extends BaseScene{

    private Sprite splash;

    @Override
    public void createScene() {
        setBackground(new Background(new Color(1.0f, 1.0f, 1,0f)));
        splash = new Sprite(0, 0, resourcesManager.splash_region, vertexBufferObjectManager)
        {
            @Override
            protected void preDraw(GLState pGLState, Camera pCamera)
            {
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
        };

        splash.setScale(1.8f);
        splash.setPosition(110, 200);
        attachChild(splash);
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
        splash.detachSelf();
        splash.dispose();
        this.detachSelf();
        this.dispose();
    }

}
