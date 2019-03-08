package de.flo.ballplatformer.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import de.flo.ballplatformer.constants.Constants;
import de.flo.ballplatformer.utils.Assets;


public class Coin extends Actor {

    private Animation<TextureRegion> coinAnim;
    private float stateTimer = 0, positionTimer = 0;
    private Vector2 pos;
    private World world;
    private Body body;
    private float size = 80;
    private boolean collected = false;

    public Coin(Stage stage, Vector2 pos, World world){
        this.pos = pos;
        this.world = world;

        this.positionTimer = (float)Math.random();
        this.stateTimer = (float)Math.random();
        this.stateTimer = 0;
        Assets assets = Assets.getInstance();

        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(assets.getManager().get(assets.coin_1, Texture.class)));
        frames.add(new TextureRegion(assets.getManager().get(assets.coin_1, Texture.class)));
        frames.add(new TextureRegion(assets.getManager().get(assets.coin_2, Texture.class)));
        frames.add(new TextureRegion(assets.getManager().get(assets.coin_3, Texture.class)));
        frames.add(new TextureRegion(assets.getManager().get(assets.coin_4, Texture.class)));
        frames.add(new TextureRegion(assets.getManager().get(assets.coin_5, Texture.class)));
        frames.add(new TextureRegion(assets.getManager().get(assets.coin_6, Texture.class)));
        frames.add(new TextureRegion(assets.getManager().get(assets.coin_1, Texture.class)));
        frames.add(new TextureRegion(assets.getManager().get(assets.coin_1, Texture.class)));

        coinAnim = new Animation(.11f, frames, Animation.PlayMode.LOOP_PINGPONG);

        initB2D();

        stage.addActor(this);
    }


    private void initB2D(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.KinematicBody;

        body = world.createBody(bdef);
        body.setFixedRotation(true);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(size / 2 / Constants.PPM);

        fdef.shape = shape;
        fdef.isSensor = true;
        //fdef.friction = .8f;
        //fdef.restitution = .1f;       // Bounciness
        body.createFixture(fdef).setUserData("coin");
        body.setUserData(this);

        setSize(size / Constants.PPM, size / Constants.PPM);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        setOrigin(width / 2, height / 2);
    }

    public void collected(){
        this.collected = true;
    }

    public boolean isCollected() {
        return collected;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(!collected) {
            stateTimer += delta;
            positionTimer += delta * 1.8f; // Vertical moving speed
                                            // Amplitude
            body.setLinearVelocity(0, .4f * (float) Math.cos(positionTimer));

            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);

            float width = (size / Constants.PPM) * ((float) coinAnim.getKeyFrame(stateTimer).getTexture().getWidth() / (float) coinAnim.getKeyFrame(stateTimer).getTexture().getHeight());
            setSize(width, getHeight());
        }else{
            body.setActive(false);
            remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(coinAnim.getKeyFrame(stateTimer), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

    }
}
