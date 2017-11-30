package com.zaid.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zaid.mariobros.MarioBros;
import com.zaid.mariobros.Scenes.Hud;
import com.zaid.mariobros.Sprites.Mario;
import com.zaid.mariobros.Tools.B2WorldCreator;
import com.zaid.mariobros.Tools.WorldContactListener;

import static sun.audio.AudioPlayer.player;

/**
 * Created by Zaid on 11/27/2017.
 */

public class PlayScreen implements Screen {
    //reference to our game, used to set screens
    private MarioBros game;
    private TextureAtlas atlas; // load images from a texture packer

    //basic playscreen varaibles
    Texture texture;
    private OrthographicCamera gamecam; //follows our game world and what view port displays
    private Viewport gamePort;
    private Hud hud;

    //Tiled map varibles
    private TmxMapLoader mapLoader; //used to losf the map
    private TiledMap map; //reference to the map
    private OrthogonalTiledMapRenderer renderer; // renders map to the screen

    //box2d variables
    private World world;
    private Box2DDebugRenderer b2dr; //graphical representation of our fixture and bodies

    //sprites
    private Mario player;

    public PlayScreen(MarioBros game) {
        atlas = new TextureAtlas("Mario_and_Enemies.pack"); //loads the pack file
        this.game = game;
        gamecam = new OrthographicCamera(); //create cam used to follow mario through cam world

        gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM, gamecam); //create a fitViewport to maintain virtuaal aspect ratio despite screen size

        hud = new Hud(game.batch);//create our game HUD for scores/timers and level info

        //load our map and setup map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/ MarioBros.PPM);

        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0); //instially set out gamecam to be center

        world = new World(new Vector2(0, -10), true); //create our box2d world, setting no gravity in x, -10 gravity to y and allow

        b2dr = new Box2DDebugRenderer();//allows for debug lines of our box2d world

        new B2WorldCreator(world, map);

        //create mario in our game world
        player = new Mario(world, this);

        world.setContactListener(new WorldContactListener()); //thiscreates the listener so the objects react when collided
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt){
        if(Gdx.input.isTouched())
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);

        if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && (player.b2body.getLinearVelocity().x <= 2))
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && (player.b2body.getLinearVelocity().x >= -2))
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
    }

    public void update(float dt){
        handleInput(dt); //handle user input

        world.step(1/60f, 6, 2); //used to find out how many times to calculate the physics of mario

        player.update(dt);

        gamecam.position.x = player.b2body.getPosition().x;

        gamecam.update();//update our gamecam with correct coordinates after changes
        renderer.setView(gamecam); //tell our renderer to draw only what our camera can see in our game world
    }

    @Override
    public void render(float delta) {
        update(delta);

        //clears the scrren
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render(); //render the game map
        b2dr.render(world, gamecam.combined);//renderer our Box2dDebugLines

        game.batch.setProjectionMatrix(gamecam.combined); //recognize where the camera is and only render that area

        game.batch.begin(); //opens the box with the sprites in it
        player.draw(game.batch); //draws the texture to the screen
        game.batch.end(); //close the box

        //set our batch to now draw what the hud camera sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    //when the screen is resized method is called to change the scrren dimensions
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
