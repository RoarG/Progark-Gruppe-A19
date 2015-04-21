package com.tdt4240.a19.mazegame.assetsHandler;

import android.graphics.Typeface;

import com.tdt4240.a19.mazegame.GameActivity;

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
        GameActivity game = ResourcesManager.getInstance().gameActivity;

        basic = FontFactory.create(game.getFontManager(), game.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 28);
    }

    public void loadFonts() {
        basic.load();
    }

    public Font getBasicFont() {
        return basic;
    }
}
