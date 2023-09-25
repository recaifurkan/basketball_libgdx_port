package com.rfbsoft.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.video.VideoPlayer;
import com.rfbsoft.components.ModelInstanceComponent;
import com.rfbsoft.components.VideoPlaneComponent;

public class VideoPlayerSystem extends EntitySystem {
    private final ComponentMapper<VideoPlaneComponent> videoPlaneComponentComponentMapper = ComponentMapper.getFor(VideoPlaneComponent.class);
    private final ComponentMapper<ModelInstanceComponent> modelInstanceComponentComponentMapper = ComponentMapper.getFor(ModelInstanceComponent.class);
    private ImmutableArray<Entity> entities;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(VideoPlaneComponent.class, ModelInstanceComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            VideoPlaneComponent videoPlaneComponent = videoPlaneComponentComponentMapper.get(entity);
            ModelInstanceComponent modelComponent = modelInstanceComponentComponentMapper.get(entity);
            VideoPlayer videoPlayer = videoPlaneComponent.videoPlayer;
            videoPlayer.update();
            if (videoPlayer.isBuffered()) {
                Texture frame = videoPlayer.getTexture();
                for (Material material : modelComponent.modelInstance.materials) {
                    TextureAttribute attribute = material.get(TextureAttribute.class, TextureAttribute.Diffuse);
                    attribute.textureDescription.texture = frame;
                }
            }
        }
    }
}
