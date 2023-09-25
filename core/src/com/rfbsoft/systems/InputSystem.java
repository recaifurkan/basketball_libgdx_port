package com.rfbsoft.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.rfbsoft.components.BulletRigidBodyComponent;
import com.rfbsoft.entities.GameEntity;
import com.rfbsoft.entities.StaticObject;

import static com.rfbsoft.initializer.Init.SENSOR_POSITION;
import static com.rfbsoft.utils.UtilMethods.calculateInitialVelocity;

public class InputSystem extends EntitySystem {

    private static Vector3 position = new Vector3();

    private final ComponentMapper<BulletRigidBodyComponent> rigidMapper = ComponentMapper.getFor(BulletRigidBodyComponent.class);


    private Camera camera = StaticObject.gameCamera;

    public InputSystem() {

    }

    @Override
    public void addedToEngine(Engine engine) {
    }

    @Override
    public void update(float deltaTime) {
        if (Gdx.input.justTouched()) {
            GameEntity ray = BulletSystem.ray(Gdx.input.getX(), Gdx.input.getY());
            if (ray != null) {
                if (ray.getName().startsWith("baseball")) {
                    System.out.println(ray);
                    btRigidBody rigidBody1 = rigidMapper.get(ray).rigidBody;
                    Vector3 velocity = calculateInitialVelocity(rigidBody1.getWorldTransform().getTranslation(position), SENSOR_POSITION.cpy());
                    if (Float.isNaN(velocity.x) || Float.isNaN(velocity.y) || Float.isNaN(velocity.z)) {
                        return;
                    }
                    rigidBody1.setLinearVelocity(velocity);

                }

            }
        }
    }


    public static class UserInput implements InputProcessor {


        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }
}
