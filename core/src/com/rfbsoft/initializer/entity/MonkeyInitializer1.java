package com.rfbsoft.initializer.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.rfbsoft.components.SceneComponent;
import com.rfbsoft.entities.GameEntity;
import com.rfbsoft.initializer.data.BulletDynamicInitializer;
import com.rfbsoft.utils.BulletUtils;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class MonkeyInitializer1 extends AbstractInitializer {

    private final String fileName;

    public MonkeyInitializer1(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void initialize(Engine engine) {
        SceneAsset character = new GLTFLoader().load(Gdx.files.internal(String.format("model/gltf/%s.gltf", fileName)));
        Scene scene = new Scene(character.scene);

        GameEntity gameEntity = new GameEntity(fileName);

        btCollisionShape collisionShape = BulletUtils.createBvhTriangleMeshShapeWithoutScale(scene.modelInstance.model.meshParts);
        BulletDynamicInitializer modelledBulletStaticInitializer = new BulletDynamicInitializer(scene.modelInstance.transform, 0.1f, collisionShape);
        modelledBulletStaticInitializer.initialize(gameEntity);
        modelledBulletStaticInitializer.rigidBody.setActivationState(Collision.DISABLE_DEACTIVATION);
        modelledBulletStaticInitializer.rigidBody.setRestitution(1.0f);

        SceneComponent sceneComponent = new SceneComponent(scene);

        gameEntity.adds(sceneComponent);
        engine.addEntity(gameEntity);
    }
}
