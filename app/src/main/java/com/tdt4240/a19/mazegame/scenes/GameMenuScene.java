package com.tdt4240.a19.mazegame.scenes;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.assetsHandler.ResourcesManager;
import com.tdt4240.a19.mazegame.scenes.BaseScene;
import com.tdt4240.a19.mazegame.scenes.SceneManager;

/**
 * Created by mathi_000 on 25.03.2015.
 */
public class GameMenuScene  extends BaseScene implements org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener{
    private MenuScene menuChildScene;
    private final int MENU_PLAY = 0;
    private final int MENU_MULTIPLAYER = 1;
    private final int MENU_OPTIONS = 2;
    private GameActivity game;

    private void createMenuChildScene()
    {
        setBackground(new Background(new Color(0.09804f, 0.6274f, 0.8784f)));
        menuChildScene = new MenuScene(ResourcesManager.getInstance().camera);

        final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, ResourcesManager.getInstance().spriteHandler.getButtonSprite(),ResourcesManager.getInstance().vertexBufferObjectManager),1.2f,1);
        final IMenuItem multiplayerMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_MULTIPLAYER,ResourcesManager.getInstance().spriteHandler.getButtonSprite(),ResourcesManager.getInstance().vertexBufferObjectManager),1.2f,1);
        final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_OPTIONS, ResourcesManager.getInstance().spriteHandler.getButtonSprite(), ResourcesManager.getInstance().vertexBufferObjectManager),1.2f,1);

        game = ResourcesManager.getInstance().gameActivity;

        Text playText = new Text(45.0f, 5.0f, ResourcesManager.getInstance().fontHandler.getBasicFont(), "Play", ResourcesManager.getInstance().vertexBufferObjectManager);
        playMenuItem.attachChild(playText);
        Text multiplayerText = new Text(5.0f,5.0f, ResourcesManager.getInstance().fontHandler.getBasicFont(),"Multiplayer",ResourcesManager.getInstance().vertexBufferObjectManager);
        multiplayerMenuItem.attachChild(multiplayerText);
        Text optionsText = new Text(20.0f, 5.0f, ResourcesManager.getInstance().fontHandler.getBasicFont(), "Options", ResourcesManager.getInstance().vertexBufferObjectManager);
        optionsMenuItem.attachChild(optionsText);

        menuChildScene.addMenuItem(playMenuItem);
        menuChildScene.addMenuItem(multiplayerMenuItem);
        menuChildScene.addMenuItem(optionsMenuItem);
        menuChildScene.buildAnimations();
        menuChildScene.setBackgroundEnabled(false);

        playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY()-60);
        multiplayerMenuItem.setPosition(multiplayerMenuItem.getX(),multiplayerMenuItem.getY()+0);
        optionsMenuItem.setPosition(optionsMenuItem.getX(),optionsMenuItem.getY()+60);

        menuChildScene.setOnMenuItemClickListener(this);

        setChildScene(menuChildScene);
    }
    @Override
    public void createScene() {
        createMenuChildScene();
    }

    @Override
    public void onBackKeyPressed() {
        System.exit(0);
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
        switch(pMenuItem.getID())
        {
            case MENU_PLAY:
                SceneManager.getInstance().loadGameScene(engine);
                return true;
            case MENU_OPTIONS:
                SceneManager.getInstance().loadSettingScene(engine);
                return true;
            case MENU_MULTIPLAYER:
               // SceneManager.getInstance().loadGameRoomScene(engine);
                game.startQuickGame();
                return true;
            default:
                return false;
        }
    }
}
