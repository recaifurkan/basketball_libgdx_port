package com.rfbsoft.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.rfbsoft.components.BulletRigidBodyComponent;
import com.rfbsoft.components.ModelInstanceComponent;

public class ModelInstanceBulletPositionSystem extends EntitySystem {


    private final ComponentMapper<ModelInstanceComponent> modelMapper = ComponentMapper.getFor(ModelInstanceComponent.class);
    private final ComponentMapper<BulletRigidBodyComponent> rigidBodyMapper = ComponentMapper.getFor(BulletRigidBodyComponent.class);
    private ImmutableArray<Entity> entities;

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(ModelInstanceComponent.class, BulletRigidBodyComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            ModelInstanceComponent modelComponent = modelMapper.get(entity);
            BulletRigidBodyComponent bulletRigidBodyComponent = rigidBodyMapper.get(entity);
            bulletRigidBodyComponent.rigidBody.getWorldTransform(modelComponent.modelInstance.transform);
        }
    }


}
