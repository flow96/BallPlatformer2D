package de.flo.platformer2d.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.flo.platformer2d.PlatformerGame;
import de.flo.platformer2d.actors.CheckPoint;
import de.flo.platformer2d.actors.Goal;
import de.flo.platformer2d.actors.Player;
import de.flo.platformer2d.constants.Constants;
import de.flo.platformer2d.handlers.Box2DContactHandler;
import de.flo.platformer2d.utils.Hud;
import de.flo.platformer2d.utils.MobileController;

public class PlayScreen implements Screen {

    private PlatformerGame gameManager;
    private SpriteBatch batch;
    private Stage mainStage;
    private Viewport viewport;

    // Map stuff
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private float WORLD_WIDTH, WORLD_HEIGHT;

    // Box2D (physics stuff)
    private World world;
    private Box2DDebugRenderer b2DebugRenderer;

    private MobileController controller;

    private Player player;

    public int levelIndex;

    private Hud hud;

    public boolean paused = false;
    private boolean isMobile;



    public PlayScreen(PlatformerGame gameManager, SpriteBatch batch, int levelIndex){
        this.gameManager = gameManager;
        this.batch = batch;
        this.levelIndex = levelIndex;
        this.isMobile = Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS;

        OrthographicCamera camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.V_WIDTH / Constants.PPM, Constants.V_HEIGHT / Constants.PPM, camera);
        mainStage = new Stage(viewport, batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Maps/New/level" + levelIndex + ".tmx");
        WORLD_HEIGHT = ((Integer) map.getProperties().get("height") * (Integer) map.getProperties().get("tileheight")) / Constants.PPM;
        WORLD_WIDTH = ((Integer) map.getProperties().get("width") * (Integer) map.getProperties().get("tilewidth")) / Constants.PPM;

        // Setting up the map renderer
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.PPM);
        //mapRenderer.setBlending(true);  // Allowing alpha

        // Setting up the physics world
        world = new World(new Vector2(0, -17), true);
        b2DebugRenderer = new Box2DDebugRenderer();


        // Get player start position
        Rectangle rec = map.getLayers().get("startPos").getObjects().getByType(RectangleMapObject.class).get(0).getRectangle();
        Vector2 playerPos = new Vector2((rec.getX() + rec.getWidth() / 2) / Constants.PPM, (rec.getY() + rec.getHeight()) / Constants.PPM);

        controller = new MobileController(batch);

        // Setting up the player
        player = new Player(mainStage, world, playerPos, gameManager, controller);

        Vector2 camPos = new Vector2(playerPos.x, playerPos.y);
        camPos.x = MathUtils.clamp(camPos.x, mainStage.getCamera().viewportWidth / 2, WORLD_WIDTH - mainStage.getCamera().viewportWidth / 2);
        camPos.y = MathUtils.clamp(camPos.y, mainStage.getCamera().viewportHeight / 2, WORLD_HEIGHT - mainStage.getCamera().viewportHeight / 2);
        mainStage.getCamera().position.set(camPos, 0);

        // Init world
        initWorldBox2D();

        player.toFront();

        // Set contact handler
        world.setContactListener(new Box2DContactHandler(player));

        // Set up HUD
        hud = new Hud(levelIndex, batch, this);



        InputMultiplexer im = new InputMultiplexer(controller.getStage(), hud.getStage());
        Gdx.input.setInputProcessor(im);

    }

    private void initWorldBox2D(){
        // Set up colliders
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        // Initialize ground objects
        for (MapObject object : map.getLayers().get("ground").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Constants.PPM, (rect.getY() + rect.getHeight() / 2) / Constants.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Constants.PPM, rect.getHeight() / 2 / Constants.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        // Goals
        for (MapObject object : map.getLayers().get("goal").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Constants.PPM, (rect.getY() + rect.getHeight() / 2) / Constants.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Constants.PPM, rect.getHeight() / 2 / Constants.PPM);
            fdef.shape = shape;
            fdef.isSensor = true;
            body.createFixture(fdef).setUserData("goal");

            // Add a flag image to the stage
            mainStage.addActor(new Goal(player, new Vector2(body.getPosition().x - Constants.TILE_WIDTH / 2 / Constants.PPM, body.getPosition().y - Constants.TILE_HEIGHT / 2 / Constants.PPM)));
        }

        // Checkpoints
        if(map.getLayers().get("checkpoint") != null) {
            for (MapObject object : map.getLayers().get("checkpoint").getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / Constants.PPM, (rect.getY() + rect.getHeight() / 2) / Constants.PPM);

                body = world.createBody(bdef);

                shape.setAsBox(rect.getWidth() / 2 / Constants.PPM, rect.getHeight() / 2 / Constants.PPM);
                fdef.shape = shape;
                fdef.isSensor = true;
                body.createFixture(fdef).setUserData("checkPoint");
                CheckPoint point = new CheckPoint(player, new Vector2(body.getPosition().x - Constants.TILE_WIDTH / 2 / Constants.PPM, body.getPosition().y - Constants.TILE_HEIGHT / 2 / Constants.PPM));
                body.setUserData(point);

                // Add a flag image to the stage
                mainStage.addActor(point);
            }
        }

        // Static spikes
        if(map.getLayers().get("spikes") != null) {
            // Goals
            for (MapObject object : map.getLayers().get("spikes").getObjects().getByType(RectangleMapObject.class)){
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / Constants.PPM, (rect.getY() + rect.getHeight() / 2) / Constants.PPM);

                body = world.createBody(bdef);

                shape.setAsBox(rect.getWidth() / 2 / Constants.PPM, rect.getHeight() / 2 / Constants.PPM);
                fdef.shape = shape;
                fdef.isSensor = true;
                body.createFixture(fdef).setUserData("spike");
            }
        }
    }



    @Override
    public void show() {

    }


    private void update(float delta){
        world.step(delta, 6, 2);
        mainStage.act(delta);

        Vector2 camPos = player.body.getPosition();
        camPos.x = MathUtils.clamp(camPos.x, mainStage.getCamera().viewportWidth / 2, WORLD_WIDTH - mainStage.getCamera().viewportWidth / 2);
        camPos.y = MathUtils.clamp(camPos.y, mainStage.getCamera().viewportHeight / 2, WORLD_HEIGHT - mainStage.getCamera().viewportHeight / 2);


        mainStage.getCamera().position.lerp(new Vector3(camPos.x, camPos.y, 0), 5f * delta);
        mainStage.getCamera().update();

        mapRenderer.setView((OrthographicCamera)mainStage.getCamera());
    }

    @Override
    public void render(float delta) {
        if(!paused)
            update(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        mainStage.getViewport().apply();
        mapRenderer.render();
        mainStage.draw();


        hud.draw();
        if(!paused && isMobile)
            controller.draw();


        // Debugging
        // b2DebugRenderer.render(world, mainStage.getCamera().combined);
    }

    @Override
    public void resize(int width, int height) {
        mainStage.getViewport().update(width, height);
        mainStage.getViewport().apply();
        hud.resize(width, height);
        controller.resize(width, height);
    }

    @Override
    public void pause() {
        if(!paused) {
            paused = true;
            hud.setPaused();
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        mainStage.dispose();
        b2DebugRenderer.dispose();
        world.dispose();
        mapRenderer.dispose();
    }

    public PlatformerGame getGameManager() {
        return gameManager;
    }
}
