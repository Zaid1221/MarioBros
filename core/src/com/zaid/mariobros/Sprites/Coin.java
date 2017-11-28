package com.zaid.mariobros.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.zaid.mariobros.MarioBros;

/**
 * Created by Zaid on 11/28/2017.
 */

public class Coin extends InteractiveTileObject {
    public Coin(World world, TiledMap map, Rectangle bounds){
        super(world, map, bounds);
    }
}
