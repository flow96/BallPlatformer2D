package de.flo.ballplatformer.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.flo.ballplatformer.utils.Assets;

public class CheckPoint extends Actor {


    private de.flo.ballplatformer.actors.Player player;
    private TextureRegion trYellow, trBlue;
    private int timer = 0;
    private int divider = 10;
    private boolean isChecked = false;

    private Assets assets;

    public CheckPoint(de.flo.ballplatformer.actors.Player player, Vector2 pos){
        this.player = player;

        assets = Assets.getInstance();

        trBlue = new TextureRegion(assets.getManager().get(assets.goalBlue, Texture.class));
        trYellow = new TextureRegion(assets.getManager().get(assets.goalYellow, Texture.class));
        setSize(de.flo.ballplatformer.constants.Constants.TILE_WIDTH / de.flo.ballplatformer.constants.Constants.PPM, de.flo.ballplatformer.constants.Constants.TILE_HEIGHT / de.flo.ballplatformer.constants.Constants.PPM);
        setPosition(pos.x, pos.y);
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        setOrigin(width / 2, height / 2);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        timer = ++timer % divider;
    }

    public void setChecked(){
        player.startPos = new Vector2(getX() + getOriginX(), getY() + getOriginY());
        isChecked = true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if(isChecked){
            batch.draw(trBlue, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }else{
            if(timer < divider / 2)
                batch.draw(trYellow, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
            else
                batch.draw(trBlue, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }
    }
}
