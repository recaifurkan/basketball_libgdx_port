package com.rfbsoft.initializer;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;
import com.rfbsoft.entities.StaticObject;
import com.rfbsoft.initializer.entity.*;
import com.rfbsoft.systems.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Init {

    public static Vector3 SENSOR_POSITION = new Vector3(-1.210f, 5.56f, 0);
    public static HashMap<String, AbstractInitializer> INITIALIZER_MAP = new HashMap<String, AbstractInitializer>() {{
        put("sensor", new SensorInitializer("sensor"));
        put("baseball", new ModelledKinematicPhysicsInitializer("baseball"));
        put("pota", new ModelledStaticPhysicInitializer("pota"));
        put("terrain", new CubeTerrainInitializer("terrain"));
        put("monkey", new CubeMonkeyInitializer("monkey"));
        put("player", new G3djCharacterAnimatedModelInitializer("character"));
        put("videoPlane", new VideoPlaneInitializer("videoPlane"));
    }};

    public void loadGameEntities(Engine engine) {

        INITIALIZER_MAP.get("sensor").initialize(engine, new Vector3(SENSOR_POSITION)); // spawn baseball -1.21048 10 0
        INITIALIZER_MAP.get("pota").initialize(engine, Vector3.Zero);
        INITIALIZER_MAP.get("terrain").initialize(engine, new Vector3(0.236126f, -1.43268f, 0));
        INITIALIZER_MAP.get("baseball").initialize(engine, new Vector3(-1.21048f, 15, 0));
        INITIALIZER_MAP.get("baseball").initialize(engine, new Vector3(0, 15, 0));
        INITIALIZER_MAP.get("monkey").initialize(engine, new Vector3(3, 10, 3));
        INITIALIZER_MAP.get("player").initialize(engine, new Vector3(-20, 10, 20));
        INITIALIZER_MAP.get("videoPlane").initialize(engine);


        List<AbstractInitializer> initalizers = Arrays.asList(
                //new MonkeyInitializer("monkey"),
                //new MonkeyInitializer("monkey"),
                //new MonkeyInitializer("torus"),
                //new CharacterInitializer(),
                //new ModelledStaticInitializer("alt", new Vector3(-6.08238f, -0.186796f, 0f)),
                //new ModelledStaticPhysicInitializer("yan"),
                //new ModelledKinematicPhysicsInitializer("baseball")
                //new VideoPlaneInitializer()
        );

        for (Initializer initializer : initalizers) {
            initializer.initialize(engine);
        }

    }


    public void loadCamera() {
        // setup camera (The BoomBox model is very small so you may need to adapt camera settings for your scene)
        Camera camera = new PerspectiveCamera(60f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        StaticObject.gameCamera = camera;
        camera.near = 0.01f;
        camera.far = 1000;

        camera.position.set(-2.7988715f, 0.13724534f, 0.03140512f);
        camera.direction.set(0.98190665f, 0.1822436f, 0.051449902f);


        FirstPersonCameraController firstPersonCameraController = new FirstPersonCameraController(camera);
        StaticObject.firstPersonCameraController = firstPersonCameraController;
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new InputSystem.UserInput());
        inputMultiplexer.addProcessor(firstPersonCameraController);
        Gdx.input.setInputProcessor(inputMultiplexer);


    }


    public void loadSystems(Engine engine) {
        List<EntitySystem> systems = Arrays.asList(
                new ModelInstanceBulletPositionSystem(),
                //new GltfRenderSystem(),
                //new GltfBulletPositionSystem(),
                new ModelInstanceRenderSystem(),
                new FirstPersonCameraSystem(),
                new BulletSystem(),
                new InputSystem(),
                new VideoPlayerSystem(),
                new DebugBulletSystem(),
                new InGameConsoleSystem(),
                new CharacterAnimationSystem(),
                new AnimationSystem()
        );
        for (EntitySystem entitySystem : systems) {
            engine.addSystem(entitySystem);
        }
    }
}
