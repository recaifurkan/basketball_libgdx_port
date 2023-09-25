package com.rfbsoft.initializer.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Vector3;

public abstract class AbstractInitializer implements Initializer {

    @Override
    public void initialize(Engine engine) {

    }

    @Override
    public void initialize(Engine engine, Vector3 position) {

    }
}
