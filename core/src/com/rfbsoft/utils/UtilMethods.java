package com.rfbsoft.utils;

import com.badlogic.gdx.math.Vector3;

import static com.rfbsoft.systems.BulletSystem.GRAVITY;

public class UtilMethods {


    public static Vector3 calculateInitialVelocity(Vector3 source, Vector3 target) {
        float displacementY = target.y - source.y;
        Vector3 displacementXZ = new Vector3(target.x - source.x, 0, target.z - source.z);
        double time = Math.sqrt(Math.abs(-2 * target.y / GRAVITY.y)) + Math.sqrt(Math.abs(2 * (displacementY - target.y) / GRAVITY.y));
        Vector3 velocityY = Vector3.Y.cpy().scl(Double.valueOf(Math.sqrt(-2 * GRAVITY.y * target.y)).floatValue());
        Vector3 velocityXZ = displacementXZ.scl(1f / Double.valueOf(time).floatValue());

        return velocityXZ.add(velocityY.scl(-Math.signum(GRAVITY.y)));
    }
}
