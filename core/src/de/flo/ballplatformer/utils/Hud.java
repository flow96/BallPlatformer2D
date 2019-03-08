package de.flo.ballplatformer.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud {

    private Stage stage;

    private de.flo.ballplatformer.utils.Assets assets;

    private int score;
    private int maxScore;
    private int level;
    private OrthographicCamera cam;
    private Viewport viewport;
    private de.flo.ballplatformer.screens.PlayScreen playScreen;
    private TextureRegionDrawable pause, play, playBig, backBig;
    private Image imgPause, imgPauseBig, imgBackBig;
    private Texture fontTexture;

    private Table pauseWindow;

    Label lblScore;
    Label lblLevel;
    Label lblPaused;



    public Hud(int level, SpriteBatch batch, final de.flo.ballplatformer.screens.PlayScreen playScreen){
        score = 0;
        this.maxScore = playScreen.getLevelMaxScore();
        this.level = level;
        this.playScreen = playScreen;

        assets = de.flo.ballplatformer.utils.Assets.getInstance();

        cam = new OrthographicCamera();

        viewport = new FitViewport(1280, 1280.0f / ((float)Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight()), cam);
        stage = new Stage(viewport, batch);
        fontTexture = assets.getManager().get(assets.font);
        fontTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        initHUD();
        initPauseWindow();

    }

    private void initHUD(){
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        lblScore = new Label("SCORE: " + score + "/" + maxScore, new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Fonts/test.fnt"), new TextureRegion(fontTexture)), Color.WHITE));
        lblLevel = new Label("LEVEL: " + level, new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Fonts/test.fnt"), new TextureRegion(fontTexture)), Color.WHITE));

        pause = new TextureRegionDrawable(new TextureRegion(assets.getManager().get(assets.btnPause, Texture.class)));
        play = new TextureRegionDrawable(new TextureRegion(assets.getManager().get(assets.btnPlay, Texture.class)));

        imgPause = new Image(pause);
        imgPause.setSize(55, 55);


        imgPause.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playScreen.paused = !playScreen.paused;
                if(playScreen.paused){
                    setPaused();
                }else{
                    setUnpaused();
                }
            }
        });

        lblScore.setFontScale(.65f);
        lblLevel.setFontScale(.65f);

        lblScore.setColor(Color.BLACK);
        lblLevel.setColor(Color.BLACK);

        table.add(lblLevel).expandX().left().padTop(10).padLeft(30);
        table.add(lblScore).expandX().right().padTop(10).padRight(30);
        table.add(imgPause).padRight(30).padTop(10).padLeft(20).size(imgPause.getWidth(), imgPause.getHeight());

        // table.setDebug(true);
        stage.addActor(table);
    }

    private void initPauseWindow(){

        Pixmap bgPixmap = new Pixmap(1,1, Pixmap.Format.RGBA4444);
        bgPixmap.setColor(0, 0, 0, .7f);
        bgPixmap.fill();
        TextureRegionDrawable textureRegionDrawableBg = new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));

        lblPaused = new Label("GAME PAUSED", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Fonts/test.fnt"), new TextureRegion(fontTexture)), Color.WHITE));
        lblPaused.setAlignment(Align.center);
        lblPaused.setFontScale(.8f);
        lblPaused.setColor(Color.WHITE);

        playBig = new TextureRegionDrawable(new TextureRegion(assets.getManager().get(assets.btnPlayLight, Texture.class)));
        backBig = new TextureRegionDrawable(new TextureRegion(assets.getManager().get(assets.btnBackLight, Texture.class)));

        imgPauseBig = new Image(playBig);
        imgPauseBig.setSize(60, 60);
        imgBackBig = new Image(backBig);
        imgBackBig.setSize(60, 60);

        imgPauseBig.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playScreen.paused = false;
                setUnpaused();
            }
        });

        imgBackBig.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playScreen.getGameManager().showLevelSelectScreen();
            }
        });

        pauseWindow = new Table();
        pauseWindow.setSize(viewport.getWorldWidth(), 200);
        pauseWindow.setPosition(0, viewport.getWorldHeight() / 2 - 100);
        pauseWindow.setBackground(textureRegionDrawableBg);

        pauseWindow.add(lblPaused).center().padBottom(20).colspan(2);
        pauseWindow.row();
        pauseWindow.add(imgBackBig).center().size(imgBackBig.getWidth(), imgBackBig.getHeight()).pad(10);
        pauseWindow.add(imgPauseBig).center().size(imgPauseBig.getWidth(), imgPauseBig.getHeight()).pad(10);
        pauseWindow.setVisible(false);
        stage.addActor(pauseWindow);
    }


    public void draw() {
        stage.act();
        stage.getViewport().apply();

        stage.draw();
    }

    public void increaseScore(){
        this.score++;
        lblScore.setText("SCORE: " + score + "/" + maxScore);
    }

    public void setPaused(){
        imgPause.setDrawable(play);
        pauseWindow.setSize(0, 0);
        lblPaused.setColor(1, 1, 1, .05f);
        imgPauseBig.setColor(1, 1, 1, .05f);
        imgBackBig.setColor(1, 1, 1, .05f);
        float delay = 0.018f;
        float delay2 = .01f;

        for (int i = 0; i < 10; i++) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    pauseWindow.setSize(pauseWindow.getWidth() + (viewport.getWorldWidth() / 10), 10);
                    pauseWindow.setPosition(viewport.getWorldWidth() / 2 - pauseWindow.getWidth() / 2, viewport.getWorldHeight() / 2 - pauseWindow.getHeight() / 2);
                }
            }, delay * i);
        }

        for (int i = 0; i < 19; i++) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    pauseWindow.setSize(pauseWindow.getWidth(), pauseWindow.getHeight() + 10);
                    pauseWindow.setPosition(0, viewport.getWorldHeight() / 2 - pauseWindow.getHeight() / 2);
                    lblPaused.setColor(1, 1, 1, lblPaused.getColor().a + .05f);
                    imgPauseBig.setColor(1, 1, 1, imgPauseBig.getColor().a + .05f);
                    imgBackBig.setColor(1, 1, 1, imgBackBig.getColor().a + .05f);
                }
            }, delay2 * i + (delay * 11));
        }
        pauseWindow.setVisible(true);
    }

    public void setUnpaused(){
        imgPause.setDrawable(pause);
        pauseWindow.setVisible(false);
    }

    public void resize(int width, int height){
        viewport.update(width, height);
        viewport.apply();
    }

    public Stage getStage() {
        return stage;
    }

}
