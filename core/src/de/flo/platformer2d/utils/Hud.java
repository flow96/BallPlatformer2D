package de.flo.platformer2d.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

public class Hud {

    public Stage stage;

    private int score;
    private int level;

    Label lblScore;
    Label lblLevel;


    public Hud(int level){
        score = 0;
        this.level = level;

        stage = new Stage(new ScalingViewport(Scaling.fill, 1200, 720));

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        Texture fontTexture = new Texture(Gdx.files.internal("Fonts/test.png"), true);
        fontTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        lblScore = new Label("SCORE: " + score, new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Fonts/test.fnt"), new TextureRegion(fontTexture)), Color.WHITE));
        lblLevel = new Label("LEVEL: " + level, new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Fonts/test.fnt"), new TextureRegion(fontTexture)), Color.WHITE));

        lblScore.setFontScale(.5f);
        lblLevel.setFontScale(.5f);


        lblScore.setColor(Color.BLACK);
        lblLevel.setColor(Color.BLACK);

        table.add(lblLevel).expandX().left().padTop(10).padLeft(30);
        table.add(lblScore).expandX().right().padTop(10).padRight(30);

        stage.addActor(table);
    }


    public void draw() {
        stage.draw();
    }

}
