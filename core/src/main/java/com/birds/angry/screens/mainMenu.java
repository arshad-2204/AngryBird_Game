package com.birds.angry.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class mainMenu implements Screen {
    private final Texture backgroundforscene;
    private final Texture button1Texture;
    private final Texture button2Texture;
    private final Texture button3Texture;
    private final angrybird game;
    private Stage stage;

    // Button properties
    private float buttonWidth = 300;
    private float buttonHeight = 150;
    private float button1X, button1Y;
    private float button2X, button2Y;
    private float button3X, button3Y;

    public mainMenu(angrybird game) {
        this.game = game;

        backgroundforscene = new Texture("mainbackground.png");
        button1Texture = new Texture("ui/play.png");
        button2Texture = new Texture("ui/load.png");
        button3Texture = new Texture("ui/exit.png");

        // Set button positions (vertically centered)
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
//        float centerX = screenWidth / 2 - buttonWidth / 2;
//        float centerY = screenHeight / 2 - buttonHeight / 2;
//
//        button1X = centerX;
//        button1Y = centerY + 150;
//        button2X = centerX;
//        button2Y = centerY;
//        button3X = centerX;
//        button3Y = centerY - 150;
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);

        stage.act(delta);
        stage.getViewport().apply();

        stage.getBatch().begin();

        // Draw background and buttons
        stage.getBatch().draw(backgroundforscene, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        stage.getBatch().draw(button1Texture, button1X, button1Y, buttonWidth, buttonHeight);
//        stage.getBatch().draw(button2Texture, button2X, button2Y, buttonWidth, buttonHeight);
//        stage.getBatch().draw(button3Texture, button3X, button3Y, buttonWidth, buttonHeight);
        stage.getBatch().draw(button1Texture, 800, 600, 300, 150);
        stage.getBatch().draw(button2Texture, 800, 400, 300, 150);
        stage.getBatch().draw(button3Texture, 800, 200, 300, 150);

        stage.getBatch().end();

        // Input handling
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Invert Y-axis for screen coordinates
            System.out.println(touchX + " " + touchY);

            // Check if touch coordinates are within the button bounds
            if (touchX > 800 && touchX < 1000 && touchY > 600 && touchY < 750) {
                this.dispose();
                codeClass.isload = 0;
//                game.setScreen(new scene3(game)); // Switch to scene3
                game.setScreen(new gameScreen());
            } else if (touchX>800 && touchX < 1000 && touchY > 400 && touchY < 550 ) {
                this.dispose();
                codeClass.isload = 1;
                game.setScreen(new gameScreen());

            } else if (touchX>800 && touchX < 1000 && touchY>200 && touchY < 350) {
                Gdx.app.exit(); // Exit the game
            }
        }

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        backgroundforscene.dispose();
        button1Texture.dispose();
        button2Texture.dispose();
        button3Texture.dispose();
        stage.dispose();
    }
}
