package com.tdt4240.a19.mazegame.assetsHandler;

import android.util.Log;

import com.google.android.gms.games.Game;
import com.tdt4240.a19.mazegame.GameActivity;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.io.IOException;

/**
 * Created by mathi_000 on 25.03.2015.
 */
public class ResourcesManager {

    private static final String TAG = "MultiMazed";

    private static final ResourcesManager INSTANCE = new ResourcesManager();

    public Engine engine;
    public GameActivity gameActivity;
    public Camera camera;
    public VertexBufferObjectManager vertexBufferObjectManager;

    public SpriteHandler spriteHandler;
    public FontHandler fontHandler;
    public ITextureRegion splash_region;
    private BitmapTextureAtlas splashTextureAtlas;

    public Music music;

    public void loadMenuResources() {
        loadMenuGraphics();
        loadGameAudio();
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
        try
        {
            music = MusicFactory.createMusicFromAsset(engine.getMusicManager(), this.gameActivity, "music/test.ogg");
            Log.d(TAG, " Music" + music);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadSplashScreen() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        splashTextureAtlas = new BitmapTextureAtlas(gameActivity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, gameActivity, "logo256.png", 0, 0);
        splashTextureAtlas.load();
    }

    public void unloadSplashScreen() {
        splashTextureAtlas.unload();
        splash_region = null;
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

