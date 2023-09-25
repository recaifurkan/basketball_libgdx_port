package com.rfbsoft.initializer.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.rfbsoft.entities.GameEntity;
import com.rfbsoft.initializer.data.BulletSensorInitializer;
import com.rfbsoft.utils.BulletUtils;
import com.rfbsoft.utils.ObjectAllocator;

public class SensorInitializer extends AbstractInitializer {

    private final String fileName;
    private final Model model;

    public SensorInitializer(String fileName) {
        this.fileName = fileName;
        model = new ObjLoader().loadModel(Gdx.files.internal(String.format("model/obj/%s.obj", fileName)));
    }

    private final static String TAG = SensorInitializer.class.getName();


    @Override
    public void initialize(Engine engine, Vector3 position) {

        GameEntity gameEntity = new GameEntity(fileName) {
            @Override
            public void onCollisionStart(GameEntity entity) {
                Gdx.app.log(TAG,
                        entity.getName() + " started");
            }

            @Override
            public void onCollisionEnd(GameEntity entity) {
                Gdx.app.log(TAG, entity.getName() + " ended");
            }
        };
        btCollisionShape collisionShape = BulletUtils.modelToBoxShape(model);


        Vector3 scale = ObjectAllocator.getObject(Vector3.class);
        Matrix4 tranform = ObjectAllocator.getObject(Matrix4.class);
        Quaternion quaternion = ObjectAllocator.getObject(Quaternion.class);
        tranform.set(position.cpy(), quaternion, scale.set(1, 1, 1));


        BulletSensorInitializer bulletSensorInitializer = new BulletSensorInitializer(tranform, collisionShape);
        bulletSensorInitializer.initialize(gameEntity);
        bulletSensorInitializer.collisionComponent.collisionObject.setUserIndex(GameEntity.addUserIndex(gameEntity));
        engine.addEntity(gameEntity);
    }
}
