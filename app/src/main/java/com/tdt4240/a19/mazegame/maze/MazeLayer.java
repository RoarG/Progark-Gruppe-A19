package com.tdt4240.a19.mazegame.maze;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.GameState;
import com.tdt4240.a19.mazegame.scenes.GameScene;
import com.tdt4240.a19.mazegame.scenes.SceneManager;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Runar on 06.03.2015.
 */
public class MazeLayer extends Entity {

    private List<Sprite> walls;
    private List<Body> wallBodies;

    private Maze maze;

    private long[] seeds = {123L};

    private int centerX, centerY, mazeX, mazeY;

    private Sprite background;
    // private String backgroundColor, wallColor;

    public MazeLayer() {

    }

    public void init(int pCenterX, int pCenterY, int pMazeX, int pMazeY, PhysicsWorld world) {
        // BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        GameActivity game = GameState.getInstance().getGameActivity();
        this.mazeX = pMazeX;
        this.mazeY = pMazeY;
        this.centerX = pCenterX;
        this.centerY = pCenterY;

        walls = new ArrayList<Sprite>();
        wallBodies = new ArrayList<Body>();

        this.maze = new RecursiveBacktrackerMaze(mazeX, mazeY);
        maze.generate(seeds[(int) (Math.random() * seeds.length)]);

        // this.backgroundColor = "White";
        // this.wallColor = "Black";
        setupBackground(game);
        setupWalls(game, world);
    }

    private void setupBackground(GameActivity game){
        background = new Sprite(0, 0, game.getSpriteHandler().getMazeBackground(), game.getVertexBufferObjectManager());
        background.setPosition(centerX - background.getWidth()/2, centerY - background.getHeight()/2);
        attachChild(background);
    }

    private void setupWalls(GameActivity game, PhysicsWorld physicsWorld){
        // #TODO: Implement algorithm for assigning walls positions
        boolean[] vertWalls = maze.getVertWalls();      // 11x10
        boolean[] horiWalls = maze.getHorizWalls();     // 10x11

        int yBase = (int)background.getHeight()/mazeY;    // should be 450/20 = 15
        int xBase = (int)background.getWidth()/mazeX;     // should be 300/20 = 15


        // setup horizontal walls on the top and bottom (frame)
        int x = 0;
        int y = 0;
        final SpriteBatch horizontalBatch = new SpriteBatch(game.getSpriteHandler().getMazeHoriWall().getTexture(), horiWalls.length, game.getVertexBufferObjectManager());
        horizontalBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        for (boolean bol : horiWalls) {
            if (bol){
                Sprite wall = new Sprite(x * xBase, y * yBase, game.getSpriteHandler().getMazeHoriWall(), game.getVertexBufferObjectManager());
                horizontalBatch.draw(wall);
                walls.add(wall);

                Body body = PhysicsFactory.createBoxBody(physicsWorld, wall, BodyDef.BodyType.StaticBody, PhysicsFactory.createFixtureDef(0, 0, 0));
                body.setUserData("horizontalWalls");
                wallBodies.add(body);

                /*physicsWorld.registerPhysicsConnector(new PhysicsConnector(wall, body, false, false) {
                    @Override
                    public void onUpdate(float pSecondsElapsed) {
                        //super.onUpdate(pSecondsElapsed);
                    }
                });*/
            }
            x++;
            if (x % mazeX == 0){
                y++;
                x=0;
            }
        }
        horizontalBatch.submit();
        background.attachChild(horizontalBatch);

        x = 0;
        y = 0;
        final SpriteBatch verticalBatch = new SpriteBatch(game.getSpriteHandler().getMazeVertwall().getTexture(), vertWalls.length, game.getVertexBufferObjectManager());
        verticalBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        for (boolean bol : vertWalls) {
            if (bol){
                Sprite wall = new Sprite(x * xBase, y * yBase, game.getSpriteHandler().getMazeVertwall(), game.getVertexBufferObjectManager());
                verticalBatch.draw(wall);
                walls.add(wall);

                Body body = PhysicsFactory.createBoxBody(physicsWorld, wall, BodyDef.BodyType.StaticBody, PhysicsFactory.createFixtureDef(0, 0, 0));
                body.setUserData("verticalWalls");
                wallBodies.add(body);

                /*physicsWorld.registerPhysicsConnector(new PhysicsConnector(wall, body, false, false) {
                    @Override
                    public void onUpdate(float pSecondsElapsed) {
                        //super.onUpdate(pSecondsElapsed);
                    }
                });*/
            }
            x++;
            if (x % (mazeX+1) == 0){
                y++;
                x=0;
            }
        }
        verticalBatch.submit();
        background.attachChild(verticalBatch);
    }

    public Sprite getMazeBackground() {
        return background;
    }

    public Maze getMaze() {
        return maze;
    }

    public List<Sprite> getWalls() {
        return walls;
    }
}
