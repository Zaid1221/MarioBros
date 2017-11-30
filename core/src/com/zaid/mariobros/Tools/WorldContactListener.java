package com.zaid.mariobros.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.zaid.mariobros.Sprites.InteractiveTileObject;

/**
 * Created by Zaid on 11/30/2017.
 */

public class WorldContactListener implements ContactListener {
    @Override
    //when contact begins
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA(); //marios head
        Fixture fixB = contact.getFixtureB(); //mario body

        if(fixA.getUserData() == "head" || fixB.getUserData() == "head"){ //if marios head hit than get the object collided with
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB; //gets the head
            Fixture object = head == fixA ? fixB : fixA; //gets the ojbect

            if(object.getUserData() instanceof InteractiveTileObject){ //see if Object exists
                ((InteractiveTileObject) object.getUserData()).onHeadHit(); //if so get the specific method for hit
            }
        }
    }

    @Override
    //when contact ended
    public void endContact(Contact contact) {
    }

    @Override
    //change characteristics of objects when collision happens
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    //results of the collision
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
