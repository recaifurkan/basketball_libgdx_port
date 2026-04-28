package com.rfbsoft.initializer.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btDefaultVehicleRaycaster;
import com.badlogic.gdx.physics.bullet.dynamics.btRaycastVehicle;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btWheelInfo;
import com.badlogic.gdx.utils.Array;
import com.rfbsoft.components.BulletCollisionComponent;
import com.rfbsoft.components.VehicleComponent;
import com.rfbsoft.entities.GameEntity;
import com.rfbsoft.entities.StaticObject;

public class CarInitializer extends AbstractInitializer {

    // Chassis boyutları (yarı uzunluklar)
    private static final float CHASSIS_HALF_W  = 0.9f;
    private static final float CHASSIS_HALF_H  = 0.25f;
    private static final float CHASSIS_HALF_L  = 1.8f;

    // Tekerlek
    private static final float WHEEL_RADIUS     = 0.4f;
    private static final float WHEEL_WIDTH      = 0.3f;

    // Fizik
    private static final float CHASSIS_MASS     = 800f;

    @Override
    public void initialize(Engine engine) {
        initialize(engine, new Vector3(5f, 5f, 5f));
    }

    @Override
    public void initialize(Engine engine, Vector3 position) {

        ModelBuilder mb = new ModelBuilder();

        // --- Chassis (kırmızı kutu) ---
        Model chassisModel = mb.createBox(
                CHASSIS_HALF_W * 2, CHASSIS_HALF_H * 2, CHASSIS_HALF_L * 2,
                new Material(ColorAttribute.createDiffuse(Color.RED)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        ModelInstance chassisInstance = new ModelInstance(chassisModel);
        chassisInstance.transform.setToTranslation(position);

        // --- Tekerlek modeli (gri silindir) ---
        Model wheelModel = mb.createCylinder(
                WHEEL_RADIUS * 2, WHEEL_WIDTH, WHEEL_RADIUS * 2, 12,
                new Material(ColorAttribute.createDiffuse(new Color(0.2f, 0.2f, 0.2f, 1f))),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        // --- Chassis Bullet rigid body ---
        btBoxShape chassisShape = new btBoxShape(new Vector3(CHASSIS_HALF_W, CHASSIS_HALF_H, CHASSIS_HALF_L));
        Vector3 localInertia = new Vector3();
        chassisShape.calculateLocalInertia(CHASSIS_MASS, localInertia);

        BulletCollisionComponent.MotionState motionState =
                new BulletCollisionComponent.MotionState(chassisInstance.transform);
        btRigidBody.btRigidBodyConstructionInfo bodyInfo =
                new btRigidBody.btRigidBodyConstructionInfo(CHASSIS_MASS, motionState, chassisShape, localInertia);
        btRigidBody chassisBody = new btRigidBody(bodyInfo);
        chassisBody.setActivationState(Collision.DISABLE_DEACTIVATION);
        chassisBody.setCollisionFlags(
                chassisBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);

        GameEntity carEntity = new GameEntity("car");
        chassisBody.userData = carEntity;

        // Rigid body'yi dünyaya doğrudan ekle (BulletRigidBodyComponent kullanmıyoruz —
        // BulletSystem.entityAdded ile çift kayıttan kaçınmak için)
        StaticObject.gamePhysicWorld.addRigidBody(chassisBody);

        // --- btRaycastVehicle ---
        btRaycastVehicle.btVehicleTuning tuning = new btRaycastVehicle.btVehicleTuning();
        btDefaultVehicleRaycaster vehicleRaycaster =
                new btDefaultVehicleRaycaster(StaticObject.gamePhysicWorld);
        btRaycastVehicle vehicle = new btRaycastVehicle(tuning, chassisBody, vehicleRaycaster);
        vehicle.setCoordinateSystem(0, 1, 2); // sağ=X, yukarı=Y, ileri=Z

        StaticObject.gamePhysicWorld.addVehicle(vehicle);

        // --- Tekerlek bağlantı parametreleri ---
        Vector3 wheelDir  = new Vector3(0, -1, 0);   // süspansiyon yönü
        Vector3 wheelAxle = new Vector3(-1, 0, 0);   // dönüş ekseni
        float suspensionRest = 0.4f;

        // Bağlantı noktaları: [ÖnSol, ÖnSağ, ArkaSol, ArkaSağ]
        float[] cx = { -CHASSIS_HALF_W * 0.85f,  CHASSIS_HALF_W * 0.85f,
                -CHASSIS_HALF_W * 0.85f,  CHASSIS_HALF_W * 0.85f };
        float[] cz = {  CHASSIS_HALF_L * 0.85f,  CHASSIS_HALF_L * 0.85f,
                -CHASSIS_HALF_L * 0.85f, -CHASSIS_HALF_L * 0.85f };
        boolean[] isFront = { true, true, false, false };

        Array<ModelInstance> wheelInstances = new Array<>();

        for (int i = 0; i < 4; i++) {
            vehicle.addWheel(
                    new Vector3(cx[i], 0f, cz[i]),
                    wheelDir,
                    wheelAxle,
                    suspensionRest,
                    WHEEL_RADIUS,
                    tuning,
                    isFront[i]);

            btWheelInfo wi = vehicle.getWheelInfo(i);
            wi.setSuspensionStiffness(20f);
            wi.setWheelsDampingRelaxation(2.3f);
            wi.setWheelsDampingCompression(4.4f);
            wi.setFrictionSlip(1000f);
            wi.setRollInfluence(0.1f);

            wheelInstances.add(new ModelInstance(wheelModel));
        }

        vehicle.resetSuspension();

        // --- Entity ---
        VehicleComponent vehicleComponent =
                new VehicleComponent(vehicle, chassisBody, chassisInstance, wheelInstances);
        carEntity.add(vehicleComponent);

        engine.addEntity(carEntity);
    }
}

