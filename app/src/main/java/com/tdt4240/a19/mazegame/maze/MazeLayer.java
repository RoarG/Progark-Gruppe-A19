package com.tdt4240.a19.mazegame.maze;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.assetsHandler.ResourcesManager;
import com.tdt4240.a19.mazegame.scenes.GameScene;
import com.tdt4240.a19.mazegame.scenes.SceneManager;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.region.ITextureRegion;

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

    public void init(int pCenterX, int pCenterY, int pMazeX, int pMazeY, PhysicsWorld world) {
        // BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
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
         setLayout(4);

        setupBackground();
        setupStartNGoal(world);
        setupWalls(world);

    }

    /**
     * Generates and places the background
     */
    private void setupBackground(){
        background = new Sprite(0, 0, ResourcesManager.getInstance().spriteHandler.getMazeBackground(backgroundColor), ResourcesManager.getInstance().vertexBufferObjectManager);
        background.setPosition(centerX - background.getWidth()/2, centerY - background.getHeight()/2);
        attachChild(background);
    }

    private void setupWalls(PhysicsWorld physicsWorld){
        boolean[] vertWalls = maze.getVertWalls();      // 11x10
        boolean[] horiWalls = maze.getHorizWalls();     // 10x11

        // Based on size of background and maze it sets the xBase and yBase
        int yBase = (int)background.getHeight()/mazeY;    // should be 450/20 = 15
        int xBase = (int)background.getWidth()/mazeX;     // should be 300/20 = 15

        // setup horizontal walls on the top and bottom (frame)
        int x = 0;
        int y = 0;
        final SpriteBatch horizontalBatch = new SpriteBatch(ResourcesManager.getInstance().spriteHandler.getHoriWall(wallColor, mazeSize).getTexture(), horiWalls.length, ResourcesManager.getInstance().vertexBufferObjectManager);
        horizontalBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        for (boolean bol : horiWalls) {
            if (bol){
                Sprite wall = new Sprite(x * xBase, y * yBase, ResourcesManager.getInstance().spriteHandler.getHoriWall(wallColor, mazeSize), ResourcesManager.getInstance().vertexBufferObjectManager);
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
        // game.getSpriteHandler().getVertWall(wallColor, mazeSize).getTexture();
        final SpriteBatch verticalBatch = new SpriteBatch(ResourcesManager.getInstance().spriteHandler.getVertWall(wallColor, mazeSize).getTexture(), vertWalls.length, ResourcesManager.getInstance().vertexBufferObjectManager);
        verticalBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        for (boolean bol : vertWalls) {
            if (bol){
                Sprite wall = new Sprite(x*xBase, y*yBase, ResourcesManager.getInstance().spriteHandler.getVertWall(wallColor, mazeSize), ResourcesManager.getInstance().vertexBufferObjectManager);
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
     */
    public void setupStartNGoal(PhysicsWorld physicsWorld){
        int xBase = (int)background.getWidth()/mazeX;     // should be 300/20 = 15

        Random rand = new Random();
        int startX = ((RecursiveBacktrackerMaze)maze).getStartX();

        int goalX = ((RecursiveBacktrackerMaze)maze).getEndX();

        start = new Sprite(startX*xBase, background.getHeight()-(background.getHeight()/mazeY), ResourcesManager.getInstance().spriteHandler.getStart(mazeSize), ResourcesManager.getInstance().vertexBufferObjectManager);
        goal = new Sprite(goalX*xBase, 0, ResourcesManager.getInstance().spriteHandler.getGoal(mazeSize), ResourcesManager.getInstance().vertexBufferObjectManager);

        Body body = PhysicsFactory.createBoxBody(physicsWorld, goal, BodyDef.BodyType.StaticBody, PhysicsFactory.createFixtureDef(0, 0, 0));
        body.setUserData("goal");
        wallBodies.add(body);

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
