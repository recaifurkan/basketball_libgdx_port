package com.rfbsoft.initializer.data;


import com.rfbsoft.entities.GameEntity;

public abstract class IEntityInitializer {
    static void initialize(GameEntity object, IEntityInitializer... initializors) {
        for (IEntityInitializer initializor : initializors) {
            initializor.initialize(object);
        }
    }

    public abstract void initialize(GameEntity object);

}
