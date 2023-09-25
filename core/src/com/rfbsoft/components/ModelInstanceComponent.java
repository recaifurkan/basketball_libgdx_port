package com.rfbsoft.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class ModelInstanceComponent implements Component {
    public final ModelInstance modelInstance;

    public ModelInstanceComponent(ModelInstance modelInstance) {
        this.modelInstance = modelInstance;
    }
}
