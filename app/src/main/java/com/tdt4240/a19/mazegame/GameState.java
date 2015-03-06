package com.tdt4240.a19.mazegame;

import com.tdt4240.a19.mazegame.scenes.WelcomeScene;

import org.andengine.entity.scene.Scene;

/**
 * Created by runar on 3/5/15.
 */
public class GameState {

    private Scene welcome = new WelcomeScene();

    public GameState() {

    }

    public Scene getWelcomeScene() {
        return welcome;
    }
}
