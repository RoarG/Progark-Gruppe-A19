package com.tdt4240.a19.mazegame.user;

import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.assetsHandler.ResourcesManager;
import com.tdt4240.a19.mazegame.assetsHandler.SpriteHandler;
import com.tdt4240.a19.mazegame.maze.Maze;
import com.tdt4240.a19.mazegame.maze.MazeLayer;
import com.tdt4240.a19.mazegame.maze.RecursiveBacktrackerMaze;
import com.tdt4240.a19.mazegame.scenes.GameScene;
import com.tdt4240.a19.mazegame.scenes.SceneManager;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
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

    public void init(MazeLayer mazeLayer, PhysicsWorld physicsWorld) {
        GameActivity game = ResourcesManager.getInstance().gameActivity;

        Sprite mazeBackground = mazeLayer.getMazeBackground();
        Maze maze = mazeLayer.getMaze();

        int xBase = (int)mazeBackground.getWidth()/maze.getWidth();
        int yBase = (int)mazeBackground.getHeight()/maze.getHeight();

        float startX = ((RecursiveBacktrackerMaze)maze).getStartX() * xBase + 4;
        float startY = (((RecursiveBacktrackerMaze)maze).getHeight() -1 ) * yBase + 4;

        user = new User(startX, startY, ResourcesManager.getInstance().spriteHandler.getUserSprite(), game.getVertexBufferObjectManager(), physicsWorld) {
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
