package com.rfbsoft.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.dynamics.btRaycastVehicle;
import com.badlogic.gdx.utils.Disposable;
import com.rfbsoft.components.VehicleComponent;
import com.rfbsoft.entities.StaticObject;

/**
 * Bullet btRaycastVehicle araba simülasyonunu yöneten sistem.
 *
 * Kontroller:
 *   YUKARı / ASAGI ok  → gaz / fren
 *   SOL / SAG ok       → direksiyon
 */
public class VehicleSystem extends EntitySystem implements Disposable {

    private static final float ENGINE_FORCE   = 2000f;
    private static final float REVERSE_FORCE  = 1200f;
    private static final float BRAKE_FORCE    = 150f;
    private static final float MAX_STEERING   = 0.5f;
    private static final float STEERING_SPEED = 1.5f;
    /** m/s altında araç "durmuş" sayılır → geri vitese geçilir */
    private static final float REVERSE_SPEED_THRESHOLD = 0.5f;

    private final ComponentMapper<VehicleComponent> vehicleMapper =
            ComponentMapper.getFor(VehicleComponent.class);

    private ImmutableArray<Entity> entities;

    private final ModelBatch modelBatch;
    private final Environment environment;
    private final Camera camera;

    // Tekerlek modelini Y ekseni → X ekseni hizalamak için kalıcı offset matrisi
    private final Matrix4 wheelOffset = new Matrix4().rotate(0f, 0f, 1f, 90f);

    private float steeringAngle = 0f;

    public VehicleSystem() {
        modelBatch = new ModelBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
        environment.add(new DirectionalLight().set(0.9f, 0.9f, 0.9f, 20f, 20f, 20f));

        camera = StaticObject.gameCamera;
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(VehicleComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        modelBatch.begin(camera);

        for (Entity entity : entities) {
            VehicleComponent vc = vehicleMapper.get(entity);
            btRaycastVehicle vehicle = vc.vehicle;

            // ----- GİRİŞ -----
            float engineForce = 0f;
            float brakeForce  = 0f;

            // Aracın ilerleme hızı (km/h → m/s için km/h * 1000/3600)
            float speedKmh = vehicle.getCurrentSpeedKmHour();

            if (Gdx.input.isKeyPressed(Keys.UP)) {
                // İleri git
                if (speedKmh < -REVERSE_SPEED_THRESHOLD) {
                    // Geri gidiyorsa önce fren uygula
                    brakeForce = BRAKE_FORCE;
                } else {
                    engineForce = ENGINE_FORCE;
                    brakeForce  = 0f;
                }
            } else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
                if (speedKmh > REVERSE_SPEED_THRESHOLD) {
                    // Hâlâ ileri gidiyorsa fren
                    brakeForce  = BRAKE_FORCE;
                } else {
                    // Durmuş veya zaten geriye gidiyorsa → geri vites
                    engineForce = -REVERSE_FORCE;
                    brakeForce  = 0f;
                }
            }

            if (Gdx.input.isKeyPressed(Keys.LEFT)) {
                steeringAngle = Math.min(steeringAngle + STEERING_SPEED * deltaTime, MAX_STEERING);
            } else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
                steeringAngle = Math.max(steeringAngle - STEERING_SPEED * deltaTime, -MAX_STEERING);
            } else {
                steeringAngle *= 0.85f; // ortaya dön
            }

            // Ön tekerleklere direksiyon (0 = ÖnSol, 1 = ÖnSağ)
            vehicle.setSteeringValue(steeringAngle, 0);
            vehicle.setSteeringValue(steeringAngle, 1);

            // Arka tekerleklere motor & fren (2 = ArkaSol, 3 = ArkaSağ)
            vehicle.applyEngineForce(engineForce, 2);
            vehicle.applyEngineForce(engineForce, 3);
            vehicle.setBrake(brakeForce, 2);
            vehicle.setBrake(brakeForce, 3);

            // ----- TRANSFORM SENKRONIZASYONU -----
            // Chassis
            vc.chassisBody.getWorldTransform(vc.chassisInstance.transform);

            // Tekerlekler
            for (int i = 0; i < vehicle.getNumWheels(); i++) {
                vehicle.updateWheelTransform(i, true);
                ModelInstance wi = vc.wheelInstances.get(i);
                // Dünya matrisini al, silindir Y→X dönüşümünü uygula
                wi.transform.set(vehicle.getWheelTransformWS(i)).mul(wheelOffset);
            }

            // ----- RENDER -----
            modelBatch.render(vc.chassisInstance, environment);
            for (ModelInstance wi : vc.wheelInstances) {
                modelBatch.render(wi, environment);
            }
        }

        modelBatch.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
    }
}

