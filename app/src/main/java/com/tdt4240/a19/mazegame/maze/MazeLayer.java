package com.tdt4240.a19.mazegame.maze;

import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.GameState;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;

/**
 * Created by Runar on 06.03.2015.
 */
public class MazeLayer extends Entity {

    private Sprite[] walls;
    private Sprite[] ground;

    private Maze maze;

    private long[] seeds = {123L};

    public MazeLayer() {
        this.maze = new RecursiveBacktrackerMaze(20, 20);
        maze.generate(seeds[(int) (Math.random() * seeds.length)]);
        //maze.print(System.out);
    }

    public void init() {
        GameActivity game = GameState.getInstance().getGameActivity();
    }
}
