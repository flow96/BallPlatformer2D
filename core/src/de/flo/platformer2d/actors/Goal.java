package de.flo.platformer2d.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.flo.platformer2d.constants.Constants;
import de.flo.platformer2d.utils.Assets;

public class Goal extends Actor {

    private Player player;
    private TextureRegion trFinished, trUnfinished;

    private Assets assets;

    public Goal(Player player, Vector2 pos){
        this.player = player;

        assets = Assets.getInstance();

        trFinished = new TextureRegion(assets.getManager().get(assets.goalGreen, Texture.class));
        trUnfinished = new TextureRegion(assets.getManager().get(assets.goalYellow, Texture.class));
        setSize(Constants.TILE_WIDTH / Constants.PPM, Constants.TILE_HEIGHT / Constants.PPM);
        setPosition(pos.x, pos.y);
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        setOrigin(width / 2, height / 2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if(player.levelFinished){
            batch.draw(trFinished, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }else{
            batch.draw(trUnfinished, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }
    }
}
