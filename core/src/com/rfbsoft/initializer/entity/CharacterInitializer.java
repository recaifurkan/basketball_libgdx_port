package com.rfbsoft.initializer.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.rfbsoft.components.SceneComponent;
import com.rfbsoft.entities.GameEntity;
import com.rfbsoft.initializer.data.BulletStaticInitializer;
import com.rfbsoft.utils.BulletUtils;
import com.rfbsoft.utils.ObjectAllocator;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class CharacterInitializer extends AbstractInitializer {
    @Override
    public void initialize(Engine engine) {
        SceneAsset character = new GLTFLoader().load(Gdx.files.internal(String.format("model/gltf/%s.gltf", "character")));
        Scene scene = new Scene(character.scene);

        GameEntity gameEntity = new GameEntity("character");


        scene.modelInstance.transform.setToTranslation(-1, 0, -1f);
        scene.animationController.setAnimation("idle", 100);

        btCollisionShape collisionShape = BulletUtils.modelToBoxShape(scene.modelInstance.model);
        Matrix4 collisionShapeTransform = ObjectAllocator.getMatrix4();

        collisionShapeTransform.set(scene.modelInstance.transform);
        BulletStaticInitializer modelledBulletStaticInitializer = new BulletStaticInitializer(collisionShapeTransform, collisionShape);
        modelledBulletStaticInitializer.initialize(gameEntity);

        SceneComponent sceneComponent = new SceneComponent(scene);

        gameEntity.adds(sceneComponent);
        engine.addEntity(gameEntity);
    }
}
