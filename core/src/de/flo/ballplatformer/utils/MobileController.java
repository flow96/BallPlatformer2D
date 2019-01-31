package de.flo.ballplatformer.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class MobileController {
    Viewport viewport;
    Stage stage;
    boolean upPressed, downPressed, leftPressed, rightPressed;
    OrthographicCamera cam;

    private int btnSize = 140;
    private de.flo.ballplatformer.utils.Assets assets;

    public MobileController(SpriteBatch batch){
        assets = de.flo.ballplatformer.utils.Assets.getInstance();
        cam = new OrthographicCamera();
        viewport = new FitViewport(1280, 1280 / ((float) Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight()), cam);
        stage = new Stage(viewport, batch);

        stage.addListener(new InputListener(){

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch(keycode){
                    case Input.Keys.W:
                    case Input.Keys.UP:
                        upPressed = true;
                        break;
                    case Input.Keys.S:
                    case Input.Keys.DOWN:
                        downPressed = true;
                        break;
                        case Input.Keys.A:
                    case Input.Keys.LEFT:
                        leftPressed = true;
                        break;
                    case Input.Keys.D:
                    case Input.Keys.RIGHT:
                        rightPressed = true;
                        break;
                }
                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                switch(keycode){
                    case Input.Keys.W:
                    case Input.Keys.UP:
                        upPressed = false;
                        break;
                    case Input.Keys.S:
                    case Input.Keys.DOWN:
                        downPressed = false;
                        break;
                    case Input.Keys.A:
                    case Input.Keys.LEFT:
                        leftPressed = false;
                        break;
                    case Input.Keys.D:
                    case Input.Keys.RIGHT:
                        rightPressed = false;
                        break;
                }
                return true;
            }
        });
        Table table = new Table();
        table.setFillParent(true);

        table.left().bottom();

        Image upImg = new Image(assets.getManager().get(assets.btnUp, Texture.class));
        upImg.setSize(btnSize, btnSize);
        upImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });


        Image rightImg = new Image(assets.getManager().get(assets.btnRight, Texture.class));
        rightImg.setSize(btnSize, btnSize);
        rightImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });

        Image leftImg = new Image(assets.getManager().get(assets.btnLeft, Texture.class));
        leftImg.setSize(btnSize, btnSize);
        leftImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });

        table.setDebug(false);

        table.row().pad(5, 40, 10, 40);
        table.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight());
        table.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight());
        table.add(upImg).expandX().right().size(upImg.getWidth(), upImg.getHeight());

        stage.addActor(table);
    }

    public void draw(){
        stage.draw();
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }

    public Stage getStage() {
        return stage;
    }
}
