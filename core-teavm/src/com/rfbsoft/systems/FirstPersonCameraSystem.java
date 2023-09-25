package com.rfbsoft.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.rfbsoft.entities.StaticObject;

public class FirstPersonCameraSystem extends EntitySystem {

    @Override
    public void update(float deltaTime) {
        StaticObject.firstPersonCameraController.update(deltaTime);
    }
}
