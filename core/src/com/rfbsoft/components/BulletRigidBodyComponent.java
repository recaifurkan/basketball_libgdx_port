package com.rfbsoft.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class BulletRigidBodyComponent implements Component {
    public btRigidBody rigidBody;

    public BulletRigidBodyComponent(btRigidBody rigidBody) {
        this.rigidBody = rigidBody;
    }
}
