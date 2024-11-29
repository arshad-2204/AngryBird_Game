package com.birds.angry.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.birds.angry.SaveMethodClass;
import com.birds.angry.screens.codeClass;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

import static com.birds.angry.screens.codeClass.code;
import static com.birds.angry.screens.codeClass.game;

public class gameScreen implements Screen {
    private World gameWorld;
    public int codeClass;
    private Box2DDebugRenderer debugRenderer;
    private SpriteBatch batchSprite;
    private OrthographicCamera camera;
    private ArrayList<Body> bodies;
    private ArrayList<Body> birds;
    private Body currentBird;
    private Sprite backgroundSprite;
    private ArrayList<pairObject> pair;
    private int indexOfBird = 0;
    private Vector2 startPoint;
    private Vector2 endPoint;
    private int instance = 0;
    private int pig = 0;
    private int bird = 0;
    private Texture catapult;
    private Body currentAbilityBird;
    private Texture homebutton;
    private Texture restartLevelButton;
    @Override
    public void show() {
        gameWorld = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();
        batchSprite = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 20, 12);
        bodies = new ArrayList<>();
        birds = new ArrayList<>();
        backgroundSprite = new Sprite(new Texture("gamebackground.png"));
        currentAbilityBird = null;
        currentBird = null;
        pair = new ArrayList<>();
        startPoint = new Vector2();
        endPoint = new Vector2();
        catapult = new Texture(Gdx.files.internal("catapult.png"));
        // Creating ground here
     // Dispose shape after creating fixture
        bodies = new ArrayList<>();
        createBorder();
        methodTohealth();
//        createStructure1();
        if(com.birds.angry.screens.codeClass.code ==0){
            createStructure2();
        }else if(com.birds.angry.screens.codeClass.code ==1){
            createStructure3();
        }else{
            createStructure1();
        }
        createCatapultPlatform();
        homebutton = new Texture("ui/backbutton.png");
        restartLevelButton = new Texture("ui/restart.png");
        if(com.birds.angry.screens.codeClass.isload ==1){
            loadGameFromFile();
            bird = 0;
        }
        for(Body b : bodies){
            b.setAngularDamping(5f);
        }
    }

    @Override
    public void render(float delta) {
        try{
            currentBird = birds.get(indexOfBird);
            currentBird.setTransform(new Vector2(3, 3), 0);
        }catch (IndexOutOfBoundsException e){
        }
        Gdx.gl.glClearColor(0, 0, 0, 1); // Set clear color to black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        if(!birds.isEmpty()){
            currentBird = birds.getFirst();
        }
        gameWorld.step(delta, 6, 2); // Step the physics world
        camera.update(); // Update the camera
        batchSprite.setProjectionMatrix(camera.combined);
        batchSprite.begin();
        batchSprite.draw(backgroundSprite, 0, -1, 20, 13);
        batchSprite.draw(homebutton, 1, 11, 1, 1);
        batchSprite.draw(restartLevelButton, 2, 11, 1, 1);
        batchSprite.draw(catapult, 2.1f, 1.7f, 1.5f, 2);
        for (Body body : bodies) {
            if(body.getUserData() != null && body.getUserData() instanceof Sprite) {
                Sprite sprite = (Sprite) body.getUserData();
                sprite.setPosition(body.getPosition().x - sprite.getWidth()/2, body.getPosition().y - sprite.getHeight()/2);
                sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
                sprite.draw(batchSprite);
            }
        }
        Vector2 dragVector = calculateDrag();
        if (dragVector != null && instance++ > 1) {
            System.out.println("Drag vector: " + dragVector);
            Vector2 normalizedDir = dragVector.nor().scl(1); // Normalize direction
            normalizedDir.x = -normalizedDir.x;
            try{
                currentBird = birds.get(indexOfBird++);
            }catch (IndexOutOfBoundsException e){
                bird++;
                currentBird = null;
            }
            if(currentBird!=null){
                shoot(new Vector3(normalizedDir.x, normalizedDir.y, 10)); // Example usage
                currentAbilityBird = currentBird;
                bird++;
            }

        }
        batchSprite.end();
        checkInput();
        checkOutput();
//        System.out.println("Pig is " + pig);
        if(pig==0){
            //win
            System.out.println("Win");
            game.setScreen(new winScreen(game, com.birds.angry.screens.codeClass.code));
        }else if(bird > birds.size()){
            System.out.println("Lose");
            game.setScreen(new loseScreen(game));
        }
//        debugRenderer.render(gameWorld, camera.combined); // Render debug lines
        buttonCheck();

    }

    public void buttonCheck(){
        float touchX = Gdx.input.getX();
        float touchY = Gdx.input.getY();
//        Vector3 worldCoordinates = camera.unproject(new Vector3(touchX, touchY, 0));
//        float worldx = worldCoordinates.x;
//        float worldy = worldCoordinates.y;
        if(Gdx.input.justTouched()){
            System.out.println(touchX+ "-+-" + touchY);
            if(touchX>100 && touchX<170 && touchY>10 && touchY < 70){
                game.setScreen(new mainMenu(game));
            }
            if(touchY>10 && touchY<70 && touchX > 180 && touchX < 250){
                game.setScreen(new gameScreen());
            }
        }


    }


    @Override
    public void resize(int width, int height) {

    }
    public Vector2 calculateDrag() {
        if (Gdx.input.justTouched()) {
            startPoint = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            System.out.println("Start point recorded: " + startPoint);
        }
        if (!Gdx.input.isTouched() && startPoint != null) {
            endPoint = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            System.out.println("End point recorded: " + endPoint);
            Vector2 dragVector = endPoint.sub(startPoint);
            System.out.println("Drag vector calculated: " + dragVector);

            // Reset points
            startPoint = null;
            endPoint = null;

            return dragVector;
        }

        return null;
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


    public Body createRedBird(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.25f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.31f;

        Body body = gameWorld.createBody(bodyDef);
        Sprite tempSprite = new Sprite(new Texture("bird1.png"));
        tempSprite.setSize(0.5f, 0.5f);
        tempSprite.setOrigin(tempSprite.getWidth()/2, tempSprite.getHeight() / 2);
        tempSprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        tempSprite.setOriginCenter();
        body.setUserData(tempSprite);

        body.createFixture(fixtureDef);
        bodies.add(body);
        birds.add(body);
        return body;
    }


    public void checkInput(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
            System.out.println("Speed");
            if(currentBird!=null){
                speedAbilityForYellowBird();
                currentBird = null;
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F)){
//            currentBird = birds.getFirst();
            System.out.println("Bomb");
            if(currentBird!=null){
                blastAbilityForBlackBird();
                currentBird = null;
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.Y)){
            saveGameintoFile();
            System.out.println("Saved game into file");
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.G)){
            loadGameFromFile();
            System.out.println("Game loaded");
        }
    }

    public void shoot(Vector3 directionAndPower){
        System.out.println("Hi");
            currentBird.setTransform(3, 3, 0);
            currentBird.setLinearVelocity(directionAndPower.x * directionAndPower.z, directionAndPower.y * directionAndPower.z);
        }

    public void createCatapultPlatform() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(3, 2);
        PolygonShape catapultShape = new PolygonShape();
        catapultShape.setAsBox(0.5f, 0.5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = catapultShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.3f;
        Body temp = gameWorld.createBody(bodyDef);
        temp.createFixture(fixtureDef);
        bodies.add(temp);
    }


    public Body createWood(float x, float y, float width, float height, float res){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.25f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = res;
//        body.createFixture(fixtureDef);

        Sprite tempSprite = new Sprite(new Texture("wood1.png"));
        tempSprite.setSize(2 * width,2 * height);
//        tempSprite.setOrigin(tempSprite.getWidth()/2, tempSprite.getHeight() / 2);
        tempSprite.setOriginCenter();
        Body body = gameWorld.createBody(bodyDef);
        body.setUserData(tempSprite);
        body.createFixture(fixtureDef);
        bodies.add(body);
        pair.add(new pairObject(body, 22, 7));
        return body;
    }
    public Body createStone(float x, float y, float width, float height, float res){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = res;
//        body.createFixture(fixtureDef);

        Sprite tempSprite = new Sprite(new Texture("stone1.png"));
        tempSprite.setSize(2 * width,2 * height);
//        tempSprite.setOrigin(tempSprite.getWidth()/2, tempSprite.getHeight() / 2);
        tempSprite.setOriginCenter();
        Body body = gameWorld.createBody(bodyDef);
        body.setUserData(tempSprite);
        body.createFixture(fixtureDef);
        bodies.add(body);
        pair.add(new pairObject(body, 25, 8));
        return body;
    }
    public Body createGlass(float x, float y, float width, float height, float res){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = res;
//        body.createFixture(fixtureDef);

        Sprite tempSprite = new Sprite(new Texture("ice1.png"));
        tempSprite.setSize(2 * width,2 * height);
//        tempSprite.setOrigin(tempSprite.getWidth()/2, tempSprite.getHeight() / 2);
        tempSprite.setOriginCenter();
        Body body = gameWorld.createBody(bodyDef);
        body.setUserData(tempSprite);
        body.createFixture(fixtureDef);
        bodies.add(body);
        pair.add(new pairObject(body, 10, 9));
        return body;
    }
    public Body createYellowBird(float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
//        bodyDef.position.set(getMousePosition().x, getMousePosition().y);
        bodyDef.position.set(x, y);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.24f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.32f;

        Body body = gameWorld.createBody(bodyDef);
        Sprite tempSprite = new Sprite(new Texture("bird2.png"));
        tempSprite.setSize(0.6f, 0.6f);
        tempSprite.setOrigin(tempSprite.getWidth()/2, tempSprite.getHeight() / 2);
        tempSprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        tempSprite.setOriginCenter();
        body.setUserData(tempSprite);

        body.createFixture(fixtureDef);
        bodies.add(body);
//        createWoodBlock(1, getMousePosition().x, getMousePosition().y);
        birds.add(body);
        return body;
    }
    public Body createBlackBird(float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
//        bodyDef.position.set(getMousePosition().x, getMousePosition().y);
        bodyDef.position.set(x, y);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.4f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.33f;

        Body body = gameWorld.createBody(bodyDef);
        Sprite tempSprite = new Sprite(new Texture("bird3.png"));
        tempSprite.setSize(0.8f, 0.8f);
        tempSprite.setOrigin(tempSprite.getWidth()/2, tempSprite.getHeight() / 2);
        tempSprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        tempSprite.setOriginCenter();
        body.setUserData(tempSprite);

        body.createFixture(fixtureDef);
        bodies.add(body);
        birds.add(body);
        return body;
    }
    public Body createPig1(float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
//        bodyDef.position.set(getMousePosition().x, getMousePosition().y);
        bodyDef.position.set(x, y);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.2f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.34f;

        Body body = gameWorld.createBody(bodyDef);
        Sprite tempSprite = new Sprite(new Texture("pig1.png"));
        tempSprite.setSize(0.4f, 0.4f);
        tempSprite.setOrigin(tempSprite.getWidth()/2, tempSprite.getHeight() / 2);
        tempSprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        tempSprite.setOriginCenter();
        body.setUserData(tempSprite);

        body.createFixture(fixtureDef);
        bodies.add(body);
        pair.add(new pairObject(body, 20, 4));
        return body;
    }
    public Body createPig2(float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
//        bodyDef.position.set(getMousePosition().x, getMousePosition().y);
        bodyDef.position.set(x, y);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.2f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.35f;

        Body body = gameWorld.createBody(bodyDef);
        Sprite tempSprite = new Sprite(new Texture("pig2.png"));
        tempSprite.setSize(0.4f, 0.4f);
        tempSprite.setOrigin(tempSprite.getWidth()/2, tempSprite.getHeight() / 2);
        tempSprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        tempSprite.setOriginCenter();
        body.setUserData(tempSprite);

        body.createFixture(fixtureDef);
        bodies.add(body);
        pair.add(new pairObject(body, 12, 5));
        return body;
    }

    public Body createPig3(float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
//        bodyDef.position.set(getMousePosition().x, getMousePosition().y);
        bodyDef.position.set(x, y);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.3f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.36f;

        Body body = gameWorld.createBody(bodyDef);
        Sprite tempSprite = new Sprite(new Texture("pig3.png"));
        tempSprite.setSize(0.6f, 0.6f);
        tempSprite.setOrigin(tempSprite.getWidth()/2, tempSprite.getHeight() / 2);
        tempSprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        tempSprite.setOriginCenter();
        body.setUserData(tempSprite);

        body.createFixture(fixtureDef);
        bodies.add(body);
        pair.add(new pairObject(body, 15, 6));
        return body;
    }
    public void methodTohealth(){
        gameWorld.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                for (pairObject pair : pair) {
                    if(contact.getFixtureA().getBody().equals(pair.body) || contact.getFixtureB().getBody().equals(pair.body)){
                        Body mainBody;
                        if(contact.getFixtureA().getBody().equals(pair.body)){
                            mainBody = contact.getFixtureB().getBody();
                        }else{
                            mainBody = contact.getFixtureA().getBody();
                        }
                        float factor = mainBody.getLinearVelocity().sub(pair.body.getLinearVelocity()).len() * 1;
                        pair.health -=mainBody.getLinearVelocity().sub(pair.body.getLinearVelocity()).len() * 1;
                        System.out.println("Cur: " + pair.health + "damage: + " + factor);
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    public void checkOutput() {
        Iterator<pairObject> iterator = pair.iterator();
        while (iterator.hasNext()) {
            pairObject t = iterator.next();
            if (t.health <= 0) {
                if(checkifpig(t.body)){
                    pig--;
                }
                gameWorld.destroyBody(t.body);  // Safely destroy the body
                bodies.remove(t.body);         // Remove body from the bodies list
                iterator.remove();             // Safely remove the object from pair
            }
        }
    }
    public boolean checkifpig(Body body){
        for (Fixture fixture : body.getFixtureList()) {
            if(fixture.getShape().getRadius()==0.2f || fixture.getShape().getRadius()==0.3f){
                return true;
            }
        }
        return false;
    }
    public void createWoodBlock(int type, float x, float y) {
        switch (type) {
            case 1:
                createWood(x, y, 0.25f, 0.25f, 0.31f);
                break;
            case 2:
                createWood(x, y, 0.125f, 0.75f, 0.32f);
                break;
            case 3:
                createWood(x, y, 0.75f, 0.125f, 0.33f);
                break;
            default:
        }
    }

    public void createStoneBlock(int type, float x, float y) {
        switch (type) {
            case 1:
                createStone(x, y, 0.25f, 0.25f, 0.31f);
                break;
            case 2:
                createStone(x, y, 0.125f, 0.75f, 0.32f);
                break;
            case 3:
                createStone(x, y, 0.75f, 0.125f, 0.33f);
                break;
            default:
        }
    }
    public void createGlassBlock(int type, float x, float y) {
        switch (type) {
            case 1:
                createGlass(x, y, 0.25f, 0.25f, 0.31f);
                break;
            case 2:
                createGlass(x, y, 0.125f, 0.75f, 0.32f);
                break;
            case 3:
                createGlass(x, y, 0.75f, 0.125f, 0.33f);
                break;
            default:
        }
    }


    public void createStructure2() {
        createWoodBlock(1, 12, 2);
        createWoodBlock(1, 12.5f, 2);
        createWoodBlock(1, 13, 2);
        createWoodBlock(1, 13.5f, 2);
        createWoodBlock(1, 12.5f, 2.51f);
        createWoodBlock(1, 13, 2.51f);
        createWoodBlock(1, 13.5f, 2.51f);
        createWoodBlock(1, 13, 3.1f);
        createWoodBlock(1, 13.5f, 3.1f);
        createWoodBlock(1, 13.5f, 3.5f);
        createPig1(13f, 3.5f);
        pig++;
        createRedBird(1, 2);
        createRedBird(2, 2);
        createBlackBird(1, 2);
//        createYellowBird(0, 2);
//        createRedBird()
    }
    public void createStructure1() {
        createStoneBlock(3,12, 1.5f);
        createStoneBlock(3, 13.5f, 1.5f);
        createStoneBlock(3, 15f, 1.5f);
        createStoneBlock(1, 11.5f, 1.75f);
        createStoneBlock(1, 12.5f, 1.75f);
        createStoneBlock(1, 13.5f, 1.75f);
        createStoneBlock(1, 14.5f, 1.75f);
        createStoneBlock(1, 15.5f, 1.75f);
        createPig2(12, 1.75f);
        createPig2(13, 1.75f);
        createPig2(14, 1.75f);
        createPig2(15, 1.75f);
        createStoneBlock(3, 11.75f, 2);
        createStoneBlock(3, 13.25f, 2);
        createStoneBlock(3, 14.75f, 2);
        createWoodBlock(1, 12.5f, 2.5f);
        createWoodBlock(1, 13.5f, 2.5f);
        createWoodBlock(1, 14.5f, 2.5f);
        createWoodBlock(3, 13.5f, 3f);
        createPig1(13.25f, 2.5f);
        createPig1(14f, 2.5f);
        createGlassBlock(1, 13, 3.25f);
        createGlassBlock(1, 14, 3.25f);
        createPig3(13.5f, 3.75f);
        createRedBird(3, 2);
        createBlackBird(2, 2);
        createYellowBird(1, 2);
        createBlackBird(4, 2);
        pig = 7;
    }

    public void createStructure3() {
        createStoneBlock(1, 12, 2);
        createStoneBlock(1, 12.5f, 2);
        createStoneBlock(1, 13, 2);
        createStoneBlock(1, 13.5f, 2);
        createGlassBlock(1, 12f, 3.1f);
        createGlassBlock(1, 13.5f, 3.1f);
        createStoneBlock(1, 12f, 2.51f);
        createStoneBlock(1, 13.5f, 2.51f);
        createStoneBlock(1, 12, 3.51f);
        createStoneBlock(1, 13.5f, 3.51f);
        createWoodBlock(3, 12.7f, 4);
        createWoodBlock(1, 12.75f, 4.51f);
        createWoodBlock(1, 12.5f, 4.51f);
        createPig2(13f, 2.4f);
        createPig1(12.7f, 2.4f);
        pig = 2;
        createRedBird(3, 2);
        createYellowBird(2, 2);
        createBlackBird(1, 2);
    }
    @Override
    public void dispose() {
        gameWorld.dispose();
        debugRenderer.dispose();
        batchSprite.dispose();
    }

    public void speedAbilityForYellowBird(){
//        if(currentBird)
        if(currentAbilityBird==null){
            return;
        }
        for(Fixture fix : currentAbilityBird.getFixtureList()){
            if(fix.getShape() instanceof CircleShape){
                float radius = ((CircleShape) fix.getShape()).getRadius();
                if(radius==0.24f){
                    currentAbilityBird.setLinearVelocity(currentAbilityBird.getLinearVelocity().scl(2));
                    currentAbilityBird = null;
                }
            }
        }
        System.out.println("Current masss is set to : " + currentBird.getMass());
    }
    public void blastAbilityForBlackBird(){
        if(currentAbilityBird==null){
            return;
        }
        for(Fixture fix : currentAbilityBird.getFixtureList()){
            if(fix.getShape() instanceof CircleShape){
                float radius = ((CircleShape) fix.getShape()).getRadius();
                if(radius==0.4f){
                    for (Body b: bodies) {
                        if(b.getPosition().sub(currentAbilityBird.getPosition()).len()<=3){
//                            gameWorld.destroyBody(b);
                            for(pairObject p : pair){
                                if(p.body.equals(b)){
                                    p.health = 0;
                                }
                            }
                        }
                    }
                    currentAbilityBird = null;
                }
            }
        }
    }

    public void saveGameintoFile(){
        ArrayList<SaveMethodClass> objects = new ArrayList<>();
        for (Body b : bodies) {
            Vector2 position = b.getPosition();
            Vector2 velocity = b.getLinearVelocity();
            float angle = b.getAngle();
            for(Fixture fix : b.getFixtureList()){
                if(fix.getShape() instanceof CircleShape){
                    if(fix.getRestitution()==0.31f){
                        if(isused(b)){
                            objects.add(new SaveMethodClass(100, b.getPosition().x, b.getPosition().y, b.getLinearVelocity().x, b.getLinearVelocity().y, b.getAngle()));
                        }else{
                            objects.add(new SaveMethodClass(0, b.getPosition().x, b.getPosition().y, b.getLinearVelocity().x, b.getLinearVelocity().y, b.getAngle()));
                        }
                    }else if (fix.getRestitution()==0.32f){
                        if(isused(b)){
                            objects.add(new SaveMethodClass(101, b.getPosition().x, b.getPosition().y, b.getLinearVelocity().x, b.getLinearVelocity().y, b.getAngle()));
                        }else{
                            objects.add(new SaveMethodClass(1, b.getPosition().x, b.getPosition().y, b.getLinearVelocity().x, b.getLinearVelocity().y, b.getAngle()));
                        }
                    }else if(fix.getRestitution()==0.33f){
                        if(isused(b)){
                            objects.add(new SaveMethodClass(102, b.getPosition().x, b.getPosition().y, b.getLinearVelocity().x, b.getLinearVelocity().y, b.getAngle()));
                        }else{
                            objects.add(new SaveMethodClass(2, b.getPosition().x, b.getPosition().y, b.getLinearVelocity().x, b.getLinearVelocity().y, b.getAngle()));
                        }
                    }else if (fix.getRestitution()==0.34f){
                        objects.add(new SaveMethodClass(3, b.getPosition().x, b.getPosition().y, b.getLinearVelocity().x, b.getLinearVelocity().y, b.getAngle()));
                    }else if(fix.getRestitution()==0.35f){
                        objects.add(new SaveMethodClass(4, b.getPosition().x, b.getPosition().y, b.getLinearVelocity().x, b.getLinearVelocity().y, b.getAngle()));
                    }else if(fix.getRestitution()==0.35f){
                        objects.add(new SaveMethodClass(5, b.getPosition().x, b.getPosition().y, b.getLinearVelocity().x, b.getLinearVelocity().y, b.getAngle()));
                    }
                }else if(fix.getShape() instanceof PolygonShape){
                    if(fix.getDensity()==0.25f){
                        System.out.println("Saved with restitution " + fix.getRestitution());
                        if(fix.getRestitution()==0.31f){
                            objects.add(new SaveMethodClass(6, b.getPosition().x, b.getPosition().y, b.getLinearVelocity().x, b.getLinearVelocity().y, b.getAngle()));
                        }else if (fix.getRestitution()==0.32f){
                            objects.add(new SaveMethodClass(7, b.getPosition().x, b.getPosition().y, b.getLinearVelocity().x, b.getLinearVelocity().y, b.getAngle()));
                        }else if(fix.getRestitution()==0.33f){
                            objects.add(new SaveMethodClass(8, b.getPosition().x, b.getPosition().y, b.getLinearVelocity().x, b.getLinearVelocity().y, b.getAngle()));
                        }
                    }else if(fix.getDensity()==0.5f){
                        if(fix.getRestitution()==0.31f){
                            objects.add(new SaveMethodClass(9, b.getPosition().x, b.getPosition().y, b.getLinearVelocity().x, b.getLinearVelocity().y, b.getAngle()));
                        }else if (fix.getRestitution()==0.32f){
                            objects.add(new SaveMethodClass(10, b.getPosition().x, b.getPosition().y, b.getLinearVelocity().x, b.getLinearVelocity().y, b.getAngle()));
                        }else if(fix.getRestitution()==0.33f){
                            objects.add(new SaveMethodClass(11, b.getPosition().x, b.getPosition().y, b.getLinearVelocity().x, b.getLinearVelocity().y, b.getAngle()));
                        }
                    }else if(fix.getDensity()==0.1f){
                        if(fix.getRestitution()==0.31f){
                            objects.add(new SaveMethodClass(12, b.getPosition().x, b.getPosition().y, b.getLinearVelocity().x, b.getLinearVelocity().y, b.getAngle()));
                        }else if (fix.getRestitution()==0.32f){
                            objects.add(new SaveMethodClass(13, b.getPosition().x, b.getPosition().y, b.getLinearVelocity().x, b.getLinearVelocity().y, b.getAngle()));
                        }else if(fix.getRestitution()==0.33f){
                            objects.add(new SaveMethodClass(14, b.getPosition().x, b.getPosition().y, b.getLinearVelocity().x, b.getLinearVelocity().y, b.getAngle()));
                        }
                    }
                }
            }
            System.out.println("Saving state for obj");
        }

        try{
            FileOutputStream file = new FileOutputStream("binarydataforsavinggame.bin");
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(objects);
            out.close();
            System.out.println("File closed");
        }catch (IOException e){
            System.out.println("NO");
        }
    }
    public boolean isused(Body b){
        if(birds.indexOf(b) < indexOfBird){
            //already used
            return true;
        }else{
            return false;
        }
    }
    public void clearWorld(){

    }
    public void createBorder(){
        BodyDef groundDef = new BodyDef();
        groundDef.type = BodyDef.BodyType.StaticBody;
        groundDef.position.set(10, 1); // Center of the ground at (10, 1)
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(10, 0.5f); // Width 20, height 1
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.5f; // Slight friction for objects
        fixtureDef.restitution = 0.3f; // Bounciness
        gameWorld.createBody(groundDef).createFixture(fixtureDef);
        groundShape.dispose();
        BodyDef boundary = new BodyDef();
        boundary.type = BodyDef.BodyType.StaticBody;
        boundary.position.set(0, 0);
        ChainShape bodyShape = new ChainShape();
        bodyShape.createChain(new Vector2[]{new Vector2(0, 0), new Vector2(0, 12), new Vector2(20, 12), new Vector2(20, 0)});
        FixtureDef fixtureDef2 = new FixtureDef();
        fixtureDef2.shape = bodyShape;
        fixtureDef2.density = 1f;
        fixtureDef2.friction = 0.5f;
        fixtureDef2.restitution = 0.3f;
        gameWorld.createBody(boundary).createFixture(fixtureDef2);
        createCatapultPlatform();
    }
    public void loadGameFromFile() {
        bodies.clear();
        birds.clear();
        pig = 0;
        indexOfBird = 0;
        Array<Body> bodies = new Array<>();
        gameWorld.getBodies(bodies);

        for (Body body : bodies) {
            gameWorld.destroyBody(body);
        }
        createBorder();
        try {
            FileInputStream file = new FileInputStream("binarydataforsavinggame.bin");
            ObjectInputStream in = new ObjectInputStream(file);

            // Read the serialized objects
            ArrayList<SaveMethodClass> objects = (ArrayList<SaveMethodClass>) in.readObject();
            in.close();

            // Loop through the saved objects and recreate game state
            for (SaveMethodClass obj : objects) {
                Body body;
                switch (obj.id) {
                    case 0:
                        body = createRedBird(obj.x, obj.y);
                        break;
                    case 100:
                        body = createRedBird(obj.x, obj.y);
                        birds.remove(body);
                        break;
                    case 1:
                        body = createYellowBird(obj.x, obj.y);
                        break;
                    case 101:
                        body = createYellowBird(obj.x, obj.y);
                        birds.remove(body);
                        break;
                    case 2:
                        body = createBlackBird(obj.x, obj.y);
                        break;
                    case 102:
                        body = createBlackBird(obj.x, obj.y);
                        birds.remove(body);
                        break;
                    case 3:
                        body = createPig1(obj.x, obj.y);
                        pig++;
                        break;
                    case 4:
                        body = createPig2(obj.x, obj.y);
                        pig++;
                        break;
                    case 5:
                        body = createPig3(obj.x, obj.y);
                        pig++;
                        break;
                    case 6:
                        body = createWood(obj.x, obj.y, 0.25f, 0.25f, 0.31f);
                        break;
                    case 7:
                        body = createWood(obj.x, obj.y, 0.125f, 0.75f, 0.32f);
                        break;
                    case 8:
                        body = createWood(obj.x, obj.y, 0.75f, 0.125f, 0.33f);
                        break;
                    case 9:
                        body = createStone(obj.x, obj.y, 0.25f, 0.25f, 0.31f);
                        break;
                    case 10:
                        body = createStone(obj.x, obj.y, 0.125f, 0.75f, 0.32f);
                        break;
                    case 11:
                        body = createStone(obj.x, obj.y, 0.75f, 0.125f, 0.33f);
                        break;
                    case 12:
                        body = createGlass(obj.x, obj.y, 0.25f, 0.25f, 0.31f);
                        break;
                    case 13:
                        body = createGlass(obj.x, obj.y, 0.125f, 0.75f, 0.32f);
                        break;
                    case 14:
                        body = createGlass(obj.x, obj.y, 0.75f, 0.125f, 0.33f);
                        break;
                    default:
                        body = createPig1(1, 1);
                }
                body.setLinearVelocity(obj.vx, obj.vy);
                body.setTransform(obj.x, obj.y, obj.angle);
                System.out.println("Loaded object with ID: " + obj.id);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading game: " + e.getMessage());
        }
    }

}
