package com.rfbsoft.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.utils.Disposable;
import com.rfbsoft.components.BulletCollisionComponent;
import com.rfbsoft.components.BulletRigidBodyComponent;
import com.rfbsoft.entities.GameEntity;
import com.rfbsoft.entities.StaticObject;
import com.rfbsoft.utils.ObjectAllocator;

public class BulletSystem extends EntitySystem implements EntityListener, Disposable {


    private static final int MAX_SUB_STEPS = 5;
    private static final float FIXED_TIME_STEP = 1f / 60f;
    private static final String TAG = BulletSystem.class.getName();
    public static final Vector3 GRAVITY = new Vector3(0, -9.8f, 0);
    public final btCollisionConfiguration collisionConfig;
    public final btCollisionDispatcher dispatcher;
    public final btBroadphaseInterface broadphase;

    public final btConstraintSolver solver;
    public final GameContactListener contactListener;

    private final btDiscreteDynamicsWorld gamePhysicWorld;
    private final ComponentMapper<BulletCollisionComponent> collisionMapper = ComponentMapper.getFor(BulletCollisionComponent.class);
    private final ComponentMapper<BulletRigidBodyComponent> rigidMapper = ComponentMapper.getFor(BulletRigidBodyComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        Family family = Family.one(BulletCollisionComponent.class, BulletRigidBodyComponent.class).get();
        engine.addEntityListener(family, this);
    }

    public BulletSystem() {


        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new GameCollisionDispatcher(collisionConfig);
        broadphase = new btAxisSweep3(new Vector3(-1000, -1000, -1000), new Vector3(1000, 1000, 1000));
        solver = new btSequentialImpulseConstraintSolver();
        this.gamePhysicWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfig);
        StaticObject.gamePhysicWorld = gamePhysicWorld;
        gamePhysicWorld.setGravity(GRAVITY);
        contactListener = new GameContactListener();
    }

    @Override
    public void update(float deltaTime) {
        this.gamePhysicWorld.stepSimulation(deltaTime, MAX_SUB_STEPS, FIXED_TIME_STEP);
    }


    @Override
    public void dispose() {
        collisionConfig.dispose();
        dispatcher.dispose();
        broadphase.dispose();
        solver.dispose();
        this.gamePhysicWorld.dispose();
        contactListener.dispose();
    }

    @Override
    public void entityAdded(Entity entity) {
//    	RigidBodyComponent  rigidBodyComponent = entity.getComponent(RigidBodyComponent.class);
        BulletRigidBodyComponent bulletComponent = rigidMapper.get(entity);

        if (bulletComponent != null) {
            if (bulletComponent.rigidBody == null) {
                Gdx.app.error(TAG, "RigidBody Null");
                return;
            }
            this.gamePhysicWorld.addRigidBody(bulletComponent.rigidBody);
            return;
        }
        BulletCollisionComponent bulletCollisionComponent = collisionMapper.get(entity);
        if (bulletCollisionComponent.collisionObject == null) {
            Gdx.app.error(TAG, "CollisionObject Null");
            return;
        }
        this.gamePhysicWorld.addCollisionObject(bulletCollisionComponent.collisionObject);


    }

    @Override
    public void entityRemoved(Entity entity) {
        BulletRigidBodyComponent bulletComponent = rigidMapper.get(entity);

        if (bulletComponent != null) {
            if (bulletComponent.rigidBody == null) {
                Gdx.app.error(TAG, "RigidBody Null");
                return;
            }
            this.gamePhysicWorld.removeRigidBody(bulletComponent.rigidBody);
            return;
        }
        BulletCollisionComponent bulletCollisionComponent = collisionMapper.get(entity);
        if (bulletCollisionComponent.collisionObject == null) {
            Gdx.app.error(TAG, "CollisionObject Null");
            return;
        }
        this.gamePhysicWorld.addCollisionObject(bulletCollisionComponent.collisionObject);


    }

    private static ClosestRayResultCallback rayTestCB = new ClosestRayResultCallback(Vector3.Zero, Vector3.Z);

    public static GameEntity ray(int x, int y) {
        Vector3 rayFrom = ObjectAllocator.getObject(Vector3.class);
        Vector3 rayTo = ObjectAllocator.getObject(Vector3.class);


        Ray ray = StaticObject.gameCamera.getPickRay(x, y);
        rayFrom.set(ray.origin);
        rayTo.set(ray.direction).scl(1000f).add(rayFrom); // 50 meters max from the origin

        // Because we reuse the ClosestRayResultCallback, we need reset it's values
        rayTestCB.setCollisionObject(null);
        rayTestCB.setClosestHitFraction(1f);
        rayTestCB.setRayFromWorld(rayFrom);
        rayTestCB.setRayToWorld(rayTo);

        StaticObject.gamePhysicWorld.rayTest(rayFrom, rayTo, rayTestCB);
        ObjectAllocator.freeAllInPool();

        if (rayTestCB.hasHit()) {
            return (GameEntity) rayTestCB.getCollisionObject().userData;
        }
        return null;

    }

    public static class GameCollisionDispatcher extends CustomCollisionDispatcher {
        public GameCollisionDispatcher(btCollisionConfiguration collisionConfiguration) {
            super(collisionConfiguration);
        }

        @Override
        public boolean needsCollision(btCollisionObject colObj0, btCollisionObject colObj1) {
            if (colObj0.userData instanceof GameEntity && colObj1.userData instanceof GameEntity) {
                return super.needsCollision(colObj0, colObj1);
            }
            return false;
        }

        @Override
        public boolean needsResponse(btCollisionObject colObj0, btCollisionObject colObj1) {
            if (colObj0.userData instanceof GameEntity && colObj1.userData instanceof GameEntity) {
                return super.needsResponse(colObj0, colObj1);
            }

            return false;
        }


    }

    public class GameContactListener extends ContactListener {

        @Override
        public void onContactStarted(btCollisionObject colObj0, btCollisionObject colObj1) {
            GameEntity entity0 = (GameEntity) colObj0.userData;
            GameEntity entity1 = (GameEntity) colObj1.userData;
            if (entity0.startMap.checkIfAbsent(entity1)) {
                entity0.onCollisionStart(entity1);
            }
            if (entity1.startMap.checkIfAbsent(entity0)) {
                entity1.onCollisionStart(entity0);
            }
        }

        @Override
        public void onContactEnded(btCollisionObject colObj0, btCollisionObject colObj1) {
            GameEntity entity0 = (GameEntity) colObj0.userData;
            GameEntity entity1 = (GameEntity) colObj1.userData;

            if (entity0.endMap.checkIfAbsent(entity1)) {
                entity0.onCollisionEnd(entity1);
            }
            if (entity1.endMap.checkIfAbsent(entity0)) {
                entity1.onCollisionEnd(entity0);
            }
        }
    }
}
