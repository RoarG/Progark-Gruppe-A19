package com.tdt4240.a19.mazegame.maze;

import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.GameState;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Runar on 06.03.2015.
 */
public class MazeLayer extends Entity {
    private List<Sprite> walls;

    private Maze maze;

    private long[] seeds = {123L};

    private int centerX, centerY, mazeX, mazeY;
    private Sprite background;
    // private String backgroundColor, wallColor;




    public MazeLayer() {

    }

    public void init(int pCenterX, int pCenterY, int pMazeX, int pMazeY) {
        // BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        GameActivity game = GameState.getInstance().getGameActivity();
        this.mazeX = pMazeX;
        this.mazeY = pMazeY;
        this.centerX = pCenterX;
        this.centerY = pCenterY;

        walls = new ArrayList<Sprite>();

        this.maze = new RecursiveBacktrackerMaze(seeds[(int) (Math.random()*seeds.length)], mazeX, mazeY);
        maze.generate(seeds[(int) (Math.random() * seeds.length)]);

        // this.backgroundColor = "White";
        // this.wallColor = "Black";
        setupBackground(game);
        setupWalls(game);
    }

    private void setupBackground(GameActivity game){
        background = new Sprite(centerX, centerY, game.getSpriteHandler().getMazeBackground(), game.getVertexBufferObjectManager());
        attachChild(background);
        setPosition(-(background.getWidth()/2), -(background.getHeight()/2));
    }

    private void setupWalls(GameActivity game){
        // #TODO: Implement algorithm for assigning walls positions
        boolean[] vertWalls = maze.getVertWalls();      // 11x10
        boolean[] horiWalls = maze.getHorizWalls();     // 10x11

        int yBase = (int)background.getHeight()/mazeY;    // should be 450/20 = 15
        int xBase = (int)background.getWidth()/mazeX;     // should be 300/20 = 15
        // setup horizontal walls on the top and bottom (frame)
        int x = 0;
        int y = 0;

        for (boolean bol : horiWalls){
            if (bol){
                Sprite wall = new Sprite(x*xBase, y*yBase, game.getSpriteHandler().getMazeHoriWall(), game.getVertexBufferObjectManager());
                background.attachChild(wall);
                walls.add(wall);
            }
            x++;
            if (x % mazeX == 0){
                y++;
                x=0;
            }

        }
        // setup vertical walls on the left and right (frame)
        x = 0;
        y = 0;
        for (boolean bol : vertWalls){
            if (bol){
                Sprite wall = new Sprite(x*xBase, y*yBase, game.getSpriteHandler().getMazeVertwall(), game.getVertexBufferObjectManager());
                background.attachChild(wall);
                walls.add(wall);
            }
            x++;
            if (x % (mazeX+1) == 0){
                y++;
                x=0;
            }
        }


    }
}
