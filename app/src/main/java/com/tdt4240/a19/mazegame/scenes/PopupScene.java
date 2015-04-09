package com.tdt4240.a19.mazegame.scenes;

import com.tdt4240.a19.mazegame.assetsHandler.ResourcesManager;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.sprite.Sprite;

/**
 * Created by Mathias on 4/9/2015.
 *
public class PopupScene extends BaseScene {

    private MenuScene popupMenuChildScene;
    private final int POPUP_BACKGROUND = 0;
    @Override
    public void createScene() {
        setBackgroundEnabled(false);
        popupMenuChildScene = new MenuScene(ResourcesManager.getInstance().camera);

        final IMenuItem popupBackground = new SpriteMenuItem(POPUP_BACKGROUND,ResourcesManager.getInstance().spriteHandler.getPopupBackground(),ResourcesManager.getInstance().vertexBufferObjectManager);

        popupMenuChildScene.addMenuItem(popupBackground);

        popupBackground.setPosition(popupBackground.getX(), popupBackground.getY());

        setChildScene(popupMenuChildScene);
    }

    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().disposePopupScene();
    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return null;
    }

    @Override
    public void disposeScene() {

    }
}
 */
