package com.rfbsoft.initializer.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.rfbsoft.components.SceneComponent;
import com.rfbsoft.entities.GameEntity;
import com.rfbsoft.initializer.data.BulletStaticInitializer;
import com.rfbsoft.initializer.entity.data.BulletModel;
import net.mgsx.gltf.scene3d.scene.Scene;

public class TerrainInitializer extends AbstractInitializer {

    private final String fileName;
    private final BulletModel model;

    public TerrainInitializer(String fileName) {
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


        btCollisionShape collisionShape = Bullet.obtainStaticNodeShape(model.modelInstance.model.nodes);
        BulletStaticInitializer modelledBulletStaticInitializer = new BulletStaticInitializer(scene.modelInstance.transform, collisionShape);
        modelledBulletStaticInitializer.rigidBody.setRestitution(0.6f);
        modelledBulletStaticInitializer.initialize(gameEntity);

        SceneComponent sceneComponent = new SceneComponent(scene);

        gameEntity.adds(sceneComponent);
        engine.addEntity(gameEntity);
    }
}
