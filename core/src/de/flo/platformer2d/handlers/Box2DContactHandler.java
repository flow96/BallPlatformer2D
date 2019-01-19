package de.flo.platformer2d.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.flo.platformer2d.actors.CheckPoint;
import de.flo.platformer2d.actors.Player;
import de.flo.platformer2d.screens.PlayScreen;

public class Box2DContactHandler implements ContactListener {

    private Player player;
    private int groundContactCounter = 0;

    public Box2DContactHandler(Player player){
        this.player = player;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() == "groundSensor" || fixB.getUserData() == "groundSensor"){
            groundContactCounter++;
            if(groundContactCounter > 0)
                player.canJump = true;
        }

        if(fixA.getUserData() == "goal" && fixB.getUserData() == "player"
                || fixB.getUserData() == "goal" && fixA.getUserData() == "player"){
            player.levelFinished = true;
        }
        if(fixA.getUserData() == "checkPoint" && fixB.getUserData() == "player"
                || fixB.getUserData() == "checkPoint" && fixA.getUserData() == "player"){
            Fixture checkPoint = fixA.getUserData() == "checkPoint" ? fixA : fixB;
            ((CheckPoint)checkPoint.getBody().getUserData()).setChecked();
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

        if(fixA.getUserData() == "groundSensor" || fixB.getUserData() == "groundSensor"){
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
