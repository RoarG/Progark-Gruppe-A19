package com.tdt4240.a19.mazegame.scenes;

import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.GameState;
import com.tdt4240.a19.mazegame.maze.MazeLayer;
import com.tdt4240.a19.mazegame.user.UserLayer;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.util.color.Color;

/**
 * Created by Runar on 06.03.2015.
 */
public class GameScene extends Scene {

    private UserLayer userLayer;
    private MazeLayer mazeLayer;

    public GameScene() {
        mazeLayer = new MazeLayer();
        userLayer = new UserLayer();
    }

    public void init() {
        GameActivity game = GameState.getInstance().getGameActivity();

        mazeLayer.init();
        userLayer.init();

        setBackground(new Background(new Color(0.09804f, 0.6274f, 0.8784f)));

        attachChild(mazeLayer);
        attachChild(userLayer);
    }
}
