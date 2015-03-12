package com.tdt4240.a19.mazegame.assetsHandler;

import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.GameState;

import org.andengine.entity.sprite.Sprite;
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

    private ITextureRegion button;
    private ITextureRegion user;

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

        button = BitmapTextureAtlasTextureRegionFactory.createFromAsset(buttons, game, "button.png");
        user = BitmapTextureAtlasTextureRegionFactory.createFromAsset(buttons, game, "user.png");
    }

    public void buildAtlases() {
        try {
            buttons.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
            backgrounds.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
            sprites.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            e.printStackTrace();
        }
    }

    public void loadAtlases() {
        buttons.load();
        backgrounds.load();
        sprites.load();
    }

    public ITextureRegion getButtonSprite() {
        return button;
    }

    public ITextureRegion getUserSprite() {
        return user;
    }
}
