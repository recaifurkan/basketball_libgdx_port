package com.rfbsoft.components;

import net.mgsx.gltf.scene3d.scene.Scene;

public class SceneComponent implements RenderableComponent {
    public final Scene scene;

    public SceneComponent(Scene scene) {
        this.scene = scene;
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
