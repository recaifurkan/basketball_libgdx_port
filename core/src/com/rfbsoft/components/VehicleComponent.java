package com.rfbsoft.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.dynamics.btRaycastVehicle;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Array;

public class VehicleComponent implements Component {

    public final btRaycastVehicle vehicle;
    public final btRigidBody chassisBody;
    public final ModelInstance chassisInstance;
    public final Array<ModelInstance> wheelInstances;

    public VehicleComponent(btRaycastVehicle vehicle,
                            btRigidBody chassisBody,
                            ModelInstance chassisInstance,
                            Array<ModelInstance> wheelInstances) {
        this.vehicle = vehicle;
        this.chassisBody = chassisBody;
        this.chassisInstance = chassisInstance;
        this.wheelInstances = wheelInstances;
    }
}

