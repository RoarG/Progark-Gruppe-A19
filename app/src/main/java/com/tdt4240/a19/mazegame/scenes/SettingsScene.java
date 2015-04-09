package com.tdt4240.a19.mazegame.scenes;

import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.assetsHandler.ResourcesManager;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;
import org.andengine.entity.sprite.ButtonSprite;

/**
 * Created by Znf on 26.03.2015.
 */
public class SettingsScene extends BaseScene implements org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener{

    //private ButtonSprite sounds;
    private MenuScene menuChildScene;
    private final int MENU_SOUNDS = 0;
    private final int MENU_MUSIC = 1;
    private final int MENU_BACK = 2;

    private void createMenuChildScene()
    {
        setBackground(new Background(new Color(0.09804f, 0.6274f, 0.8784f)));
        menuChildScene = new MenuScene(ResourcesManager.getInstance().camera);

        final IMenuItem soundsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_SOUNDS, ResourcesManager.getInstance().spriteHandler.getButtonSprite(),ResourcesManager.getInstance().vertexBufferObjectManager),1.2f,1);
        final IMenuItem musicMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_MUSIC, ResourcesManager.getInstance().spriteHandler.getButtonSprite(), ResourcesManager.getInstance().vertexBufferObjectManager),1.2f,1);
        final IMenuItem backMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_BACK, ResourcesManager.getInstance().spriteHandler.getButtonSprite(), ResourcesManager.getInstance().vertexBufferObjectManager), 1.2f,1);


        Text soundsText = new Text(20.0f, 5.0f, ResourcesManager.getInstance().fontHandler.getBasicFont(), "Sounds", ResourcesManager.getInstance().vertexBufferObjectManager);
        soundsMenuItem.attachChild(soundsText);
        Text musicText = new Text(20.0f, 5.0f, ResourcesManager.getInstance().fontHandler.getBasicFont(), "Music", ResourcesManager.getInstance().vertexBufferObjectManager);
        musicMenuItem.attachChild(musicText);
        Text backText = new Text(20.0f, 5.0f, ResourcesManager.getInstance().fontHandler.getBasicFont(), "Back", ResourcesManager.getInstance().vertexBufferObjectManager);
        backMenuItem.attachChild(backText);

        menuChildScene.addMenuItem(soundsMenuItem);
        menuChildScene.addMenuItem(musicMenuItem);
        menuChildScene.addMenuItem(backMenuItem);
        menuChildScene.buildAnimations();
        menuChildScene.setBackgroundEnabled(false);

        soundsMenuItem.setPosition(soundsMenuItem.getX(), soundsMenuItem.getY()+10);
        musicMenuItem.setPosition(musicMenuItem.getX(),musicMenuItem.getY()-110);
        backMenuItem.setPosition(backMenuItem.getX(), backMenuItem.getY()+110);

        menuChildScene.setOnMenuItemClickListener(this);

        setChildScene(menuChildScene);
    }
    @Override
    public void createScene() {
        createMenuChildScene();
    }

    @Override
    public void onBackKeyPressed() {
       SceneManager.getInstance().loadMenuScene();
        //System.exit(0);
    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_MENUSCENE;
    }

    @Override
    public void disposeScene() {

    }

    @Override
    public boolean onMenuItemClicked(org.andengine.entity.scene.menu.MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
        switch (pMenuItem.getID()) {
            case MENU_SOUNDS:
                //SceneManager.getInstance().loadGameScene(engine);
                return true;
            case MENU_MUSIC:
                //SceneManager.getInstance().createSettingsScene();
                return true;
            case MENU_BACK:
                SceneManager.getInstance().loadMenuScene();
                return true;
            default:
                return false;
        }
    }
}
