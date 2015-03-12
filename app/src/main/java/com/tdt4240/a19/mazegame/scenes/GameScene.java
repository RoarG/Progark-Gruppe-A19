package com.tdt4240.a19.mazegame.scenes;

import android.view.MotionEvent;

import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.GameState;
import com.tdt4240.a19.mazegame.maze.MazeLayer;
import com.tdt4240.a19.mazegame.user.UserLayer;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

import java.util.Vector;

/**
 * Created by Runar on 06.03.2015.
 */
public class GameScene extends Scene {

    private UserLayer userLayer;
    private MazeLayer mazeLayer;

    private Vector<Float> pressed = new Vector<Float>(2);

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

    @Override
    public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
        switch (pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                pressed.clear();
                pressed.add(pSceneTouchEvent.getX());
                pressed.add(pSceneTouchEvent.getY());
                break;
            case TouchEvent.ACTION_UP:
                float deltaX = pSceneTouchEvent.getX() - pressed.get(0);
                float deltaY = pSceneTouchEvent.getY() - pressed.get(1);
                if (Math.abs(deltaX) > Math.abs(deltaY))
                    userLayer.getUser().setPosition(userLayer.getUser().getX() + deltaX, userLayer.getUser().getY());
                else
                    userLayer.getUser().setPosition(userLayer.getUser().getX(), userLayer.getUser().getY() + deltaY);
                break;
            case TouchEvent.ACTION_MOVE:
                //userLayer.getUser().setPosition(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
                break;
        }
        return false;
    }
}
