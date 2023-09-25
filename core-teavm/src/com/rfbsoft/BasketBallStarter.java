package com.rfbsoft;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.rfbsoft.entities.StaticObject;
import com.rfbsoft.initializer.Init;

public class BasketBallStarter extends ApplicationAdapter {


    public BasketBallStarter() {
        Bullet.init();
    }

    @Override
    public void create() {
        final Engine engine = new Engine();
        StaticObject.gameEngine = engine;
        Init init = new Init();
        init.loadCamera();
        init.loadSystems(engine);
        init.loadGameEntities(engine);
    }

    @Override
    public void render() {
        //Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        float deltaTime = Gdx.graphics.getDeltaTime();
        StaticObject.gameEngine.update(deltaTime);

    }

    @Override
    public void dispose() {

    }
}
