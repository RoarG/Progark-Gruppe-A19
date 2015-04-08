package com.tdt4240.a19.mazegame.assetsHandler;

import com.google.android.gms.games.Game;
import com.tdt4240.a19.mazegame.GameActivity;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by mathi_000 on 25.03.2015.
 */
public class ResourcesManager {

    private static final ResourcesManager INSTANCE = new ResourcesManager();

    public Engine engine;
    public GameActivity gameActivity;
    public Camera camera;
    public VertexBufferObjectManager vertexBufferObjectManager;

    public SpriteHandler spriteHandler;
    public FontHandler fontHandler;

    public void loadMenuResources() {
        loadMenuGraphics();
        loadMenuAudio();
        loadGameFonts();
    }

    public void loadGameResources() {
        loadGameGraphics();
        loadGameFonts();
        loadGameAudio();
    }

    private void loadMenuGraphics() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        spriteHandler = new SpriteHandler();
        spriteHandler.setupAtlases();
        spriteHandler.setupSprites();
        spriteHandler.buildAtlases();
        spriteHandler.loadAtlases();
    }

    private void loadMenuAudio() {

    }

    private void loadGameGraphics() {

    }

    private void loadGameFonts() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("fonts/");

        fontHandler = new FontHandler();
        fontHandler.createFonts();
        fontHandler.loadFonts();
    }

    private void loadGameAudio() {

    }

    public void loadSplashScreen() {

    }

    public void unloadSplashScreen() {


    }
    public static void prepareManager(Engine engine, GameActivity gameActivity, Camera camera, VertexBufferObjectManager vertexBufferObjectManager){
        getInstance().engine = engine;
        getInstance().gameActivity= gameActivity;
        getInstance().camera = camera;
        getInstance().vertexBufferObjectManager = vertexBufferObjectManager;
    }
    public static ResourcesManager getInstance(){
        return INSTANCE;
    }
}

