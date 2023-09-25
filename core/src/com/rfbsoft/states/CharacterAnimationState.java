package com.rfbsoft.states;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector3;
import com.rfbsoft.components.AnimationComponent;
import com.rfbsoft.components.BulletRigidBodyComponent;
import com.rfbsoft.entities.GameEntity;

public enum CharacterAnimationState implements State<GameEntity> {
    IDLE("Armature|idle") {
        @Override
        public void enter(GameEntity entity) {
            AnimationComponent animationComponent = mapper.get(entity);
            animationComponent.animationController.animate(this.animationName, -1, null, 0.5f);
            BulletRigidBodyComponent bulletRigidBodyComponent = collisionMapper.get(entity);
            bulletRigidBodyComponent.rigidBody.setAngularVelocity(Vector3.Zero);
        }
    },
    WALK("Armature|walk") {
        @Override
        public void enter(GameEntity entity) {
            AnimationComponent animationComponent = mapper.get(entity);
            animationComponent.animationController.animate(this.animationName, -1, null, 0.5f);
        }

        @Override
        public void update(GameEntity entity) {

//            BulletCollisionComponent collisionComponent = collisionMapper.get(entity);
//
//
//            btRigidBody rigidBody = (btRigidBody) collisionComponent.collisionObject;
//            Vector3 forward = ObjectAllocator.getObject(Vector3.class);
//
//            Matrix4 worldTransform = rigidBody.getWorldTransform();
//
//            rigidBody.translate(forward.set(0, 0, -walkSpeed).rot(worldTransform));
//            ObjectAllocator.freeAllInPool();


        }
    };


    ComponentMapper<AnimationComponent> mapper = ComponentMapper.getFor(AnimationComponent.class);
    ComponentMapper<BulletRigidBodyComponent> collisionMapper = ComponentMapper.getFor(BulletRigidBodyComponent.class);

    String animationName;
    float walkSpeed = 0.2f;


    CharacterAnimationState(String animationName) {
        this.animationName = animationName;
    }

    @Override
    public void update(GameEntity entity) {

    }

    @Override
    public void exit(GameEntity entity) {

    }

    @Override
    public boolean onMessage(GameEntity entity, Telegram telegram) {
        return false;
    }
}
