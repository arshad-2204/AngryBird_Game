package com.birds.angry.screens;

import com.badlogic.gdx.physics.box2d.Body;

public class pairObject {
    public Body body;
    public int health;
    public  int genericCode;
    pairObject(Body body, int health, int genericCode) {
        this.body = body;
        this.health = health;
    }

}
