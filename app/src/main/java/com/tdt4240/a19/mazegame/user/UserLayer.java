package com.tdt4240.a19.mazegame.user;

import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.GameState;
import com.tdt4240.a19.mazegame.assetsHandler.SpriteHandler;
import com.tdt4240.a19.mazegame.maze.Maze;
import com.tdt4240.a19.mazegame.maze.RecursiveBacktrackerMaze;
import com.tdt4240.a19.mazegame.scenes.GameScene;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Runar on 06.03.2015.
 */
public class UserLayer extends Entity {

    private Sprite user;

    public UserLayer() {

    }

    public void init() {
        GameState gameState = GameState.getInstance();
        GameActivity game = gameState.getGameActivity();

        Sprite mazeBackground = ((GameScene)gameState.getGameScene()).getMazeLayer().getMazeBackground();
        Maze maze = ((GameScene)gameState.getGameScene()).getMazeLayer().getMaze();

        int xBase = (int)mazeBackground.getHeight()/maze.getWidth();
        int yBase = (int)mazeBackground.getHeight()/maze.getHeight();

        float startX = ((RecursiveBacktrackerMaze)maze).getStartX() * xBase + 4;
        float startY = ((RecursiveBacktrackerMaze)maze).getStartY() * yBase + 4;

        user = new User(startX, startY, game.getSpriteHandler().getUserSprite(), game.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                return true;
            }
        };

        mazeBackground.attachChild(user);
    }

    public Sprite getUser() {
        return user;
    }
}
