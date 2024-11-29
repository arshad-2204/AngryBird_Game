package com.birds.angry.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class levelSelection implements Screen {
    private final angrybird game;

    private final Texture level1IconTexture;
    private final Texture level2IconTexture;
    private final Texture level3IconTexture;
    private final Texture homeIconTexture;
    private final Texture levelbackground;

    private Rectangle level1Bounds;
    private Rectangle level2Bounds;
    private Rectangle level3Bounds;
    private Rectangle homeBounds;

    private SpriteBatch batch;
    public levelSelection(angrybird game) {
        this.game = game;
        batch = new SpriteBatch();
        // Loading textures for level icons and home button
        levelbackground = new Texture("levelbackground.png");
        level1IconTexture = new Texture("level1.png");
        level2IconTexture = new Texture("level2.png");
        level3IconTexture = new Texture("level3.png");
        homeIconTexture = new Texture("ui/backbutton.png");

        // Define the positions and sizes of the textures
        level1Bounds = new Rectangle(400, 400, 200, 200); // x, y, width, height
        level2Bounds = new Rectangle(800, 400, 200, 200);
        level3Bounds = new Rectangle(1200, 400, 200, 200);
        homeBounds = new Rectangle(50, 50, 50, 50); // Smaller for the home button
    }

    private void checkTouches() {
        if (Gdx.input.justTouched()) {
            // Get touch position
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Convert to world coordinates

            if (level1Bounds.contains(touchX, touchY)) {
                codeClass.code = 0;
                game.setScreen(new gameScreen()); // Navigate to gameScreen for level 1
            } else if (level2Bounds.contains(touchX, touchY)) {
                codeClass.code = 1;
                game.setScreen(new gameScreen()); // Navigate to gameScreen for level 2
            } else if (level3Bounds.contains(touchX, touchY)) {
                codeClass.code = 2;
                game.setScreen(new gameScreen()); // Navigate to gameScreen for level 3
            } else if (homeBounds.contains(touchX, touchY)) {
                game.setScreen(new mainMenu(game)); // Navigate to home
            }
        }
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1); // Clear the screen

        // Render background
        batch.begin();
        batch.draw(levelbackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Render textures
        batch.draw(level1IconTexture, level1Bounds.x, level1Bounds.y, level1Bounds.width, level1Bounds.height);
        batch.draw(level2IconTexture, level2Bounds.x, level2Bounds.y, level2Bounds.width, level2Bounds.height);
        batch.draw(level3IconTexture, level3Bounds.x, level3Bounds.y, level3Bounds.width, level3Bounds.height);
        batch.draw(homeIconTexture, homeBounds.x, homeBounds.y, homeBounds.width, homeBounds.height);
        batch.end();

        // Check for touches
        checkTouches();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        level1IconTexture.dispose();
        level2IconTexture.dispose();
        level3IconTexture.dispose();
        homeIconTexture.dispose();
        levelbackground.dispose();
    }
}
