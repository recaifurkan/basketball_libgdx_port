package com.rfbsoft.initializer.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.rfbsoft.components.ModelInstanceComponent;
import com.rfbsoft.components.SceneComponent;
import com.rfbsoft.entities.GameEntity;
import com.rfbsoft.initializer.data.BulletStaticInitializer;
import com.rfbsoft.initializer.entity.data.BulletModel;
import com.rfbsoft.utils.BulletUtils;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.utils.MaterialConverter;

public class CubeTerrainInitializer extends AbstractInitializer {

    private final String fileName;
    private final BulletModel model;

    public CubeTerrainInitializer(String fileName) {
        this.fileName = fileName;
        model = new BulletModel(fileName);
    }

    @Override
    public void initialize(Engine engine) {
        this.initialize(engine, Vector3.Zero);
    }

    @Override
    public void initialize(Engine engine, Vector3 position) {
        Scene scene = new Scene(model.sceneAsset.scene);

        GameEntity gameEntity = new GameEntity(fileName);

        scene.modelInstance.transform.setToTranslation(position);


        btCollisionShape collisionShape = BulletUtils.modelToBoxShape(model.modelInstance.model);
        BulletStaticInitializer modelledBulletStaticInitializer = new BulletStaticInitializer(scene.modelInstance.transform, collisionShape);
        modelledBulletStaticInitializer.rigidBody.setRestitution(0.6f);
        modelledBulletStaticInitializer.initialize(gameEntity);

        SceneComponent sceneComponent = new SceneComponent(scene);

        gameEntity.adds(sceneComponent);
        MaterialConverter.makeCompatible(scene);
        gameEntity.adds(new ModelInstanceComponent(scene.modelInstance));
        modelledBulletStaticInitializer.rigidBody.setUserIndex(GameEntity.addUserIndex(gameEntity));
        engine.addEntity(gameEntity);
    }
}
