package com.tdt4240.a19.mazegame.assetsHandler;

import com.tdt4240.a19.mazegame.GameActivity;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;

/**
 * Created by runar on 3/5/15.
 */
public class SpriteHandler {

    private BuildableBitmapTextureAtlas buttons;
    private BuildableBitmapTextureAtlas backgrounds;
    private BuildableBitmapTextureAtlas sprites;
    private BuildableBitmapTextureAtlas popup;
    private BuildableBitmapTextureAtlas logos;

    private ITextureRegion play_button;
    private ITextureRegion user;
    private ITextureRegion androidBg, blackBg, blueBg, yellowBg;
    private ITextureRegion androidH, androidV, blackH, blackV, blueH, blueV, redH, redV;
    private ITextureRegion start, goal;
    private ITextureRegion popupBackground;
    private ITextureRegion logoIcon;
    public SpriteHandler() {

    }

    public void setupAtlases() {
        GameActivity game = ResourcesManager.getInstance().gameActivity;

        buttons = new BuildableBitmapTextureAtlas(game.getTextureManager(), 1024, 1024);
        backgrounds = new BuildableBitmapTextureAtlas(game.getTextureManager(), 2048, 2048);
        sprites = new BuildableBitmapTextureAtlas(game.getTextureManager(), 2048, 2048);
        popup = new BuildableBitmapTextureAtlas(game.getTextureManager(), 400,400);
        logos = new BuildableBitmapTextureAtlas(game.getTextureManager(),1024,1024);
    }

    public void setupSprites() {
        GameActivity game = ResourcesManager.getInstance().gameActivity;

        play_button = BitmapTextureAtlasTextureRegionFactory.createFromAsset(buttons, game, "button.png");
        user = BitmapTextureAtlasTextureRegionFactory.createFromAsset(buttons, game, "user.png");

        // Logos
        logoIcon = BitmapTextureAtlasTextureRegionFactory.createFromAsset(buttons,game,"logo256.png");
        // PopupBackground
        popupBackground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(popup,game,"PopupBackground.png");

        // Backgrounds
        androidBg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "androidBackground.png");
        blackBg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "blackBackground.png");
        blueBg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "blueBackground.png");
        yellowBg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "yellowBackground.png");

        // Horizontal walls
        androidH = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "androidHori.png");
        blackH = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "blackHori.png");
        blueH = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "blueHori.png");
        redH = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "redHori.png");

        // Vertical walls
        androidV = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "androidVert.png");
        blackV = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "blackVert.png");
        blueV = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "blueVert.png");
        redV = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "redVert.png");

        // Start and goal
        start = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "start.png");
        goal = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sprites, game, "goal.png");



    }

    public void buildAtlases() {
        try {
            buttons.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,1,0));
            backgrounds.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
            sprites.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
            popup.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,0,1));
            logos.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0,0,1));
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            e.printStackTrace();
        }
    }

    public void loadAtlases() {
        this.buttons.load();
        this.backgrounds.load();
        this.sprites.load();
        this.popup.load();
        this.logos.load();
    }

    public ITextureRegion getButtonSprite() {
        return play_button;
    }

    public ITextureRegion getUserSprite() {
        return user;
    }

    public ITextureRegion getPopupBackground(){ return popupBackground; }

    public ITextureRegion getLogoIcon(){ return logoIcon; }

    public  ITextureRegion getGoal() {
        return goal;
    }

    public ITextureRegion getStart() { return start; }

    public  ITextureRegion getMazeBackground(String color){
        switch (color){
            case "Android":
                return androidBg;
            case "Black":
                return blackBg;
            case "Blue":
                return blueBg;
            case "yellow":
                return yellowBg;
            default:
                return androidBg;
                
        }
    }

    public ITextureRegion getHoriWall(String color){
        switch (color){
            case "Android":
                return androidH;
            case "Black":
                return blackH;
            case "Blue":
                return blueH;
            case "Red":
                return redH;
            default:
                return blackH;
        }


    }

    public ITextureRegion getVertWall(String color) {
        switch (color){
            case "Android":
                return androidV;
            case "Black":
                return blackV;
            case "Blue":
                return blueV;
            case "Red":
                return redV;
            default:
                return blackV;
        }
    }

}
