package com.rfbsoft.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.rfbsoft.components.ModelInstanceComponent;
import com.rfbsoft.entities.StaticObject;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;

public class ModelInstanceRenderSystem extends EntitySystem implements Disposable {


    private final ComponentMapper<ModelInstanceComponent> modelMapper = ComponentMapper.getFor(ModelInstanceComponent.class);

    private ImmutableArray<Entity> entities;

    private Camera camera = StaticObject.gameCamera;

    private final ModelBatch modelBatch;
    private final Environment environment;

    DirectionalShadowLight shadowLight;

    public ModelInstanceRenderSystem() {
        modelBatch = new ModelBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
        environment.add(new DirectionalLight().set(0.9f, 0.9f, 0.9f, 20f, 20f, 20f));
        environment.add(new PointLight().set(1, 1, 1, 20, 20, 20, 500));
        //environment.add((shadowLight = new DirectionalShadowLight(1024, 1024, 60f, 60f, .1f, 50f)).set(1f, 1f, 1f, 40.0f, -35f, -35f));
        //environment.shadowMap = shadowLight;



    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(ModelInstanceComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        modelBatch.begin(camera);
        for (Entity entity : entities) {
            ModelInstanceComponent modelComponent = modelMapper.get(entity);
            modelBatch.render(modelComponent.modelInstance, environment);
        }
        modelBatch.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
    }
}
