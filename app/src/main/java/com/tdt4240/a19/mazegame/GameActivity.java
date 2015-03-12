package com.tdt4240.a19.mazegame;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tdt4240.a19.mazegame.assetsHandler.FontHandler;
import com.tdt4240.a19.mazegame.assetsHandler.SpriteHandler;
import com.tdt4240.a19.mazegame.scenes.GameScene;
import com.tdt4240.a19.mazegame.scenes.WelcomeScene;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

public class GameActivity extends BaseGameActivity {

    public static final int CAMERA_WIDTH = 480;
    public static final int CAMERA_HEIGHT = 800;

    private Camera camera;

    private SpriteHandler spriteHandler;
    private FontHandler fontHandler;

    @Override
    public EngineOptions onCreateEngineOptions() {
        BoundCamera camera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        //camera.setBounds(-400, -500, 2000, 800);
        //camera.setBoundsEnabled(true);
        GameState.getInstance().setGameActivity(this);
        this.camera = camera;
        return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        spriteHandler = new SpriteHandler();
        spriteHandler.setupAtlases();
        spriteHandler.setupSprites();
        spriteHandler.buildAtlases();
        spriteHandler.loadAtlases();

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("fonts/");

        fontHandler = new FontHandler();
        fontHandler.createFonts();
        fontHandler.loadFonts();

        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        pOnCreateSceneCallback.onCreateSceneFinished(GameState.getInstance().getGameScene() );
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        if (pScene instanceof WelcomeScene)
            ((WelcomeScene) pScene).init();
        else if (pScene instanceof GameScene)
            ((GameScene) pScene).init();
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

    public SpriteHandler getSpriteHandler() {
        return spriteHandler;
    }

    public FontHandler getFontHandler() {
        return fontHandler;
    }
}
