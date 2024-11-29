package com.birds.angry.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Catapult {
    private final Body body;
    private final Texture texture;
    private Body currentBird;
    public Catapult(Body body, Texture texture){
        this.body = body;
        this.texture = texture;
        this.currentBird = null;
    }

    public void shoot(Vector2 angle, int scl){
        currentBird.setTransform(1,1, 0);
        currentBird.setLinearVelocity(angle.scl(scl));
        return;
    }
    public void reloadBird(Body newBird){
        if(currentBird!=null){

            currentBird = newBird;
            return;
        }
    }

}

