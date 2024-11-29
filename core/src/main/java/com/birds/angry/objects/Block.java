package com.birds.angry.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Block {
    private int health;
    private final Body body;
    private final Texture texture;
    public Block(Body body, Texture texture){
        this.body = body;
        this.texture = texture;
    }
}
