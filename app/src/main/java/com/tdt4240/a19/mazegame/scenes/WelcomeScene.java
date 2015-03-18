package com.tdt4240.a19.mazegame.scenes;

import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.GameState;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.color.Color;

/**
 * Created by runar on 3/5/15.
 */
public class WelcomeScene extends Scene implements MenuScene.IOnMenuItemClickListener{


    private MenuScene menuChildScene;
    private final int MENU_PLAY = 0;
    private final int MENU_OPTIONS = 1;

    public void init() {
        GameActivity game = GameState.getInstance().getGameActivity();
        setBackground(new Background(new Color(0.09804f, 0.6274f, 0.8784f)));
        menuChildScene = new MenuScene(game.getEngine().getCamera());

        final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY,game.getSpriteHandler().getButtonSprite(),game.getVertexBufferObjectManager()),1.2f,1);
        final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_OPTIONS,game.getSpriteHandler().getButtonSprite(), game.getVertexBufferObjectManager()),1.2f,1);


        Text playText = new Text(45.0f, 5.0f, game.getFontHandler().getBasicFont(), "Play", game.getVertexBufferObjectManager());
        playMenuItem.attachChild(playText);
        Text optionsText = new Text(20.0f, 5.0f, game.getFontHandler().getBasicFont(), "Options", game.getVertexBufferObjectManager());
        optionsMenuItem.attachChild(optionsText);

        menuChildScene.addMenuItem(playMenuItem);
        menuChildScene.addMenuItem(optionsMenuItem);
        menuChildScene.buildAnimations();
        menuChildScene.setBackgroundEnabled(false);

        playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY()+10);
        optionsMenuItem.setPosition(optionsMenuItem.getX(),optionsMenuItem.getY()-110);

        menuChildScene.setOnMenuItemClickListener(this);

        setChildScene(menuChildScene);
    }

    @Override
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
        switch(pMenuItem.getID()){
            case MENU_PLAY:

                GameState.getInstance().getGameScene();
                return true;
            case MENU_OPTIONS:
                System.out.println("TEST");
                return true;
            default:
                return false;
        }
    }
}
