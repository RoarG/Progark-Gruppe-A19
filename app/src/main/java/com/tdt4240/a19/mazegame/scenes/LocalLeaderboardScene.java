package com.tdt4240.a19.mazegame.scenes;

import android.content.Context;
import android.content.SharedPreferences;

import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.assetsHandler.ResourcesManager;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

/**
 * Created by Runar on 06.03.2015.
 */
public class LocalLeaderboardScene extends BaseScene implements MenuScene.IOnMenuItemClickListener {

    private final int MENU_NEWGAME = 1;
    private final int MENU_MENU = 2;
    private final int MENU_EXITGAME = 3;
    private final int BEST_TIME = 4;
    private final int MY_TIME = 5;
    private final int GAME_SCORE = 6;

    @Override
    public void createScene() {
        setBackground(new Background(new Color(1.0f, 1.0f, 1.0f)));
        MenuScene menuChildScene = new MenuScene(ResourcesManager.getInstance().camera);

        String hiscore = getBestTime();
        String endTime = SceneManager.getInstance().getGameScene().getEndTime();
        GameActivity game = ResourcesManager.getInstance().gameActivity;

        final IMenuItem newGameMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_NEWGAME, ResourcesManager.getInstance().spriteHandler.getButtonSprite(), ResourcesManager.getInstance().vertexBufferObjectManager),1.2f,1);
        final IMenuItem menuGameMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_MENU, ResourcesManager.getInstance().spriteHandler.getButtonSprite(), ResourcesManager.getInstance().vertexBufferObjectManager),1.2f,1);
        final IMenuItem exitGameMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_EXITGAME, ResourcesManager.getInstance().spriteHandler.getButtonSprite(), ResourcesManager.getInstance().vertexBufferObjectManager), 1.2f,1);
        final Text victory = new Text(0, 0, ResourcesManager.getInstance().fontHandler.getBasicFont(), "HISCORE!", ResourcesManager.getInstance().vertexBufferObjectManager);
        attachChild(victory);

        final IMenuItem bestTime = new TextMenuItem(BEST_TIME, ResourcesManager.getInstance().fontHandler.getBasicFont(), "Best: " + hiscore,vertexBufferObjectManager);
        final IMenuItem myTime = new TextMenuItem(MY_TIME, ResourcesManager.getInstance().fontHandler.getBasicFont(), "Curr: "  + endTime,vertexBufferObjectManager);

        final Text gameResult = new Text(100,150,ResourcesManager.getInstance().fontHandler.getBasicFont(),"Result",ResourcesManager.getInstance().vertexBufferObjectManager);
        attachChild(gameResult);

        final IMenuItem gameScore = new TextMenuItem(GAME_SCORE,ResourcesManager.getInstance().fontHandler.getBasicFont(),game.updatePeerScoresDisplay(),vertexBufferObjectManager);

        Text newGameText = new Text(20.0f, 5.0f, ResourcesManager.getInstance().fontHandler.getBasicFont(), "New Game", ResourcesManager.getInstance().vertexBufferObjectManager);
        newGameMenuItem.attachChild(newGameText);
        Text menuText = new Text(20.0f, 5.0f, ResourcesManager.getInstance().fontHandler.getBasicFont(), "Menu", ResourcesManager.getInstance().vertexBufferObjectManager);
        menuGameMenuItem.attachChild(menuText);
        Text exitGameText = new Text(20.0f, 5.0f, ResourcesManager.getInstance().fontHandler.getBasicFont(), "Exit", ResourcesManager.getInstance().vertexBufferObjectManager);
        exitGameMenuItem.attachChild(exitGameText);

        menuChildScene.addMenuItem(bestTime);
        menuChildScene.addMenuItem(myTime);
        menuChildScene.addMenuItem(gameScore);
        menuChildScene.addMenuItem(newGameMenuItem);
        menuChildScene.addMenuItem(menuGameMenuItem);
        menuChildScene.addMenuItem(exitGameMenuItem);
        menuChildScene.buildAnimations();
        menuChildScene.setBackgroundEnabled(false);

        bestTime.setPosition(myTime.getX(), myTime.getY() -250);
        myTime.setPosition(myTime.getX(), myTime.getY() -200);
        gameScore.setPosition(gameScore.getX(),gameScore.getY() - 100);
        newGameMenuItem.setPosition(newGameMenuItem.getX(),newGameMenuItem.getY()+130);
        menuGameMenuItem.setPosition(menuGameMenuItem.getX(), menuGameMenuItem.getY()+160);
        exitGameMenuItem.setPosition(exitGameMenuItem.getX(), exitGameMenuItem.getY()+190);

        menuChildScene.setOnMenuItemClickListener(this);

        setChildScene(menuChildScene);


    }

    private String getBestTime() {
        SharedPreferences prefs = ResourcesManager.getInstance().gameActivity.getSharedPreferences("hiscores", Context.MODE_PRIVATE);
        int score = prefs.getInt("seed" + SceneManager.getInstance().getGameScene().getMazeLayer().getMaze().getSeed(), 0);
        long ms = (long)score;
        long sec = ms / 1000;
        long min = sec / 60;
        ms = ms % 1000;
        sec = sec % 60;
        min = min % 60;
        return (min < 10 ? "0" + min : min) + ":" + (sec < 10 ? "0" + sec : sec) + ":" + ms;
    }

    @Override
    public void onBackKeyPressed() {
           SceneManager.getInstance().createVictoryScene();
    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_MENUSCENE;
    }

    @Override
    public void disposeScene() {

    }

    @Override
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
        switch (pMenuItem.getID()) {
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
