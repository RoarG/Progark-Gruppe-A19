package com.tdt4240.a19.mazegame.assetsHandler;

import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.GameState;


import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.region.ITextureRegion;

/**
 * Created by runar on 3/5/15.
 */
public class SpriteHandler {

    private BuildableBitmapTextureAtlas buttons;
    private BuildableBitmapTextureAtlas backgrounds;
    private BuildableBitmapTextureAtlas sprites;

    private ITextureRegion play_button;
    private ITextureRegion user;
    private ITextureRegion mazeBackground, mazeHoriWall, mazeVertwall;

    public SpriteHandler() {

    }

    public void setupAtlases() {
        GameActivity game = GameState.getInstance().getGameActivity();

        buttons = new BuildableBitmapTextureAtlas(game.getTextureManager(), 1024, 1024);
        backgrounds = new BuildableBitmapTextureAtlas(game.getTextureManager(), 2048, 2048);
        sprites = new BuildableBitmapTextureAtlas(game.getTextureManager(), 1024, 1024);
    }

    public void setupSprites() {
        GameActivity game = GameState.getInstance().getGameActivity();

        play_button = BitmapTextureAtlasTextureRegionFactory.createFromAsset(buttons, game, "button.png");
        user = BitmapTextureAtlasTextureRegionFactory.createFromAsset(buttons, game, "user.png");
        mazeBackground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "BlackBackground.png");
        mazeHoriWall = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "WhiteHorizontalWall.png");
        mazeVertwall = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "WhiteVerticalWall.png");


    }

    public void buildAtlases() {
        try {
            buttons.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,1,0));
            backgrounds.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
            sprites.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            e.printStackTrace();
        }
    }

    public void loadAtlases() {
        this.buttons.load();
        this.backgrounds.load();
        this.sprites.load();
    }

    public ITextureRegion getButtonSprite() {
        return play_button;
    }

    public ITextureRegion getUserSprite() {
        return user;
    }

    public  ITextureRegion getMazeBackground(){ return mazeBackground;}

    public ITextureRegion getMazeHoriWall(){ return mazeHoriWall;}

    public ITextureRegion getMazeVertwall() { return mazeVertwall; }
}
