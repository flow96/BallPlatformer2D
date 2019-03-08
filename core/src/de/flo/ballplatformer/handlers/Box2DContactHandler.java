package de.flo.ballplatformer.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.flo.ballplatformer.actors.Coin;
import de.flo.ballplatformer.actors.Player;
import de.flo.ballplatformer.utils.Hud;
import de.flo.ballplatformer.utils.ParticleEffectController;

public class Box2DContactHandler implements ContactListener {

    private Player player;
    private Hud hud;
    private int groundContactCounter = 0;
    private ParticleEffectController effectController;

    public Box2DContactHandler(Player player, Hud hud){
        this.player = player;
        this.hud = hud;
        this.effectController = ParticleEffectController.getInstance();
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if((fixA.getUserData() == "groundSensor" || fixB.getUserData() == "groundSensor")
            && (fixA.getUserData() == "ground" || fixB.getUserData() == "ground")){
            groundContactCounter++;
            if(groundContactCounter > 0) {
                player.canJump = true;
                effectController.setPos(player.getX() + player.getWidth() / 2, player.getY());
                effectController.setEmit(true); // set to true to cause collision effect
            }
        }
        if((fixA.getUserData() == "player" || fixB.getUserData() == "player")
                && (fixA.getUserData() == "coin" || fixB.getUserData() == "coin")){
            Fixture coin = fixA.getUserData() == "coin" ? fixA : fixB;
            ((Coin)coin.getBody().getUserData()).collected();
            hud.increaseScore();
        }

        if(fixA.getUserData() == "goal" && fixB.getUserData() == "player"
                || fixB.getUserData() == "goal" && fixA.getUserData() == "player"){
            player.levelFinished = true;
        }
        if(fixA.getUserData() == "checkPoint" && fixB.getUserData() == "player"
                || fixB.getUserData() == "checkPoint" && fixA.getUserData() == "player"){
            Fixture checkPoint = fixA.getUserData() == "checkPoint" ? fixA : fixB;
            ((de.flo.ballplatformer.actors.CheckPoint)checkPoint.getBody().getUserData()).setChecked();
        }

        if(fixA.getUserData() == "spike" && fixB.getUserData() == "player"
                || fixB.getUserData() == "spike" && fixA.getUserData() == "player"){
            player.die();
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();


        if((fixA.getUserData() == "groundSensor" || fixB.getUserData() == "groundSensor")
                && (fixA.getUserData() == "ground" || fixB.getUserData() == "ground")){
            groundContactCounter--;
            if(groundContactCounter <= 0)
                player.canJump = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
