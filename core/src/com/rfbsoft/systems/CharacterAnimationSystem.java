package com.rfbsoft.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.rfbsoft.components.AnimationComponent;
import com.rfbsoft.components.BulletRigidBodyComponent;
import com.rfbsoft.entities.GameEntity;
import com.rfbsoft.states.CharacterAnimationState;
import com.rfbsoft.utils.BulletUtils;

public class CharacterAnimationSystem extends EntitySystem {
    final float animationTime = 5;
    private final ComponentMapper<AnimationComponent.AnimationStateComponent> modelMapper =
            ComponentMapper.getFor(AnimationComponent.AnimationStateComponent.class);

    private final ComponentMapper<BulletRigidBodyComponent> collisionMapper =
            ComponentMapper.getFor(BulletRigidBodyComponent.class);
    float timeElapsed;
    private ImmutableArray<Entity> entities;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(AnimationComponent.AnimationStateComponent.class,
                BulletRigidBodyComponent.class).get());

    }

    @Override
    public void update(float deltaTime) {
//        timeElapsed += deltaTime;
//        if(timeElapsed < animationTime)return;
//        timeElapsed = 0;
        for (Entity entity : entities) {
            if (entity instanceof GameEntity) {
                AnimationComponent.AnimationStateComponent animationStateComponent = modelMapper.get(entity);
                BulletRigidBodyComponent bulletCollisionComponent = collisionMapper.get(entity);
                btRigidBody rigidBody = bulletCollisionComponent.rigidBody;
                /*
                Aşağıda bakıyoruz
                hızı sıfır mı diye
                hızı sıfır değilse ve durur vaziyetteyse animasyon durumunu yürüyor yapıyoruz
                hızı sıfırsa ve yürüyorsa durur vaziyete getiriyoruz
                 */
                Vector3 linVel = rigidBody.getLinearVelocity();
                if (!linVel.isZero(BulletUtils.getZeroLinearSpeedThreshold())) {
                    if (CharacterAnimationState.IDLE.equals(animationStateComponent.stateMachine.getCurrentState()))
                        animationStateComponent.stateMachine.changeState(CharacterAnimationState.WALK);
                } else {
                    if (CharacterAnimationState.WALK.equals(animationStateComponent.stateMachine.getCurrentState()))
                        animationStateComponent.stateMachine.changeState(CharacterAnimationState.IDLE);

                }


            }
        }
    }
}
