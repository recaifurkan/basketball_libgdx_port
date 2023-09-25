package com.rfbsoft.initializer.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.rfbsoft.entities.GameEntity;
import com.rfbsoft.initializer.data.BulletStaticInitializer;
import com.rfbsoft.initializer.entity.data.BulletModel;
import com.rfbsoft.utils.BulletUtils;
import com.rfbsoft.utils.ObjectAllocator;

public class ModelledStaticInitializer extends AbstractInitializer {

    private final String fileName;
    private Vector3 position;

    public ModelledStaticInitializer(String fileName, Vector3 position) {
        this.fileName = fileName;
        this.position = position;
    }

    public ModelledStaticInitializer(String fileName) {
        this(fileName, null);
    }


    @Override
    public void initialize(Engine engine) {
        BulletModel model = new BulletModel(fileName);
        GameEntity gameEntity = new GameEntity(fileName);

        btCollisionShape collisionShape = BulletUtils.modelToBoxShape(model.modelInstance.model);
        Matrix4 collisionShapeTransform = ObjectAllocator.getMatrix4();


        if (position != null) {
            Matrix4 tranform = ObjectAllocator.getObject(Matrix4.class);
            Quaternion quaternion = ObjectAllocator.getObject(Quaternion.class);
            Vector3 scale = ObjectAllocator.getObject(Vector3.class);
            tranform.set(position, quaternion, scale.set(1, 1, 1));
            collisionShapeTransform.set(tranform);
        }
        else{
            collisionShapeTransform.set(model.modelInstance.transform);
        }

        BulletStaticInitializer modelledBulletStaticInitializer = new BulletStaticInitializer(collisionShapeTransform, collisionShape);
        modelledBulletStaticInitializer.initialize(gameEntity);
        engine.addEntity(gameEntity);
    }
}
