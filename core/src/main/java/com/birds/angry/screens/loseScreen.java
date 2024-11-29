package com.birds.angry.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class loseScreen implements Screen {
    angrybird game;
    private Stage stage;
    private Texture backgrondimage;
    private Texture star;
    private Texture homebutton;
    private Texture nextlevelbutton;
    public loseScreen(angrybird game){
        this.game = game;
        stage = new Stage(new StretchViewport(1920, 1080));
        backgrondimage = new Texture("ui/Lose.jpg");
        homebutton = new Texture("ui/backbutton.png");
        nextlevelbutton = new Texture("ui/restart.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1); // Clear the screen
        float touchX = Gdx.input.getX();
        float touchY = Gdx.input.getY();
        //background
        stage.getBatch().begin();
        stage.getBatch().draw(backgrondimage, 0, 0, 1920, 1080);
        stage.getBatch().draw(homebutton, 800, 350, 100, 100);
        stage.getBatch().draw(nextlevelbutton, 900, 350, 100, 100);
        checkTouchInsideBoundary(touchX, touchY,800, 350, 100, 100, () -> {
            this.dispose();
            game.setScreen(new mainMenu(game));
        });
        checkTouchInsideBoundary(touchX, touchY,900, 350, 100, 100, () -> {
            this.dispose();
            game.setScreen(new gameScreen());
        });

        stage.getBatch().end();
    }
    public static void checkTouchInsideBoundary(float touchX, float touchY, float boundaryX, float boundaryY, float width, float height, Runnable action) {
        // Convert the touchY coordinate to match the origin at bottom-left (LibGDX default)
        float convertedTouchY = Gdx.graphics.getHeight() - touchY;

        // Check if the touch is within the boundary
        if (touchX >= boundaryX && touchX <= boundaryX + width &&
            convertedTouchY >= boundaryY && convertedTouchY <= boundaryY + height) {
            // If within boundary, execute the action
            if(Gdx.input.isTouched()){
                action.run();
            }
        }
    }
    @Override
    public void resize(int width, int height) {

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
