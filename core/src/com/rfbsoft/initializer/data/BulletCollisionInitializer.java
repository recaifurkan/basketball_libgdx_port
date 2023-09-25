package com.rfbsoft.initializer.data;


import com.rfbsoft.components.BulletCollisionComponent;
import com.rfbsoft.entities.GameEntity;

public class BulletCollisionInitializer extends BulletInitilizer {

    public BulletCollisionComponent collisionComponent;

    public BulletCollisionInitializer() {

        collisionComponent = new BulletCollisionComponent();
    }


    @Override
    public void initialize(GameEntity object) {
        object.add(collisionComponent);
    }
}
