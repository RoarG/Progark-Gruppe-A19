package com.tdt4240.a19.mazegame.scenes;

import com.tdt4240.a19.mazegame.assetsHandler.ResourcesManager;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

/**
 * Created by Znf on 09.04.2015.
 */
public class VictoryScene extends BaseScene implements org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener{


    private MenuScene menuChildScene;
    private final int MENU_LEADERBOARD = 0;
    private final int MENU_NEWGAME = 1;
    private final int MENU_RESTART = 2;
    private final int MENU_EXITGAME = 3;


    private void createMenuChildScene()
    {
        setBackground(new Background(new Color(0.09804f, 0.6274f, 0.8784f)));
        menuChildScene = new MenuScene(ResourcesManager.getInstance().camera);

        final IMenuItem leaderBoardMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_LEADERBOARD, ResourcesManager.getInstance().spriteHandler.getButtonSprite(),ResourcesManager.getInstance().vertexBufferObjectManager),1.2f,1);
        final IMenuItem newGameMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_NEWGAME, ResourcesManager.getInstance().spriteHandler.getButtonSprite(), ResourcesManager.getInstance().vertexBufferObjectManager),1.2f,1);
        final IMenuItem restartGameMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_RESTART, ResourcesManager.getInstance().spriteHandler.getButtonSprite(), ResourcesManager.getInstance().vertexBufferObjectManager),1.2f,1);
        final IMenuItem exitGameMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_EXITGAME, ResourcesManager.getInstance().spriteHandler.getButtonSprite(), ResourcesManager.getInstance().vertexBufferObjectManager), 1.2f,1);

        final Text victory = new Text(0, 0, ResourcesManager.getInstance().fontHandler.getBasicFont(), "CONGRATULATIONS, YOU SOLVED THE MAZE!", ResourcesManager.getInstance().vertexBufferObjectManager);
        attachChild(victory);

        Text leaderBoardText = new Text(20.0f, 5.0f, ResourcesManager.getInstance().fontHandler.getBasicFont(), "Leaderboard", ResourcesManager.getInstance().vertexBufferObjectManager);
        leaderBoardMenuItem.attachChild(leaderBoardText);
        Text newGameText = new Text(20.0f, 5.0f, ResourcesManager.getInstance().fontHandler.getBasicFont(), "New Game", ResourcesManager.getInstance().vertexBufferObjectManager);
        newGameMenuItem.attachChild(newGameText);
        Text restartText = new Text(20.0f, 5.0f, ResourcesManager.getInstance().fontHandler.getBasicFont(), "Restart", ResourcesManager.getInstance().vertexBufferObjectManager);
        restartGameMenuItem.attachChild(restartText);
        Text exitGameText = new Text(20.0f, 5.0f, ResourcesManager.getInstance().fontHandler.getBasicFont(), "Exit", ResourcesManager.getInstance().vertexBufferObjectManager);
        exitGameMenuItem.attachChild(exitGameText);

        menuChildScene.addMenuItem(leaderBoardMenuItem);
        menuChildScene.addMenuItem(newGameMenuItem);
        menuChildScene.addMenuItem(restartGameMenuItem);
        menuChildScene.addMenuItem(exitGameMenuItem);
        menuChildScene.buildAnimations();
        menuChildScene.setBackgroundEnabled(false);

        leaderBoardMenuItem.setPosition(leaderBoardMenuItem.getX(), leaderBoardMenuItem.getY()+10);
        newGameMenuItem.setPosition(newGameMenuItem.getX(),newGameMenuItem.getY()-110);
        restartGameMenuItem.setPosition(restartGameMenuItem.getX(), restartGameMenuItem.getY()+55);
        exitGameMenuItem.setPosition(exitGameMenuItem.getX(), exitGameMenuItem.getY()+110);

        menuChildScene.setOnMenuItemClickListener(this);

        setChildScene(menuChildScene);
    }
    @Override
    public void createScene() {
        createMenuChildScene();
    }

    @Override
    public void onBackKeyPressed() {
       // SceneManager.getInstance().loadMenuScene();
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
            case MENU_LEADERBOARD:
                //SceneManager.getInstance().loadGameScene(engine);
                return true;
            case MENU_NEWGAME:
                SceneManager.getInstance().loadGameScene(engine);
                return true;
            case MENU_RESTART:
                SceneManager.getInstance().loadGameScene(engine);
                return true;
            case MENU_EXITGAME:
                System.exit(0);
                return true;
            default:
                return false;
        }
    }
}
