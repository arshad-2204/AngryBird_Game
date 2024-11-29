package com.birds.angry.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class Chuckbird extends Birds implements ability{
    public Chuckbird(Body body, Texture texture){
        super(body, texture, 1);
    }

    public void userAbility(World world){
        getBody().setLinearVelocity(getBody().getLinearVelocity().scl(2));
    }

}
