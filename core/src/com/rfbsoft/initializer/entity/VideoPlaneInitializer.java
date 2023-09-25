package com.rfbsoft.initializer.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.rfbsoft.components.ModelInstanceComponent;
import com.rfbsoft.components.VideoPlaneComponent;
import com.rfbsoft.entities.GameEntity;

public class VideoPlaneInitializer extends AbstractInitializer {
    private final static String TAG = VideoPlaneInitializer.class.getName();

    private final String fileName;

    private final Model model;

    public VideoPlaneInitializer(String fileName) {
        this.fileName = fileName;
        model = new ObjLoader().loadModel(Gdx.files.internal(String.format("model/obj/%s.obj", fileName)));
    }

    @Override
    public void initialize(Engine engine) {
        GameEntity gameEntity = new GameEntity(fileName);
        ModelInstance modelInstance = new ModelInstance(model);
        VideoPlaneComponent sceneComponent = new VideoPlaneComponent("video");
        ModelInstanceComponent modelInstanceComponent = new ModelInstanceComponent(modelInstance);
        gameEntity.adds(sceneComponent, modelInstanceComponent);
        engine.addEntity(gameEntity);
    }
}
