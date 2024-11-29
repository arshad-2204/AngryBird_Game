package com.birds.angry;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

public class SaveMethodClass implements Serializable {
    public int id;
    public float x;
    public float y;
    public float angle;
    public float vx;
    public float vy;
    public SaveMethodClass(int id, float x, float y, float vx, float vy, float angle) {
        this.id = id;
//        this.health = health;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
    }

}
