package de.flo.platformer2d.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.flo.platformer2d.PlatformerGame;

public class StartScreen implements Screen {


    private SpriteBatch batch;
    private OrthographicCamera cam;
    private Stage stage;
    private Viewport viewport;
    private Texture fontTexture;
    private BitmapFont font;

    private PlatformerGame game;

    private Label lblTitle;


    public StartScreen(SpriteBatch batch, PlatformerGame game){
        this.batch = batch;
        this.game = game;

        cam = new OrthographicCamera();
        viewport = new FitViewport(1200, 720, cam);
        stage = new Stage(viewport, batch);

        fontTexture = new Texture(Gdx.files.internal("Fonts/test.png"), true);
        fontTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font = new BitmapFont(Gdx.files.internal("Fonts/test.fnt"), new TextureRegion(fontTexture));

        initMenu();

    }

    private void initMenu(){
        lblTitle = new Label("BALL PLATFORMER 2D", new Label.LabelStyle(font, Color.WHITE));
        lblTitle.setAlignment(Align.center);
        lblTitle.setFontScale(1.2f);
        lblTitle.setColor(Color.BLACK);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("Hud/Buttons/btnBackground.png")));
        textButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture("Hud/Buttons/btnBackground.png")));
        textButtonStyle.checked = new TextureRegionDrawable(new TextureRegion(new Texture("Hud/Buttons/btnBackground.png")));
        textButtonStyle.fontColor = Color.BLACK;
        textButtonStyle.downFontColor = Color.LIGHT_GRAY;
        TextButton btnStart = new TextButton("Start", textButtonStyle);
        btnStart.pad(5);
        btnStart.getLabel().setFontScale(.7f);
        btnStart.setSize(170, 80);

        btnStart.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.showLevelSelectScreen();
            }
        });


        Table table = new Table();
        table.setFillParent(true);
        table.top();

        table.add(lblTitle).center().fill().padTop(20);
        table.row();
        table.add(btnStart).expandY().center().size(btnStart.getWidth(), btnStart.getHeight());
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
