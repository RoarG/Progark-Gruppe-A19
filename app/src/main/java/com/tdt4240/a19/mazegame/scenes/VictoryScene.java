package com.tdt4240.a19.mazegame.scenes;

import android.content.res.Resources;

import com.google.android.gms.common.data.DataBuffer;
import com.google.android.gms.games.PlayerEntity;
import com.tdt4240.a19.mazegame.assetsHandler.ResourcesManager;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.list.IList;
import org.andengine.util.color.Color;

/**
 * Created by Znf on 09.04.2015.
 */
public class VictoryScene extends BaseScene implements org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener{


    private MenuScene menuChildScene;
    private final int MENU_LEADERBOARD = 0;
    private final int MENU_NEWGAME = 1;
    private final int MENU_MENU = 2;
    private final int MENU_EXITGAME = 3;
    private final int MY_TIME = 4;
    private String endTime = "0";
    String userName = "";

    private void createMenuChildScene()
    {
        setBackground(new Background(new Color(1.0f, 1.0f, 1.0f)));
        menuChildScene = new MenuScene(ResourcesManager.getInstance().camera);

        endTime = SceneManager.getInstance().getGameScene().getEndTime();
        final IMenuItem leaderBoardMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_LEADERBOARD, ResourcesManager.getInstance().spriteHandler.getButtonSprite(),ResourcesManager.getInstance().vertexBufferObjectManager),1.2f,1);
        final IMenuItem newGameMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_NEWGAME, ResourcesManager.getInstance().spriteHandler.getButtonSprite(), ResourcesManager.getInstance().vertexBufferObjectManager),1.2f,1);
        final IMenuItem menuGameMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_MENU, ResourcesManager.getInstance().spriteHandler.getButtonSprite(), ResourcesManager.getInstance().vertexBufferObjectManager),1.2f,1);
        final IMenuItem exitGameMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_EXITGAME, ResourcesManager.getInstance().spriteHandler.getButtonSprite(), ResourcesManager.getInstance().vertexBufferObjectManager), 1.2f,1);
        final IMenuItem myTime = new TextMenuItem(MY_TIME, ResourcesManager.getInstance().fontHandler.getBasicFont(),endTime,vertexBufferObjectManager);
        final Text victory = new Text(80, 50, ResourcesManager.getInstance().fontHandler.getBasicFont(), "   CONGRATULATIONS \n " +
                                                                                                        "YOU SOLVED THE MAZE!", ResourcesManager.getInstance().vertexBufferObjectManager);
        attachChild(victory);

        Text leaderBoardText = new Text(30.0f, 5.0f, ResourcesManager.getInstance().fontHandler.getBasicFont(), "Scores", ResourcesManager.getInstance().vertexBufferObjectManager);
        leaderBoardMenuItem.attachChild(leaderBoardText);
        Text newGameText = new Text(15.0f, 5.0f, ResourcesManager.getInstance().fontHandler.getBasicFont(), "New Game", ResourcesManager.getInstance().vertexBufferObjectManager);
        newGameMenuItem.attachChild(newGameText);
        Text menuText = new Text(40.0f, 5.0f, ResourcesManager.getInstance().fontHandler.getBasicFont(), "Menu", ResourcesManager.getInstance().vertexBufferObjectManager);
        menuGameMenuItem.attachChild(menuText);
        Text exitGameText = new Text(40.0f, 5.0f, ResourcesManager.getInstance().fontHandler.getBasicFont(), "Exit", ResourcesManager.getInstance().vertexBufferObjectManager);
        exitGameMenuItem.attachChild(exitGameText);

        menuChildScene.addMenuItem(myTime);
        menuChildScene.addMenuItem(leaderBoardMenuItem);
        menuChildScene.addMenuItem(newGameMenuItem);
        menuChildScene.addMenuItem(menuGameMenuItem);
        menuChildScene.addMenuItem(exitGameMenuItem);
        menuChildScene.buildAnimations();
        menuChildScene.setBackgroundEnabled(false);

        myTime.setPosition(myTime.getX(),myTime.getY()-100);
        leaderBoardMenuItem.setPosition(leaderBoardMenuItem.getX(), leaderBoardMenuItem.getY()+100);
        newGameMenuItem.setPosition(newGameMenuItem.getX(),newGameMenuItem.getY()+130);
        menuGameMenuItem.setPosition(menuGameMenuItem.getX(), menuGameMenuItem.getY()+160);
        exitGameMenuItem.setPosition(exitGameMenuItem.getX(), exitGameMenuItem.getY()+190);

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
                SceneManager.getInstance().createLocalLeaderboardScene();
                return true;
            case MENU_NEWGAME:
                SceneManager.getInstance().loadGameScene(engine);
                return true;
            case MENU_MENU:
                SceneManager.getInstance().loadMenuScene();
                return true;
            case MENU_EXITGAME:
                System.exit(0);
                return true;
            default:
                return false;
        }
    }
}
