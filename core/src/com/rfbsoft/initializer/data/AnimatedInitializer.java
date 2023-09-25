package com.rfbsoft.initializer.data;


import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.rfbsoft.components.AnimationComponent;
import com.rfbsoft.entities.GameEntity;
import com.rfbsoft.states.CharacterAnimationState;


public class AnimatedInitializer extends IEntityInitializer {


    public AnimationComponent animationComponent;
    public AnimationComponent.AnimationStateComponent animationStateComponent;


    public AnimatedInitializer(ModelInstance instance, StateMachine stateMachine) {


        animationComponent = new AnimationComponent(new AnimationController(instance));
        animationStateComponent = new AnimationComponent.AnimationStateComponent(
                stateMachine
        );

    }


    @Override
    public void initialize(GameEntity object) {
        object.add(animationComponent);
        object.add(animationStateComponent);

    }
}
