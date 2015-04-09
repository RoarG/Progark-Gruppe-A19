package com.tdt4240.a19.mazegame.scenes;

import android.os.CountDownTimer;

import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.assetsHandler.ResourcesManager;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

/**
 * Created by Runar on 06.03.2015.
 */
public class GameRoomScene extends BaseScene implements org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener {

    private MenuScene menuChildScene;
    private final int MENU_START = 0;
    private final int MENU_LEAVE = 1;

    private void createMenuChildScene() {
        setBackground(new Background(new Color(0.31f, 0.61f, 0.40f)));
        menuChildScene = new MenuScene(ResourcesManager.getInstance().camera);

        final IMenuItem startMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_START, ResourcesManager.getInstance().spriteHandler.getButtonSprite(), ResourcesManager.getInstance().vertexBufferObjectManager), 1.2f, 1);
        final IMenuItem leaveMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_LEAVE, ResourcesManager.getInstance().spriteHandler.getButtonSprite(), ResourcesManager.getInstance().vertexBufferObjectManager), 1.2f, 1);

        Text startText = new Text(20.0f, 5.0f, ResourcesManager.getInstance().fontHandler.getBasicFont(), "Start", ResourcesManager.getInstance().vertexBufferObjectManager);
        startMenuItem.attachChild(startText);
        Text leaveText = new Text(20.0f, 5.0f, ResourcesManager.getInstance().fontHandler.getBasicFont(), "Exit", ResourcesManager.getInstance().vertexBufferObjectManager);
        leaveMenuItem.attachChild(leaveText);

        menuChildScene.addMenuItem(startMenuItem);
        menuChildScene.addMenuItem(leaveMenuItem);
        menuChildScene.buildAnimations();
        menuChildScene.setBackgroundEnabled(false);

        startMenuItem.setPosition(startMenuItem.getX(), startMenuItem.getY() + 10);
        leaveMenuItem.setPosition(leaveMenuItem.getX(), leaveMenuItem.getY() + 110);

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
            case MENU_START:
                SceneManager.getInstance().loadGameScene(engine);
                return true;
            case MENU_LEAVE:
                SceneManager.getInstance().loadMenuScene();
                return true;
            default:
                return false;
        }
    }

}