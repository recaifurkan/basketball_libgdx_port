package com.rfbsoft.initializer.entity.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class BulletModel {
    public SceneAsset sceneAsset;
    public ModelInstance modelInstance;

    public BulletModel(String fileName) {
        Model model = new ObjLoader().loadModel(Gdx.files.internal(String.format("model/obj/%s.obj", fileName)));
        sceneAsset = new GLTFLoader().load(Gdx.files.internal(String.format("model/gltf/%s.gltf", fileName)));
        modelInstance = new ModelInstance(model);
    }
}