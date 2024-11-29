package com.birds.angry.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;

abstract class Birds extends identifier
{
    private final Body body;
    private final Texture texture;
    public Birds(Body body, Texture texture, int genericCode) {
        super(genericCode);
        this.body = body;
        this.texture = texture;
    }

    public Body getBody() {
        return body;
    }

    public Texture getTexture() {
        return texture;
    }
}
