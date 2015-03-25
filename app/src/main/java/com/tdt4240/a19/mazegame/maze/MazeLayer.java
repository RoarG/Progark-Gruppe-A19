package com.tdt4240.a19.mazegame.maze;

import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.GameState;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Runar on 06.03.2015.
 */
public class MazeLayer extends Entity {

    /**
     * Contains a list of walls, both horizontal and vertical
     */
    private List<Sprite> walls;


    private Maze maze;

    /**
     * List of seeds that are set to be used
     */
    private long[] seeds = {123L};

    /**
     * Variables for center of screen, and size of the maze in (x*y)
     */
    private int centerX, centerY, mazeX, mazeY;

    private Sprite background, start, goal;

    /**
     * Colors of the walls, set by theme (todo)
     */
    private String backgroundColor, wallColor;

    private int mazeSize;


    public MazeLayer() {

    }

    /**
     * Initialize variables, generate maze based on seed and setup the maze.
     *
     * @param pCenterX
     * @param pCenterY
     * @param pMazeX
     * @param pMazeY
     */
    public void init(int pCenterX, int pCenterY, int pMazeX, int pMazeY) {
        // BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        GameActivity game = GameState.getInstance().getGameActivity();
        this.mazeX = pMazeX;
        this.mazeY = pMazeY;
        this.centerX = pCenterX;
        this.centerY = pCenterY;
        this.mazeSize = pMazeX/10;

        walls = new ArrayList<Sprite>();

        System.out.println("mazeX: " + mazeX);
        System.out.println("mazeY: " + mazeY);
        this.maze = new RecursiveBacktrackerMaze(seeds[(int) (Math.random()*seeds.length)], mazeX, mazeY);
        maze.generate(seeds[(int) (Math.random() * seeds.length)]);

        /**
         * Default settings for color layout
         */
         setLayout(4);

        setupBackground(game);
        setupStartNGoal(game);
        setupWalls(game);
    }

    /**
     * Sets up the background
     * @param game
     */
    private void setupBackground(GameActivity game){
        background = new Sprite(centerX, centerY, game.getSpriteHandler().getMazeBackground(backgroundColor), game.getVertexBufferObjectManager());
        attachChild(background);
        setPosition(-(background.getWidth()/2), -(background.getHeight()/2));
    }

    /**
     * Sets up the walls
     * @param game
     */
    private void setupWalls(GameActivity game){
        boolean[] vertWalls = maze.getVertWalls();      // 11x10
        boolean[] horiWalls = maze.getHorizWalls();     // 10x11

        // Based on size of background and maze it sets the xBase and yBase
        int yBase = (int)background.getHeight()/mazeY;    // should be 450/20 = 15
        int xBase = (int)background.getWidth()/mazeX;     // should be 300/20 = 15

        // Draw the horizontal walls
        int x = 0;
        int y = 0;
        System.out.println("Wallcolor: " + wallColor);
        System.out.println("MazeSize: " + mazeSize);
        for (boolean bol : horiWalls){
            if (bol){
                Sprite wall = new Sprite(x*xBase, y*yBase, game.getSpriteHandler().getHoriWall(wallColor, mazeSize), game.getVertexBufferObjectManager());
                background.attachChild(wall);
                walls.add(wall);
            }
            x++;
            if (x % mazeX == 0){
                y++;
                x=0;
            }

        }
        // Draw the vertical walls
        x = 0;
        y = 0;
        for (boolean bol : vertWalls){
            if (bol){
                Sprite wall = new Sprite(x*xBase, y*yBase, game.getSpriteHandler().getVertWall(wallColor, mazeSize), game.getVertexBufferObjectManager());
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

    /**
     * Paints the start and goal areas.
     * @param game
     */
    public void setupStartNGoal(GameActivity game){
        int xBase = (int)background.getWidth()/mazeX;     // should be 300/20 = 15

        Random rand = new Random();
        int startX = rand.nextInt(mazeX);
        int goalX = rand.nextInt(mazeX);

        start = new Sprite(startX*xBase, background.getHeight()-(background.getHeight()/mazeY), game.getSpriteHandler().getStart(mazeSize), game.getVertexBufferObjectManager());
        goal = new Sprite(goalX*xBase, 0, game.getSpriteHandler().getGoal(mazeSize), game.getVertexBufferObjectManager());

        background.attachChild(start);
        background.attachChild(goal);
    }

    /**
     * @param layout
     * Layoutsetup: Background & Wall
     *
     * Layout 0: Black & White
     * Layout 1: White & Black
     * Layout 2: Black & Turquoise
     * Layout 3: Black & Red
     * Layout 4: Turquoise & Black
     */
    public void setLayout(int layout){
        switch (layout){
            case 0:
                backgroundColor = "Black";
                wallColor = "White";
                break;
            case 1:
                backgroundColor = "White";
                wallColor = "Black";
                break;
            case 2:
                backgroundColor = "Black";
                wallColor = "Turquoise";
                break;
            case 3:
                backgroundColor = "Black";
                wallColor = "Red";
                break;
            case 4:
                backgroundColor = "Turquoise";
                wallColor = "Black";
                break;
        }
    }

    /**
     * Sets the scale of the entire maze, takes a float parameter <0, 0.99>
     * @param  scale
     */
    public void setScale(float scale){
        background.setScale(scale);
    }
}
