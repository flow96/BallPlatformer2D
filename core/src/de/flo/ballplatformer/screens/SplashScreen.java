package de.flo.ballplatformer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SplashScreen implements Screen {


    private de.flo.ballplatformer.utils.Assets assets;


    private SpriteBatch batch;
    private OrthographicCamera cam;
    private Stage stage;
    private Viewport viewport;
    private Texture fontTexture, pgbFore, pgbBack;
    private BitmapFont font;
    private Screen nextScreen;

    float x, y;

    private de.flo.ballplatformer.PlatformerGame game;

    private Label lblTitle;
    float progress = 0;
    float alpha = 1f;


    public SplashScreen(SpriteBatch batch, de.flo.ballplatformer.PlatformerGame game, Screen nextScreen){
        this.batch = batch;
        this.game = game;
        this.nextScreen = nextScreen;

        cam = new OrthographicCamera();
        viewport = new FitViewport(1200, 720, cam);
        stage = new Stage(viewport, batch);
        assets = de.flo.ballplatformer.utils.Assets.getInstance();

        assets.loadSplashSynchronasly();

        pgbFore = assets.getManager().get(assets.pgbForeground);
        pgbBack = assets.getManager().get(assets.pgbBackground);

        fontTexture = assets.getManager().get(assets.font);
        fontTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font = new BitmapFont(Gdx.files.internal("Fonts/test.fnt"), new TextureRegion(fontTexture));


        lblTitle = new Label("Loading", new Label.LabelStyle(font, Color.BLACK));
        lblTitle.setAlignment(Align.center);
        lblTitle.setFontScale(.7f);
        lblTitle.setColor(Color.BLACK);

        stage.addActor(lblTitle);


        assets.load();
    }


    @Override
    public void show() {

    }

    private void update(){
        stage.act();
        progress = assets.getManager().getProgress();
        if(assets.getManager().update()){
            if(nextScreen == null) {
                stage.addAction(Actions.sequence(Actions.color(Color.BLACK, .22f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.showStartScreen(true);
                    }
                })));
            }else{
                game.setScreen(nextScreen);
            }
        }
    }


    @Override
    public void render(float delta) {
        update();
        Color color = stage.getRoot().getColor();
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
        batch.setColor(color);
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        //lblTitle.draw(batch, 1);
        batch.draw(pgbBack, x, y, 160, 20);
        batch.draw(pgbFore, x + 2f, y + 2f, progress * 160 - 4, 16);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        x = viewport.getWorldWidth() / 2 - 80;
        y = viewport.getWorldHeight() / 2 - 10;
        lblTitle.setPosition(viewport.getWorldWidth() / 2 - lblTitle.getWidth() / 2, y + lblTitle.getHeight() / 2);
        lblTitle.setAlignment(Align.center);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
