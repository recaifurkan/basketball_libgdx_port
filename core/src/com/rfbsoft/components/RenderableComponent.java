package com.rfbsoft.components;

import com.badlogic.ashley.core.Component;
import net.mgsx.gltf.scene3d.scene.Scene;

public interface RenderableComponent extends Component {
    Scene getScene();
}
