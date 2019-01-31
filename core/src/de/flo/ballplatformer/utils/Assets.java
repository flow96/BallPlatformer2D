package de.flo.ballplatformer.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {


    private static Assets instance;

    private AssetManager manager;


    private Assets(){
        manager = new AssetManager();
    }

    public static Assets getInstance(){
        if(instance == null)
            instance = new Assets();
        return instance;
    }


    public AssetManager getManager() {
        return manager;
    }

    public static void reset(){
        instance = null;
    }


    /**
     * Textures
     */
    public final String ball = "Player/Ball2.png";
    // HUD
    public final String btnBack = "Hud/Buttons/btnBack.png";
    public final String btnBackground = "Hud/Buttons/btnBackground.png";
    public final String btnBackLight = "Hud/Buttons/btnBackLight.png";
    public final String btnBox = "Hud/Buttons/btnBox.png";
    public final String btnDisabled = "Hud/Buttons/btnDisabled.png";
    public final String btnLeft = "Hud/Buttons/btnLeft.png";
    public final String btnRight = "Hud/Buttons/btnRight.png";
    public final String btnUp = "Hud/Buttons/btnUp.png";
    public final String btnPause = "Hud/Buttons/btnPause.png";
    public final String btnPlay = "Hud/Buttons/btnPlay.png";
    public final String btnPlayLight = "Hud/Buttons/btnPlayLight.png";
    // Map items
    public final String goalBlue = "Goals/goal_blue.png";
    public final String goalGreen = "Goals/goal_green.png";
    public final String goalRed = "Goals/goal_red.png";
    public final String goalYellow = "Goals/goal_yellow.png";






    public void load(){
        manager.load(ball, Texture.class);
        manager.load(btnBack, Texture.class);
        manager.load(btnBackground, Texture.class);
        manager.load(btnBackLight, Texture.class);
        manager.load(btnBox, Texture.class);
        manager.load(btnDisabled, Texture.class);
        manager.load(btnLeft, Texture.class);
        manager.load(btnRight, Texture.class);
        manager.load(btnUp, Texture.class);
        manager.load(btnPause, Texture.class);
        manager.load(btnPlay, Texture.class);
        manager.load(btnPlayLight, Texture.class);
        manager.load(goalBlue, Texture.class);
        manager.load(goalGreen, Texture.class);
        manager.load(goalRed, Texture.class);
        manager.load(goalYellow, Texture.class);
    }


    // Splashscreen
    public final String pgbForeground = "Progressbar/pgbForeground.png";
    public final String pgbBackground = "Progressbar/pgbBackground.png";
    public final String font = "Fonts/test.png";

    public void loadSplashSynchronasly(){
        manager.load(pgbForeground, Texture.class);
        manager.load(pgbBackground, Texture.class);
        manager.load(font, Texture.class);
        manager.finishLoading();
    }

    public void dispose(){
        manager.dispose();
        instance = null;
    }
}
