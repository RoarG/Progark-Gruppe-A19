package com.tdt4240.a19.mazegame.scenes;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.assetsHandler.ResourcesManager;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

/**
 * Created by Znf on 26.03.2015.
 */
public class CountdownScene extends BaseScene {

    private void setupCountdownTimer() {
        final Text gameIsStarting = new Text(0, 0, ResourcesManager.getInstance().fontHandler.getBasicFont(), "Game Is Starting In:", "Game Is Starting In: XX".length(), ResourcesManager.getInstance().vertexBufferObjectManager);
        attachChild(gameIsStarting);
        final CountDownTimer timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                gameIsStarting.setText("Game Is Starting In: " + millisUntilFinished / 1000);
            }
            @Override
            public void onFinish() {
                //GameState.getInstance().getGameScene();
            }
        }.start();
        attachChild(gameIsStarting);
    }

    @Override
    public void createScene() {
        GameActivity game = ResourcesManager.getInstance().gameActivity;
        setBackground(new Background(new Color(0.09804f, 0.6274f, 0.8784f)));

        setupCountdownTimer();
    }

    @Override
    public void onBackKeyPressed() {

    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return null;
    }

    @Override
    public void disposeScene() {

    }
}
