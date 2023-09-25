package com.rfbsoft.initializer.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.rfbsoft.components.ModelInstanceComponent;
import com.rfbsoft.components.SceneComponent;
import com.rfbsoft.entities.GameEntity;
import com.rfbsoft.initializer.data.BulletDynamicInitializer;
import com.rfbsoft.utils.BulletUtils;
import com.rfbsoft.utils.ObjectAllocator;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.utils.MaterialConverter;

public class CubeMonkeyInitializer extends AbstractInitializer {

    private final String fileName;

    public CubeMonkeyInitializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void initialize(Engine engine) {
        this.initialize(engine, Vector3.Zero);

    }

    @Override
    public void initialize(Engine engine, Vector3 position) {
        SceneAsset character = new GLTFLoader().load(Gdx.files.internal(String.format("model/gltf/%s.gltf", fileName)));
        Scene scene = new Scene(character.scene);

        GameEntity gameEntity = new GameEntity(fileName);

        Vector3 scale = ObjectAllocator.getObject(Vector3.class);
        Matrix4 tranform = ObjectAllocator.getObject(Matrix4.class);
        Quaternion quaternion = ObjectAllocator.getObject(Quaternion.class);
        tranform.set(position.cpy(), quaternion, scale.set(1, 1, 1));

        btCollisionShape collisionShape = BulletUtils.modelToBoxShape(scene.modelInstance.model);
        BulletDynamicInitializer modelledBulletStaticInitializer = new BulletDynamicInitializer(tranform, 0.1f, collisionShape);
        modelledBulletStaticInitializer.rigidBody.setRestitution(1.0f);
        modelledBulletStaticInitializer.initialize(gameEntity);

        SceneComponent sceneComponent = new SceneComponent(scene);

        gameEntity.adds(sceneComponent);
        MaterialConverter.makeCompatible(scene);
        gameEntity.adds(new ModelInstanceComponent(scene.modelInstance));
        modelledBulletStaticInitializer.rigidBody.setUserIndex(GameEntity.addUserIndex(gameEntity));
        engine.addEntity(gameEntity);
    }
}
