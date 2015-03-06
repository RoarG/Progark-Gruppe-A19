package com.tdt4240.a19.mazegame.maze;

import org.andengine.entity.sprite.Sprite;

/**
 * Created by Runar on 06.03.2015.
 */
public class MazeLayer {

    private Sprite[] walls;
    private Sprite[] ground;
    private Sprite end;
    private Sprite start;

    private Maze maze;

    public MazeLayer() {
        this.maze = new RecursiveBacktrackerMaze(20, 20);
        maze.generate(123L);
        //maze.print(System.out);
    }
}
