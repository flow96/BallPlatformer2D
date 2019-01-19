package de.flo.platformer2d.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.flo.platformer2d.constants.Constants;

public class CheckPoint extends Actor {


    private Player player;
    private TextureRegion trYellow, trBlue;
    private int timer = 0;
    private int divider = 10;
    private boolean isChecked = false;

    public CheckPoint(Player player, Vector2 pos){
        this.player = player;

        trBlue = new TextureRegion(new Texture("Goals/goal_blue.png"));
        trYellow = new TextureRegion(new Texture("Goals/goal_yellow.png"));
        setSize(Constants.TILE_WIDTH / Constants.PPM, Constants.TILE_HEIGHT / Constants.PPM);
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
