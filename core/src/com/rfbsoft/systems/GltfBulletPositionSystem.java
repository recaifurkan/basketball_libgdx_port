package com.rfbsoft.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.rfbsoft.components.BulletRigidBodyComponent;
import com.rfbsoft.components.SceneComponent;

public class GltfBulletPositionSystem extends EntitySystem  {


    private final ComponentMapper<SceneComponent> modelMapper = ComponentMapper.getFor(SceneComponent.class);
    private final ComponentMapper<BulletRigidBodyComponent> rigidBodyMapper = ComponentMapper.getFor(BulletRigidBodyComponent.class);
    private ImmutableArray<Entity> entities;





    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(SceneComponent.class,BulletRigidBodyComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            SceneComponent modelComponent = modelMapper.get(entity);
            BulletRigidBodyComponent bulletRigidBodyComponent = rigidBodyMapper.get(entity);
            bulletRigidBodyComponent.rigidBody.getWorldTransform(modelComponent.scene.modelInstance.transform);
        }
    }


}
