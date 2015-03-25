package com.tdt4240.a19.mazegame.scenes;

import com.tdt4240.a19.mazegame.GameState;
import com.tdt4240.a19.mazegame.assetsHandler.ResourcesManager;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.IGameInterface;

/**
 * Created by mathi_000 on 25.03.2015.
 */
public class SceneManager {
    private WelcomeScene welcomeScene;
    private GameScene gameScene;
    private GameRoomScene gameRoomScene;
    private BaseScene menuScene;
    private BaseScene splashScene;

    private static final SceneManager INSTANCE = new SceneManager();
    private SceneType currentSceneType = SceneType.SCENE_MENUSCENE;
    private BaseScene currentScene;

    private Engine engine = GameState.getInstance().getGameActivity().getEngine();

    public enum SceneType{
        SCENE_WELCOMESCENE,
        SCENE_GAMESCENE,
        SCENE_GAMEROOMSCENE,
        SCENE_MENUSCENE,
        SCENE_SPLASHSCENE
    }

    public void setScene(BaseScene scene) {
        engine.setScene(scene);
        currentScene = scene;
        currentSceneType = scene.getSceneType();
    }

    public void setScene(SceneType sceneType){
        switch (sceneType){
           /*case SCENE_WELCOMESCENE:
                setScene(welcomeScene);
                break;
            */case SCENE_GAMESCENE:
                setScene(gameScene);
                break;
            /*case SCENE_GAMEROOMSCENE:
                setScene(gameRoomScene);
                break;
                */
            case SCENE_MENUSCENE:
                setScene(menuScene);
                break;
            case SCENE_SPLASHSCENE:
                setScene(splashScene);
                break;
            default:
                break;
        }
    }
    public static SceneManager getInstance()
    {
        return INSTANCE;
    }

    public SceneType getCurrentSceneType()
    {
        return currentSceneType;
    }

    public BaseScene getCurrentScene()
    {
        return currentScene;
    }
    public void createSplashScene(IGameInterface.OnCreateSceneCallback pOnCreateSceneCallback)
    {
        ResourcesManager.getInstance().loadSplashScreen();
        splashScene = new SplashScene();
        currentScene = splashScene;
        pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
    }
    private void disposeSplashScene()
    {
        ResourcesManager.getInstance().unloadSplashScreen();
        splashScene.disposeScene();
        splashScene = null;
    }
    public void createMenuScene()
    {
        ResourcesManager.getInstance().loadMenuResources();
        menuScene = new GameMenuScene();
        setScene(menuScene);
        disposeSplashScene();
    }
    public void loadGameScene(final Engine mEngine)
    {
        mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback()
        {
            public void onTimePassed(final TimerHandler pTimerHandler)
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadGameResources();
                gameScene = new GameScene();
                setScene(gameScene);
            }
        }));
    }

}
