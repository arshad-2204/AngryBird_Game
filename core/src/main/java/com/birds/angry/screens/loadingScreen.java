package com.birds.angry.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class loadingScreen implements Screen {
    private Stage stage;
    private final Texture backgroundforloadingscreen;
    angrybird game;


    public loadingScreen(angrybird game){
        stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        backgroundforloadingscreen = new Texture("opening.jpg");
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1); // Clear the screen
        stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        stage.act(delta);
        stage.getViewport().apply(); // Apply the viewport to make sure the screen scales correctly

        // Begin drawing the background
        stage.getBatch().begin();

        // Draw the background using the screen's width and height to cover the full screen
        stage.getBatch().draw(backgroundforloadingscreen, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if(Gdx.input.isTouched()){
            this.dispose();
            game.setScreen(new mainMenu(game));
        }
        stage.getBatch().end();

        stage.draw(); // Draw the stage after rendering the background
    }


    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
