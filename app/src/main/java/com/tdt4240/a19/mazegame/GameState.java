package com.tdt4240.a19.mazegame;

import com.tdt4240.a19.mazegame.scenes.GameRoomScene;
import com.tdt4240.a19.mazegame.scenes.GameScene;
import com.tdt4240.a19.mazegame.scenes.WelcomeScene;
import com.tdt4240.a19.mazegame.scenes.SettingsScene;
import com.tdt4240.a19.mazegame.scenes.CountdownScene;


import org.andengine.entity.scene.Scene;

/**
 * Created by runar on 3/5/15.
 */
public class GameState {

    private static final GameState gameState = new GameState();

    /**
    private Scene welcome = new WelcomeScene();
    private Scene game = new GameScene();
    private Scene gameRoom = new GameRoomScene();
    **/

    private Scene settings = new SettingsScene();
    private Scene countdown = new CountdownScene();

    private GameActivity gameActivity;

    public GameState() {

    }

    /**
    public Scene getWelcomeScene() {
        return welcome;
    }
     **/

    public static GameState getInstance() {
        return gameState;
    }

    public void setGameActivity(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    public GameActivity getGameActivity() {
        return gameActivity;
    }

    /**
    public Scene getGameScene() {
        return game;
    }

    public Scene getGameRoomScene() {
        return gameRoom;
    }
     **/


    public Scene getSettingsScene() { return settings; }

    public Scene getCountdownScene() { return countdown; }
}
