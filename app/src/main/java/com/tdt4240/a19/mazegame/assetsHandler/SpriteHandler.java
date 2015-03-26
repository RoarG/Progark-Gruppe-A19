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
    private ITextureRegion whiteBackground, blackBackground, redBackground, turquoiseBackground;
    private ITextureRegion whw10x15, wvw10x15, whw20x30, wvw20x30, whw30x45, wvw30x45;
    private ITextureRegion bhw10x15, bvw10x15, bhw20x30, bvw20x30, bhw30x45, bvw30x45;
    private ITextureRegion rhw10x15, rvw10x15, rhw20x30, rvw20x30, rhw30x45, rvw30x45;
    private ITextureRegion thw10x15, tvw10x15, thw20x30, tvw20x30, thw30x45, tvw30x45;
    private ITextureRegion start10x15, goal10x15, start20x30, goal20x30, start30x45, goal30x45;

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

        // Backgrounds
        blackBackground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "BlackBackground.png");
        whiteBackground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "WhiteBackground.png");
        redBackground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "RedBackground.png");
        turquoiseBackground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "TurquoiseBackground.png");

        /**
         * 10x15 setup
         */

        // 10x15: Horizontal walls
        whw10x15 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "whw10x15.png");
        bhw10x15 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "bhw10x15.png");
        rhw10x15 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "rhw10x15.png");
        thw10x15 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "thw10x15.png");

        // 10x15: Vertical walls
        wvw10x15 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "wvw10x15.png");
        bvw10x15 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "bvw10x15.png");
        rvw10x15 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "rvw10x15.png");
        tvw10x15 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "tvw10x15.png");

        // 10x15: Start and goal
        start10x15 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "start10x15.png");
        goal10x15 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "goal10x15.png");

        /**
         * 20x30 setup
         */

        // 20x30: Horizontal walls
        whw20x30 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "whw20x30.png");
        bhw20x30= BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "bhw20x30.png");
        rhw20x30 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "rhw20x30.png");
        thw20x30 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "thw20x30.png");

        // 20x30: Vertical walls
        wvw20x30 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "wvw20x30.png");
        bvw20x30 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "bvw20x30.png");
        rvw20x30 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "rvw20x30.png");
        tvw20x30 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "tvw20x30.png");

        // 20x30: Start and goal
        start20x30 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "start20x30.png");
        goal20x30 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "goal20x30.png");

        /**
         * 30x45 setup
         */

        // 30x45: Horizontal walls
        whw30x45 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "whw30x45.png");
        bhw30x45= BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "bhw30x45.png");
        rhw30x45= BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "rhw30x45.png");
        thw30x45= BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "thw30x45.png");

        // 30x45: Vertical walls
        wvw30x45 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "wvw30x45.png");
        bvw30x45 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "bvw30x45.png");
        rvw30x45 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "rvw30x45.png");
        tvw30x45 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "tvw30x45.png");

        // 30x45: Start and goal
        start30x45 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "start30x45.png");
        goal30x45 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "goal30x45.png");


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

    public  ITextureRegion getGoal(int mazeSize) {
        if (mazeSize == 1){
            return goal10x15;
        } else if (mazeSize == 2){
            return goal20x30;
        } else {
            return goal30x45;
        }
    }

    public ITextureRegion getStart(int mazeSize) {
        if (mazeSize == 1){
            return start10x15;
        } else if (mazeSize == 2){
            return start20x30;
        } else {
            return start30x45;
        }
    }

    public  ITextureRegion getMazeBackground(String color){
        switch (color){
            case "White":
                return whiteBackground;
            case "Black":
                return blackBackground;
            case "Red":
                return redBackground;
            case "Turquoise":
                return turquoiseBackground;
            default:
                return blackBackground;
                
        }
    }

    public ITextureRegion getHoriWall(String color, int mazeSize){
        if (mazeSize == 1){
            switch (color){
                case "White":
                    return whw10x15;
                case "Black":
                    return bhw10x15;
                case "Red":
                    return rhw10x15;
                case "Turquoise":
                    return thw10x15;
                default:
                    return whw10x15;
            }
        } else if (mazeSize == 2){
            switch (color){
                case "White":
                    return whw20x30;
                case "Black":
                    return bhw20x30;
                case "Red":
                    return rhw20x30;
                case "Turquoise":
                    return thw20x30;
                default:
                    return whw20x30;
            }
        } else {
            switch (color){
                case "White":
                    return whw30x45;
                case "Black":
                    return bhw30x45;
                case "Red":
                    return rhw30x45;
                case "Turquoise":
                    return thw30x45;
                default:
                    return whw30x45;
            }
        }
    }

    public ITextureRegion getVertWall(String color, int mazeSize) {
        System.out.println("Color: " + color);
        System.out.println("mazeSize: " + mazeSize);
        if (mazeSize == 1){
            switch (color){
                case "White":
                    return wvw10x15;
                case "Black":
                    return bvw10x15;
                case "Red":
                    return rvw10x15;
                case "Turquoise":
                    return tvw10x15;
                default:
                    return wvw10x15;
            }
        } else if (mazeSize == 2){
            switch (color){
                case "White":
                    return wvw20x30;
                case "Black":
                    return bvw20x30;
                case "Red":
                    return rvw20x30;
                case "Turquoise":
                    return tvw20x30;
                default:
                    return wvw20x30;
            }
        } else {
            switch (color){
                case "White":
                    return wvw30x45;
                case "Black":
                    return bvw30x45;
                case "Red":
                    return rvw30x45;
                case "Turquoise":
                    return tvw30x45;
                default:
                    return wvw30x45;
            }
        }
    }
}
