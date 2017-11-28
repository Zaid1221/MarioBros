package com.zaid.mariobros.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Zaid on 11/28/2017.
 */

public class Brick extends InteractiveTileObject {
    public Brick(World world, TiledMap map, Rectangle bounds){
        super(world, map, bounds);
    }
}
