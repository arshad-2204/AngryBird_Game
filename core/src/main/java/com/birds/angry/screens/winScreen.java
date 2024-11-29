package com.birds.angry.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class winScreen implements Screen {
    angrybird game;
    private Stage stage;
    private Texture backgrondimage;
    private Texture star;
    private Texture homebutton;
    private Texture nextlevelbutton;
    private Texture restartbutton;
    private int temp;
    public winScreen(angrybird game, int tempo){
        this.game = game;
        stage = new Stage(new StretchViewport(1920, 1080));
        backgrondimage = new Texture("ui/win.png");
        homebutton = new Texture("ui/backbutton.png");
        star = new Texture("ui/star.png");
        nextlevelbutton = new Texture("ui/backbutton.png");
        restartbutton = new Texture("ui/restart.png");
        temp = tempo;
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
        stage.getBatch().draw(star, 800, 500, 200, 200);
        stage.getBatch().draw(star, 1000, 500, 200, 200);
        stage.getBatch().draw(homebutton, 800, 350, 100, 100);
        stage.getBatch().draw(restartbutton, 900, 350, 100, 100);
        stage.getBatch().draw(nextlevelbutton, 1000, 350, 100, 100, 0, 0,
            nextlevelbutton.getWidth(), nextlevelbutton.getHeight(),
            true, false);
        checkTouchInsideBoundary(touchX, touchY, 1000, 350, 100, 100, () -> {
            this.dispose();
            codeClass.code = (temp+1)%3;
            game.setScreen(new gameScreen());

        });
        checkTouchInsideBoundary(touchX, touchY,800, 350, 100, 100, () -> {
            this.dispose();
            game.setScreen(new mainMenu(game));
        });
        checkTouchInsideBoundary(touchX, touchY,900, 350, 100, 100, () -> {
            this.dispose();
//            game.setScreen(new scene3(game));
            game.setScreen(new gameScreen());
        });

        stage.getBatch().end();
    }
    public void checkTouchInsideBoundary(float touchX, float touchY, float boundaryX, float boundaryY, float width, float height, Runnable action) {
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
