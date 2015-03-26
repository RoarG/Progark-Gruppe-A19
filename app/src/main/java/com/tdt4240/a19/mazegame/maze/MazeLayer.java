package com.tdt4240.a19.mazegame.maze;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.GameState;
import com.tdt4240.a19.mazegame.scenes.GameScene;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Runar on 06.03.2015.
 */
public class MazeLayer extends Entity {

    /**
     * Contains a list of walls, both horizontal and vertical
     */

    private List<Sprite> walls;
    private List<Body> wallBodies;


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

    // private String backgroundColor, wallColor;

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
        wallBodies = new ArrayList<Body>();

        this.maze = new RecursiveBacktrackerMaze(mazeX, mazeY);
        maze.generate(seeds[(int) (Math.random() * seeds.length)]);

        /**
         * Default settings for color layout
         */
         setLayout(3);

        setupBackground(game);
        setupStartNGoal(game);
        setupWalls(game);
    }

    /**
     * Sets up the background
     * @param game
     */
    private void setupBackground(GameActivity game){
        background = new Sprite(0, 0, game.getSpriteHandler().getMazeBackground(backgroundColor), game.getVertexBufferObjectManager());
        background.setPosition(centerX - background.getWidth()/2, centerY - background.getHeight()/2);
        attachChild(background);
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

        PhysicsWorld physicsWorld = ((GameScene) GameState.getInstance().getGameScene()).getPhysicsWorld();

        // setup horizontal walls on the top and bottom (frame)
        int x = 0;
        int y = 0;
        final SpriteBatch horizontalBatch = new SpriteBatch(game.getSpriteHandler().getHoriWall(wallColor, mazeSize).getTexture(), horiWalls.length, game.getVertexBufferObjectManager());
        horizontalBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        for (boolean bol : horiWalls) {
            if (bol){
                Sprite wall = new Sprite(x * xBase, y * yBase, game.getSpriteHandler().getHoriWall(wallColor, mazeSize), game.getVertexBufferObjectManager());
                horizontalBatch.draw(wall);
                walls.add(wall);

                Body body = PhysicsFactory.createBoxBody(physicsWorld, wall, BodyDef.BodyType.StaticBody, PhysicsFactory.createFixtureDef(0, 0, 0));
                body.setUserData("horizontal");
                wallBodies.add(body);
            }
            x++;
            if (x % mazeX == 0){
                y++;
                x=0;
            }
        }
        // Draw the horizontal walls
        horizontalBatch.submit();
        background.attachChild(horizontalBatch);

        x = 0;
        y = 0;
        final SpriteBatch verticalBatch = new SpriteBatch(game.getSpriteHandler().getVertWall(wallColor, mazeSize).getTexture(), vertWalls.length, game.getVertexBufferObjectManager());
        verticalBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        for (boolean bol : vertWalls) {
            if (bol){
                Sprite wall = new Sprite(x*xBase, y*yBase, game.getSpriteHandler().getVertWall(wallColor, mazeSize), game.getVertexBufferObjectManager());
                verticalBatch.draw(wall);
                walls.add(wall);

                Body body = PhysicsFactory.createBoxBody(physicsWorld, wall, BodyDef.BodyType.StaticBody, PhysicsFactory.createFixtureDef(0, 0, 0));
                body.setUserData("vertical");
                wallBodies.add(body);
            }
            x++;
            if (x % (mazeX+1) == 0){
                y++;
                x=0;
            }
        }
        // Draw the vertical walls
        verticalBatch.submit();
        background.attachChild(verticalBatch);
    }

    /**
     * Paints the start and goal areas.
     * @param game
     */
    public void setupStartNGoal(GameActivity game){
        int xBase = (int)background.getWidth()/mazeX;     // should be 300/20 = 15

        Random rand = new Random();
        int startX = ((RecursiveBacktrackerMaze)maze).getStartX();

        int goalX = ((RecursiveBacktrackerMaze)maze).getEndX();

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

    public Sprite getMazeBackground() {
        return background;
    }

    public Maze getMaze() {
        return maze;
    }

    public int getMazeSize(){
        return mazeSize;
    }

    public List<Sprite> getWalls() {
        return walls;
    }
}
