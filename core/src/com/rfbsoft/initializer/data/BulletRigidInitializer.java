package com.rfbsoft.initializer.data;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.rfbsoft.components.BulletCollisionComponent;
import com.rfbsoft.components.BulletRigidBodyComponent;
import com.rfbsoft.entities.GameEntity;

public class BulletRigidInitializer extends BulletInitilizer {

    public btRigidBody rigidBody;
    protected BulletRigidBodyComponent bulletRigidBodyComponent;

    public BulletRigidInitializer(Matrix4 transform, float mass, btCollisionShape collisionShape) {
        Vector3 localInertia = new Vector3();

        if (mass > 0f) {
            collisionShape.calculateLocalInertia(mass, localInertia);
        } else {
            localInertia.set(0, 0, 0);
        }
        BulletCollisionComponent.MotionState motionState = new BulletCollisionComponent.MotionState(transform);
        btRigidBody.btRigidBodyConstructionInfo bodyInfo = new
                btRigidBody.btRigidBodyConstructionInfo(mass, motionState, collisionShape, localInertia);
        rigidBody = new btRigidBody(bodyInfo);
        bulletRigidBodyComponent = new BulletRigidBodyComponent(rigidBody);
    }

    @Override
    public void initialize(GameEntity object) {
        rigidBody.userData = object;
        object.add(bulletRigidBodyComponent);
    }
}
