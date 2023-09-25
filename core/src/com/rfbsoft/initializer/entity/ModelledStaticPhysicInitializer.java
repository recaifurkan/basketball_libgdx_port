package com.rfbsoft.initializer.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.rfbsoft.components.ModelInstanceComponent;
import com.rfbsoft.components.SceneComponent;
import com.rfbsoft.entities.GameEntity;
import com.rfbsoft.initializer.data.BulletStaticInitializer;
import com.rfbsoft.initializer.entity.data.BulletModel;
import com.rfbsoft.utils.ObjectAllocator;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.utils.MaterialConverter;

public class ModelledStaticPhysicInitializer extends AbstractInitializer {

    private final String fileName;
    private final BulletModel model;

    public ModelledStaticPhysicInitializer(String fileName) {
        this.fileName = fileName;
        model = new BulletModel(fileName);
    }

    @Override
    public void initialize(Engine engine, Vector3 position) {

        Scene scene = new Scene(model.sceneAsset.scene);
        GameEntity gameEntity = new GameEntity();
        SceneComponent sceneComponent = new SceneComponent(scene);


        btCollisionShape collisionShape = Bullet.obtainStaticNodeShape(model.modelInstance.model.nodes);

        Vector3 scale = ObjectAllocator.getObject(Vector3.class);
        Matrix4 tranform = ObjectAllocator.getObject(Matrix4.class);
        Quaternion quaternion = ObjectAllocator.getObject(Quaternion.class);
        tranform.set(position.cpy(), quaternion, scale.set(1, 1, 1));


        BulletStaticInitializer modelledBulletStaticInitializer = new BulletStaticInitializer(tranform, collisionShape);
        modelledBulletStaticInitializer.initialize(gameEntity);
        gameEntity.adds(sceneComponent);
        MaterialConverter.makeCompatible(scene);
        gameEntity.adds(new ModelInstanceComponent(scene.modelInstance));
        modelledBulletStaticInitializer.rigidBody.setUserIndex(GameEntity.addUserIndex(gameEntity));
        engine.addEntity(gameEntity);
    }
}
