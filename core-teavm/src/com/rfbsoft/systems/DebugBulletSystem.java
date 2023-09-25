package com.rfbsoft.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Disposable;
import com.rfbsoft.entities.StaticObject;

public class DebugBulletSystem extends EntitySystem implements Disposable {
    private final DebugDrawer debugDrawer;

    public DebugBulletSystem() {
        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        StaticObject.gamePhysicWorld.setDebugDrawer(debugDrawer);
    }

    @Override
    public void addedToEngine(Engine engine) {
    }

    @Override
    public void update(float deltaTime) {
        debugDrawer.begin(StaticObject.gameCamera);
        StaticObject.gamePhysicWorld.debugDrawWorld();
        debugDrawer.end();
    }


    @Override
    public void dispose() {
        debugDrawer.dispose();
    }
}
