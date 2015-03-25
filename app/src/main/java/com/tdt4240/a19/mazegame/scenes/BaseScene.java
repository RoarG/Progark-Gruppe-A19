package com.tdt4240.a19.mazegame.scenes;

import android.app.Activity;

import com.tdt4240.a19.mazegame.assetsHandler.ResourcesManager;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by mathi_000 on 25.03.2015.
 */
public abstract class BaseScene extends Scene{
    protected Engine engine;
    protected Activity activity;
    protected ResourcesManager resourcesManager;
    protected VertexBufferObjectManager vertexBufferObjectManager;
    protected Camera camera;

    public BaseScene(){
        this.resourcesManager = ResourcesManager.getInstance();
        this.engine = resourcesManager.engine;
        this.activity = resourcesManager.gameActivity;
        this.vertexBufferObjectManager = resourcesManager.vertexBufferObjectManager;
        this.camera = resourcesManager.camera;
        createScene();
    }
    public abstract void createScene();
    public abstract void onBackKeyPressed();
    public abstract SceneManager.SceneType getSceneType();
    public abstract void disposeScene();

}
