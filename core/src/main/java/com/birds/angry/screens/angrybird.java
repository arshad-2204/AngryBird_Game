package com.birds.angry.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class angrybird extends Game {
    public SpriteBatch spriteBatch;

    @Override
    public void create(){
        spriteBatch = new SpriteBatch();
        codeClass.setGame(this);
        setScreen(new loadingScreen(this));
//        setScreen(new gameScreen());
    }

    @Override
    public void render(){
        super.render();
    }
}
