package com.rfbsoft.initializer.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.JsonReader;
import com.rfbsoft.components.ModelInstanceComponent;
import com.rfbsoft.entities.GameEntity;
import com.rfbsoft.initializer.data.AnimatedInitializer;
import com.rfbsoft.initializer.data.BulletDynamicInitializer;
import com.rfbsoft.states.CharacterAnimationState;
import com.rfbsoft.utils.BulletUtils;

public class G3djCharacterAnimatedModelInitializer extends AbstractInitializer {

    private final String fileName;
    private final Model model;

    public G3djCharacterAnimatedModelInitializer(String fileName) {
        this.fileName = fileName;
        model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal(String.format("model/g3dj/%s.g3dj", fileName)));

    }

    @Override
    public void initialize(Engine engine) {
        this.initialize(engine, Vector3.Zero);
    }

    @Override
    public void initialize(Engine engine, Vector3 position) {
        ModelInstance modelInstance = new ModelInstance(model);


        GameEntity gameEntity = new GameEntity(fileName);

        modelInstance.transform.setToTranslation(position);

        btCollisionShape collisionShape = BulletUtils.modelToBoxShape(modelInstance.model);
        BulletDynamicInitializer modelledBulletStaticInitializer = new BulletDynamicInitializer(modelInstance.transform, 0.1f, collisionShape);
        modelledBulletStaticInitializer.rigidBody.setRestitution(0.6f);
        modelledBulletStaticInitializer.initialize(gameEntity);
        AnimatedInitializer animatedInitializer = new AnimatedInitializer(modelInstance, new DefaultStateMachine(gameEntity));
        animatedInitializer.initialize(gameEntity);
        animatedInitializer.animationStateComponent.stateMachine.changeState(CharacterAnimationState.WALK);
        ModelInstanceComponent modelInstanceComponent = new ModelInstanceComponent(modelInstance);


        gameEntity.adds(modelInstanceComponent);
        modelledBulletStaticInitializer.rigidBody.setUserIndex(GameEntity.addUserIndex(gameEntity));
        engine.addEntity(gameEntity);
    }
}
