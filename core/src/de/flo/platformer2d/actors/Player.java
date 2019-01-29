package de.flo.platformer2d.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;


import java.util.HashMap;

import de.flo.platformer2d.PlatformerGame;
import de.flo.platformer2d.constants.Constants;
import de.flo.platformer2d.utils.Assets;
import de.flo.platformer2d.utils.MobileController;
import de.flo.platformer2d.utils.ParticleEffectController;

public class Player extends Actor {


    private TextureRegion texture;
    public Vector2 startPos;
    private World world;
    private Stage stage;
    public Body body;
    private int size = 80;
    public boolean canJump = false;
    public boolean levelFinished = false;
    private boolean isDead = false;
    private PlatformerGame gameManager;
    private MobileController controller;

    private ParticleEffectController effectController;

    private Assets assets;

    public Player(Stage stage, World world, Vector2 startPos, PlatformerGame gameManager, MobileController controller){
        this.world = world;
        this.stage = stage;
        this.startPos = startPos;
        this.gameManager = gameManager;
        this.controller = controller;

        assets = Assets.getInstance();
        Texture txt = assets.getManager().get(assets.ball);
        txt.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.texture = new TextureRegion(txt);

        effectController = ParticleEffectController.getInstance();

        initPlayerBox2D();
        addAction(Actions.scaleTo(1, 1, .9f, Interpolation.circle));
        stage.addActor(this);
    }

    private void initPlayerBox2D(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(startPos);
        bdef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bdef);
        body.setFixedRotation(true);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(size / 2 / Constants.PPM);

        fdef.shape = shape;
        //fdef.friction = .8f;
        //fdef.restitution = .1f;       // Bounciness
        body.createFixture(fdef).setUserData("player");


        // Ground contact sensor
        EdgeShape groundSensor = new EdgeShape();
        groundSensor.set(new Vector2(-size / 3f / Constants.PPM, (-(size / 2) - 1f) / Constants.PPM), new Vector2(size / 3f / Constants.PPM, (-(size / 2) - 1f) / Constants.PPM));
        fdef.shape = groundSensor;
        fdef.restitution = 0;
        fdef.isSensor = true;
        body.setUserData(this);
        body.createFixture(fdef).setUserData("groundSensor");

        // Set sprite pos
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setSize(size / Constants.PPM, size / Constants.PPM);
        setScale(0);
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        setOrigin(width / 2, height / 2);
    }

    @Override
    public void act(float delta) {
        super.act(delta);


        if(!levelFinished) {
            if(controller.isLeftPressed())
                moveLeft();
            if(controller.isRightPressed())
                moveRight();
            if(controller.isUpPressed())
                jump();

            // Check for restart
            if (body.getPosition().y < -2) {
                respawn();  // Instant respawn
            }

            // Check if die() was called from outside (collision etc.)
            if(isDead)
                respawn();
        }else{
            addAction(Actions.sequence(Actions.scaleTo(0.05f, 0.05f, .3f), Actions.run(new Runnable() {
                @Override
                public void run() {
                    clearActions();
                    gameManager.loadNextLevel();
                }
            })));
        }

        // Last actions - update sprite pos and rotation
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRotation((getRotation() + (body.getLinearVelocity().x * -4.7f)));


        if(canJump && body.getLinearVelocity().len() > 1f && !levelFinished)
            effectController.setEmit(true);
        else
            effectController.setEmit(false);
        effectController.setPos(getX() + getWidth() / 2, getY());
        effectController.update(delta);
    }


    public void moveRight() {
        if (body.getLinearVelocity().x <= 5.7f)
            body.applyLinearImpulse(new Vector2(0.2f, 0), body.getWorldCenter(), true);
    }

    public void moveLeft() {
        if (body.getLinearVelocity().x >= -5.7f)
            body.applyLinearImpulse(new Vector2(-0.2f, 0), body.getWorldCenter(), true);
    }

    public void jump(){
        if(canJump && body.getLinearVelocity().y < .4f) {
            body.applyLinearImpulse(new Vector2(0, 6.2f), body.getWorldCenter(), true);
            canJump = false;
        }
    }

    // Can be called from outside
    public void die(){
        isDead = true;
    }

    private void respawn(){
        isDead = false;
        setScale(0);
        body.setTransform(startPos, 0);
        body.setLinearVelocity(Vector2.Zero);
        body.setAngularVelocity(0);
        addAction(Actions.scaleTo(1, 1, .8f, Interpolation.circle));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        effectController.draw(batch);
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
