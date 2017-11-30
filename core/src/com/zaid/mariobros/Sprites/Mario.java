package com.zaid.mariobros.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.zaid.mariobros.MarioBros;
import com.zaid.mariobros.Screens.PlayScreen;

/**
 * Created by Zaid on 11/28/2017.
 */

//this class holds the animations and action of the player
public class Mario extends Sprite{
    public enum State{FALLING, JUMPING, STANDING, RUNNING}; //holds all possible states mario can be in
    public State currentState;  //current state mario in in
    public State previousState;//the previous state mario was in
    public World world; //a big oontainer for eveything in the game
    public Body b2body; //are empty containers for objects
    private TextureRegion marioStand;//creating a marioStand texture region
    //creating the arrays of animation for the run and jump animation
    private Animation<TextureRegion> marioRun;
    private Animation<TextureRegion> marioJump;

    private float stateTimer; //keep track how much of how long mario is in a current state
    private boolean runningRight; //direction mario is currently running

    public Mario(World world, PlayScreen screen){
        super(screen.getAtlas().findRegion("little_mario")); //the texture for the little mario
        this.world = world;
        //setting the default current state, previous state of mario
        currentState = State.STANDING;
        previousState = State.STANDING;

        stateTimer = 0;
        runningRight = true;

        //taking in the frames from the png file of mario running using the for loop
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i=1; i<4; i++) {
            frames.add(new TextureRegion(getTexture(), i * 16, 0, 16, 16));
        }
        marioRun = new Animation(.01f, frames); //sets run into a animation variable
        frames.clear(); //clears the frames for htd next animation grab


        //taking in the frames from the png file of mario jumping using the for loop
        for(int i=4; i<6; i++){
            frames.add(new TextureRegion(getTexture(), i * 16, 0, 16, 16));
        }
        marioJump = new Animation(0.1f , frames); //sets jump into a animation variable

        marioStand = new TextureRegion(getTexture(), 0,0,16,16); //sets stand into a animation variable

        defineMario();
        setBounds(0, 0, 16 / MarioBros.PPM, 16 / MarioBros.PPM);
        setRegion(marioStand); //associates the texture region with that sprite
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        //this switch case is getting the current state mario and then returning the frames of the sprite needed
        TextureRegion region;
        switch(currentState){
            case JUMPING:
                region = marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioStand;
                break;
        }

        //makes it so mario is facing the correct way while he is moving
        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
           region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    //gets the current state mario is in by viewing his velocity
    public State getState(){
        if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)){
            return State.JUMPING;
        }
        else if(b2body.getLinearVelocity().y < 0){
            return State.FALLING;
        }
        else if(b2body.getLinearVelocity().x != 0){
            return State.RUNNING;
        }
        else
            return State.STANDING;
    }

    public void defineMario() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / MarioBros.PPM, 32 / MarioBros.PPM); //sets starting positon of mario
        bdef.type = BodyDef.BodyType.DynamicBody; //sets mario to be a dynamic body
        b2body = world.createBody(bdef); //creates mario to the world

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.DEFAULT_BIT | MarioBros.BRICK_BIT | MarioBros.COIN_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);

        EdgeShape head = new EdgeShape(); //creates the shape for marios head
        head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM)); //sets the hitbox for mario head
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData("head");
    }

}
