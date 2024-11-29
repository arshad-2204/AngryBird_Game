package com.birds.angry.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Pig extends identifier{
    private int health;
    private final Body body;
    private final Texture texture;
    public Pig(Body body, Texture texture, int genericCode) {
        super(genericCode);
        this.body = body;
        this.texture = texture;
    }


    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Body getBody() {
        return body;
    }

    public Texture getTexture() {
        return texture;
    }
}
