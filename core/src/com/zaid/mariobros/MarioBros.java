package com.zaid.mariobros;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zaid.mariobros.Screens.PlayScreen;

public class MarioBros extends Game {

	public static final int V_WIDTH = 400; //width of screen
	public static final int V_HEIGHT = 208; //height of screen
	public static final float PPM = 100; //something to do with speed

	public static final short DEFAULT_BIT = 1;
	public static final short MARIO_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;

	public SpriteBatch batch; //container tha holds all images and textures
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		//sets the playscreen to the screen
		setScreen(new PlayScreen(this));
	}

	@Override
	//deligates the render method to the play screen or whatever screen in active at that time
	public void render () {
		super.render();
	}

}
