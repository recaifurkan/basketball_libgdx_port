package com.rfbsoft.entities;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.rfbsoft.utils.SelfExpiringHashMap;

import java.util.HashMap;

public class GameEntity extends Entity {

    public static final HashMap<Integer, GameEntity> USER_INDEX_MAP = new HashMap<>();

    private static int userIndex = 0;

    public static int addUserIndex(GameEntity gameEntity) {
        int index = userIndex++;
        USER_INDEX_MAP.put(index, gameEntity);
        return index;
    }

    public static GameEntity retrieveFromUserIndex(int index) {
        return USER_INDEX_MAP.get(index);
    }

    private static final long CONTACT_WAIT_TIME = 1000;

    public SelfExpiringHashMap startMap = new SelfExpiringHashMap(CONTACT_WAIT_TIME);
    public SelfExpiringHashMap endMap = new SelfExpiringHashMap(CONTACT_WAIT_TIME);
    private static long gameObjectSize = 0;
    private final String name;

    public GameEntity() {
        this("Game Object");
    }

    public GameEntity(String name) {
        this.name = name + gameObjectSize++;
    }

    public void adds(Component... components) {
        for (Component component : components) {
            this.add(component);
        }
    }


    public void addComponents(ImmutableArray<Component> components) {
        for (Component component : components) {
            this.add(component);
        }
    }


    public String getName() {
        return name;
    }

    public void onCollisionStart(GameEntity entity) {
    }


    public void onCollisionEnd(GameEntity entity) {
    }
}
