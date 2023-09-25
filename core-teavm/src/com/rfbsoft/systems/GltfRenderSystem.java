package com.rfbsoft.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Disposable;
import com.rfbsoft.components.RenderableComponent;
import com.rfbsoft.components.SceneComponent;
import com.rfbsoft.components.VideoPlaneComponent;
import com.rfbsoft.entities.StaticObject;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRFloatAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.lights.DirectionalShadowLight;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

public class GltfRenderSystem extends EntitySystem implements EntityListener, Disposable {
    private SceneManager sceneManager;
    private Cubemap diffuseCubemap;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private Texture brdfLUT;
    private SceneSkybox skybox;
    private DirectionalLightEx light;

    private Camera camera = StaticObject.gameCamera;

    public static final int[] SHADOW_SIZES = {512, 1024, 2048, 4096};

    public int shadowQuality = 0;


    DirectionalShadowLight sunLight;


    public GltfRenderSystem() {


        sceneManager = new SceneManager();
        // setup light
        light = new DirectionalLightEx();
        light.direction.set(0, -1, 0).nor();
        light.color.set(Color.YELLOW);

        // setup quick IBL (image based lighting)
        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        environmentCubemap = iblBuilder.buildEnvMap(1024);
        diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        specularCubemap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();

        // This texture is provided by the library, no need to have it in your assets.
        brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));


        sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));
        sceneManager.environment.set(new PBRFloatAttribute(PBRFloatAttribute.ShadowBias, 0.01f));
        sceneManager.setAmbientLight(0.8f);

        sunLight = new DirectionalShadowLight(SHADOW_SIZES[shadowQuality], SHADOW_SIZES[shadowQuality]);
        sunLight.intensity = 7;
        sunLight.updateColor();
        float s = 100;
        BoundingBox bbox = new BoundingBox(new Vector3(-s, -s, -s), new Vector3(s, s, s));
        sunLight.setBounds(bbox);
        sunLight.setCenter(camera.position.cpy().mulAdd(camera.direction, s));
        sunLight.direction.set(2, -3, 2).nor();
        sunLight.baseColor.set(Color.WHITE);
        sunLight.intensity = 7;
        sunLight.updateColor();
        sceneManager.environment.add(sunLight);


        // setup skybox
        skybox = new SceneSkybox(environmentCubemap);
        sceneManager.setSkyBox(skybox);
    }

    @Override
    public void addedToEngine(Engine engine) {
        sceneManager.setCamera(camera);
        Family family = Family.one(SceneComponent.class).get();
        engine.addEntityListener(family, this);
        ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.one(SceneComponent.class).get());
        for (Entity entity :
                entities) {
            sceneManager.addScene(entity.getComponent(SceneComponent.class).scene);
        }
    }


    @Override
    public void update(float deltaTime) {


        sceneManager.update(deltaTime);
        sceneManager.render();
    }

    @Override
    public void dispose() {
        sceneManager.dispose();
        environmentCubemap.dispose();
        diffuseCubemap.dispose();
        specularCubemap.dispose();
        brdfLUT.dispose();
        skybox.dispose();
    }

    @Override
    public void entityAdded(Entity entity) {
        SceneComponent renderComponent = entity.getComponent(SceneComponent.class);
        sceneManager.addScene(renderComponent.getScene());

    }

    @Override
    public void entityRemoved(Entity entity) {
        RenderableComponent renderComponent = entity.getComponent(SceneComponent.class);
        sceneManager.removeScene(renderComponent.getScene());
    }
}
