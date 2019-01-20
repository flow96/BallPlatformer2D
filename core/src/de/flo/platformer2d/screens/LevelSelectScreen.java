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

public class LevelSelectScreen implements Screen {


    private SpriteBatch batch;
    private OrthographicCamera cam;
    private Stage stage;
    private Viewport viewport;
    private Texture fontTexture;
    private BitmapFont font;

    private PlatformerGame game;

    private Label lblTitle;



    public LevelSelectScreen(SpriteBatch batch, PlatformerGame game){
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
        lblTitle = new Label("SELECT A LEVEL", new Label.LabelStyle(font, Color.WHITE));
        lblTitle.setAlignment(Align.center);
        lblTitle.setFontScale(1.2f);
        lblTitle.setColor(Color.BLACK);

        Table table = new Table();
        table.setFillParent(true);
        table.top();


        TextButton.TextButtonStyle backStyle = new TextButton.TextButtonStyle();
        backStyle.font = font;
        backStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("Hud/Buttons/btnBack.png")));
        backStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture("Hud/Buttons/btnBack.png")));
        backStyle.checked = new TextureRegionDrawable(new TextureRegion(new Texture("Hud/Buttons/btnBack.png")));
        backStyle.fontColor = Color.BLACK;
        backStyle.downFontColor = Color.LIGHT_GRAY;


        TextButton btnBack = new TextButton("", backStyle);
        btnBack.pad(10);
        btnBack.getLabel().setFontScale(.7f);
        btnBack.setSize(85, 85);

        btnBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new StartScreen(batch, game));
            }
        });

        table.add(btnBack).left().size(btnBack.getWidth(), btnBack.getHeight()).pad(20).padBottom(50);

        table.add(lblTitle).center().fill().pad(20).padBottom(35).colspan(8);
        table.row();




        TextButton.TextButtonStyle txtBtnDisabledStyle = new TextButton.TextButtonStyle();
        txtBtnDisabledStyle.font = font;
        txtBtnDisabledStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("Hud/Buttons/btnDisabled.png")));
        txtBtnDisabledStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture("Hud/Buttons/btnDisabled.png")));
        txtBtnDisabledStyle.checked = new TextureRegionDrawable(new TextureRegion(new Texture("Hud/Buttons/btnDisabled.png")));
        txtBtnDisabledStyle.disabled = new TextureRegionDrawable(new TextureRegion(new Texture("Hud/Buttons/btnDisabled.png")));
        txtBtnDisabledStyle.disabledFontColor = new Color(0, 0, 0, 0);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("Hud/Buttons/btnBox.png")));
        textButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture("Hud/Buttons/btnBox.png")));
        textButtonStyle.checked = new TextureRegionDrawable(new TextureRegion(new Texture("Hud/Buttons/btnBox.png")));
        textButtonStyle.disabledFontColor = Color.LIGHT_GRAY;
        textButtonStyle.fontColor = Color.BLACK;
        textButtonStyle.downFontColor = Color.LIGHT_GRAY;

        int levelName = 1;
        A: for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 10; j++) {
                final int nr = levelName;
                TextButton btnStart;
                if(nr > game.maxLevel) {
                    btnStart = new TextButton("", txtBtnDisabledStyle);
                    btnStart.pad(10);
                    btnStart.getLabel().setFontScale(.7f);
                    btnStart.setSize(85, 85);
                    btnStart.setDisabled(true);
                }else {
                    btnStart = new TextButton("" + nr, textButtonStyle);
                    btnStart.pad(10);
                    btnStart.getLabel().setFontScale(.7f);
                    btnStart.setSize(85, 85);
                    if(levelName > 100) // TODO: Load last mastered level index and disable all other levels!
                        btnStart.setDisabled(true);
                    else {
                        btnStart.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                game.loadLevel(nr);
                            }
                        });
                    }
                }



                table.add(btnStart).size(btnStart.getWidth(), btnStart.getHeight()).pad(10);
                levelName++;
            }
            table.row();
        }

        table.row();

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
