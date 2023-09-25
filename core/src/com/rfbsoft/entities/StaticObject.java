package com.rfbsoft.entities;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;

public class StaticObject {
    public static Camera gameCamera;
    public static Engine gameEngine;

    public static FirstPersonCameraController firstPersonCameraController;
    public static btDiscreteDynamicsWorld gamePhysicWorld;
}
