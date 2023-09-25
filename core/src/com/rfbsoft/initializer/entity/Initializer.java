package com.rfbsoft.initializer.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Vector3;

public interface Initializer {
    void initialize(Engine engine);

    void initialize(Engine engine, Vector3 position);
}
