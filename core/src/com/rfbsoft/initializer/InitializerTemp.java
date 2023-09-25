package com.rfbsoft.initializer;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.Pools;
import com.rfbsoft.components.SceneComponent;
import com.rfbsoft.entities.GameEntity;
import com.rfbsoft.entities.StaticObject;
import com.rfbsoft.initializer.data.BulletDynamicInitializer;
import com.rfbsoft.initializer.data.BulletSensorInitializer;
import com.rfbsoft.initializer.data.BulletStaticInitializer;
import com.rfbsoft.initializer.entity.data.BulletModel;
import com.rfbsoft.systems.*;
import com.rfbsoft.utils.BulletUtils;
import com.rfbsoft.utils.ObjectAllocator;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

import java.util.Arrays;
import java.util.List;

public class InitializerTemp {

    private final static String TAG = InitializerTemp.class.getName();

    public void loadModels(Engine engine) {
        List<BulletModel> list = Arrays.asList(
                new BulletModel("pota"),
                new BulletModel("yan")
        );

        for (BulletModel model : list) {
            Scene scene = new Scene(model.sceneAsset.scene);
            GameEntity gameEntity = new GameEntity();
            SceneComponent sceneComponent = new SceneComponent(scene);


            btCollisionShape collisionShape = Bullet.obtainStaticNodeShape(model.modelInstance.model.nodes);
            Matrix4 collisionShapeTransform = Pools.get(Matrix4.class).obtain();
            collisionShapeTransform.set(model.modelInstance.transform);


            BulletStaticInitializer modelledBulletStaticInitializer = new BulletStaticInitializer(collisionShapeTransform, collisionShape);
            modelledBulletStaticInitializer.initialize(gameEntity);
            gameEntity.adds(sceneComponent);
            engine.addEntity(gameEntity);
        }

        loadBaseBall(engine, 3.02867f);
        loadBaseBall(engine, 4.02867f);
        loadBaseBall(engine, 5.02867f);
        loadAlt(engine);
        loadSensor(engine);
        loadVideoPlane(engine);
        loadCharacter(engine);
        loadMonkey(engine);

    }

    public static void loadBaseBall(Engine engine, float y) {
        BulletModel model = new BulletModel("baseball");
        Scene scene = new Scene(model.sceneAsset.scene);
        GameEntity gameEntity = new GameEntity("baseball");
        SceneComponent sceneComponent = new SceneComponent(scene);


        Vector3 vector = ObjectAllocator.getObject(Vector3.class);
        vector.set(-1.1556f, y, 0.019981f);
        scene.modelInstance.transform.setTranslation(vector);


        btCollisionShape collisionShape = BulletUtils.modelToSphereShape(model.modelInstance.model);
        Matrix4 collisionShapeTransform = ObjectAllocator.getMatrix4();

        collisionShapeTransform.set(scene.modelInstance.transform);
        BulletDynamicInitializer modelledBulletStaticInitializer = new BulletDynamicInitializer(collisionShapeTransform, 0.1f, collisionShape);
        modelledBulletStaticInitializer.rigidBody.setActivationState(Collision.DISABLE_DEACTIVATION);
        modelledBulletStaticInitializer.initialize(gameEntity);
        modelledBulletStaticInitializer.rigidBody.setRestitution(1.0f);
        gameEntity.adds(sceneComponent);
        engine.addEntity(gameEntity);

    }

    public void loadAlt(Engine engine) {
        BulletModel model = new BulletModel("alt");
        GameEntity gameEntity = new GameEntity("alt");


        Matrix4 tranform = ObjectAllocator.getObject(Matrix4.class);
        Vector3 vector = ObjectAllocator.getObject(Vector3.class);
        vector.set(-6.08238f, -0.186796f, 0f);
        Quaternion quaternion = ObjectAllocator.getObject(Quaternion.class);
        Vector3 scale = ObjectAllocator.getObject(Vector3.class);
        tranform.set(vector, quaternion, scale.set(1, 1, 1));


        btCollisionShape collisionShape = BulletUtils.modelToBoxShape(model.modelInstance.model);
        Matrix4 collisionShapeTransform = ObjectAllocator.getMatrix4();

        collisionShapeTransform.set(tranform);
        BulletStaticInitializer modelledBulletStaticInitializer = new BulletStaticInitializer(collisionShapeTransform, collisionShape);
        modelledBulletStaticInitializer.initialize(gameEntity);

        engine.addEntity(gameEntity);

    }

    public void loadCharacter(Engine engine) {


        SceneAsset character = new GLTFLoader().load(Gdx.files.internal(String.format("model/gltf/%s.gltf", "character")));
        Scene scene = new Scene(character.scene);

        GameEntity gameEntity = new GameEntity("character");


        scene.modelInstance.transform.setToTranslation(-1, 0, -1f);
        scene.animationController.setAnimation("idle", 100);

        btCollisionShape collisionShape = BulletUtils.modelToBoxShape(scene.modelInstance.model);
        Matrix4 collisionShapeTransform = ObjectAllocator.getMatrix4();

        collisionShapeTransform.set(scene.modelInstance.transform);
        BulletStaticInitializer modelledBulletStaticInitializer = new BulletStaticInitializer(collisionShapeTransform, collisionShape);
        modelledBulletStaticInitializer.initialize(gameEntity);

        SceneComponent sceneComponent = new SceneComponent(scene);

        gameEntity.adds(sceneComponent);
        engine.addEntity(gameEntity);

    }

    public void loadMonkey(Engine engine) {


        SceneAsset character = new GLTFLoader().load(Gdx.files.internal(String.format("model/gltf/%s.gltf", "monkey")));
        Scene scene = new Scene(character.scene);

        GameEntity gameEntity = new GameEntity("character");


        btCollisionShape collisionShape = BulletUtils.modelToBoxShape(scene.modelInstance.model);
        Matrix4 collisionShapeTransform = ObjectAllocator.getMatrix4();

        scene.modelInstance.transform.setToTranslation(-1, 0, -3);

        collisionShapeTransform.set(scene.modelInstance.transform);
        BulletStaticInitializer modelledBulletStaticInitializer = new BulletStaticInitializer(collisionShapeTransform, collisionShape);
        modelledBulletStaticInitializer.initialize(gameEntity);

        SceneComponent sceneComponent = new SceneComponent(scene);

        gameEntity.adds(sceneComponent);
        engine.addEntity(gameEntity);

    }

    public void loadSensor(Engine engine) {
        GameEntity gameEntity = new GameEntity("sensor") {
            @Override
            public void onCollisionStart(GameEntity entity) {
                Gdx.app.log(TAG, entity.getName() + " started");
            }

            @Override
            public void onCollisionEnd(GameEntity entity) {
                Gdx.app.log(TAG, entity.getName() + " ended");
            }
        };


        Vector3 coord = ObjectAllocator.getObject(Vector3.class);
        Vector3 scale = ObjectAllocator.getObject(Vector3.class);
        Matrix4 tranform = ObjectAllocator.getObject(Matrix4.class);
        Quaternion quaternion = ObjectAllocator.getObject(Quaternion.class);
        tranform.set(coord.set(-1.1556f, 1f, 0.019981f), quaternion, scale.set(0.05f, 0.025f, 0.05f));
        Vector3 boxVector = ObjectAllocator.getObject(Vector3.class);
        btBoxShape boxShape = new btBoxShape(boxVector.set(1, 1, 1));
        BulletSensorInitializer bulletSensorInitializer = new BulletSensorInitializer(tranform, boxShape);

        bulletSensorInitializer.initialize(gameEntity);
        engine.addEntity(gameEntity);

    }

    public void loadCamera(Engine engine) {
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

    public void loadVideoPlane(Engine engine) {
        // setup camera (The BoomBox model is very small so you may need to adapt camera settings for your scene)
        SceneAsset videoPlane = new GLTFLoader().load(Gdx.files.internal(String.format("model/gltf/%s.gltf", "videoPlane")));
        Scene scene = new Scene(videoPlane.scene);
        GameEntity gameEntity = new GameEntity("videoPlane");
        //VideoPlaneComponent sceneComponent = new VideoPlaneComponent(scene);
        //gameEntity.adds(sceneComponent);
        engine.addEntity(gameEntity);

    }

    public void loadSystems(Engine engine) {
        List<EntitySystem> systems = Arrays.asList(
                new ModelInstanceBulletPositionSystem(),
                new ModelInstanceRenderSystem(),
                new GltfRenderSystem(),
                new FirstPersonCameraSystem(),
                new BulletSystem(),
                new GltfBulletPositionSystem(),
                new InputSystem(),
                // new VideoPlayerSystem()
                new DebugBulletSystem(),
                new InGameConsoleSystem()
        );
        for (EntitySystem entitySystem : systems) {
            engine.addSystem(entitySystem);
        }
    }
}
