package com.birds.angry.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Blackbird extends Birds implements ability{
    public Blackbird(Body body, Texture texture){
        super(body, texture, 1);
    }


    @Override
    public void userAbility(World world) {
        Array<Body> b = new Array<>();
        world.getBodies(b);
        for(Body t: b){
            if(t.getPosition().sub(getBody().getPosition()).len()<2){
                world.destroyBody(t);
            }
        }
    }
}
