package com.rfbsoft.initializer.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.rfbsoft.components.ModelInstanceComponent;
import com.rfbsoft.components.SceneComponent;
import com.rfbsoft.entities.GameEntity;
import com.rfbsoft.initializer.data.BulletDynamicInitializer;
import com.rfbsoft.initializer.entity.data.BulletModel;
import com.rfbsoft.utils.BulletUtils;
import com.rfbsoft.utils.ObjectAllocator;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.utils.MaterialConverter;

public class ModelledKinematicPhysicsInitializer extends AbstractInitializer {

    private final String fileName;
    private final BulletModel model;


    public ModelledKinematicPhysicsInitializer(String fileName) {
        this.fileName = fileName;
        model = new BulletModel(fileName);
    }

    @Override
    public void initialize(Engine engine) {
        this.initialize(engine, new Vector3(0, 10, 0));
    }

    @Override
    public void initialize(Engine engine, Vector3 position) {
        Scene scene = new Scene(model.sceneAsset.scene);
        GameEntity gameEntity = new GameEntity(fileName);
        SceneComponent sceneComponent = new SceneComponent(scene);


        Vector3 scale = ObjectAllocator.getObject(Vector3.class);
        Matrix4 tranform = ObjectAllocator.getObject(Matrix4.class);
        Quaternion quaternion = ObjectAllocator.getObject(Quaternion.class);
        tranform.set(position.cpy(), quaternion, scale.set(1, 1, 1));


        btCollisionShape collisionShape = BulletUtils.modelToSphereShape(model.modelInstance.model);

        BulletDynamicInitializer modelledBulletStaticInitializer = new BulletDynamicInitializer(tranform, 0.1f, collisionShape);
        modelledBulletStaticInitializer.rigidBody.setActivationState(Collision.DISABLE_DEACTIVATION);
        modelledBulletStaticInitializer.rigidBody.setRestitution(1.0f);
        modelledBulletStaticInitializer.initialize(gameEntity);
        gameEntity.adds(sceneComponent);
        MaterialConverter.makeCompatible(scene);
        gameEntity.adds(new ModelInstanceComponent(scene.modelInstance));
        modelledBulletStaticInitializer.rigidBody.setUserIndex(GameEntity.addUserIndex(gameEntity));
        engine.addEntity(gameEntity);
    }
}
