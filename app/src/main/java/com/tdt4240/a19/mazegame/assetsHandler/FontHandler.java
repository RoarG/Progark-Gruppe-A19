package com.tdt4240.a19.mazegame.assetsHandler;

import android.graphics.Typeface;

import com.tdt4240.a19.mazegame.GameActivity;
import com.tdt4240.a19.mazegame.GameState;

import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;

/**
 * Created by runar on 3/5/15.
 */
public class FontHandler {

    private Font basic;

    public FontHandler() {

    }

    public void createFonts() {
        GameActivity game = GameState.getInstance().getGameActivity();

        basic = FontFactory.create(game.getFontManager(), game.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
    }

    public void loadFonts() {
        basic.load();
    }

    public Font getBasicFont() {
        return basic;
    }
}
