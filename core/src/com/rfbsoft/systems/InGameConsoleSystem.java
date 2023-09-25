package com.rfbsoft.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.rfbsoft.config.ConsoleCommands;

public class InGameConsoleSystem extends EntitySystem {
    private final ConsoleCommands consoleCommands;

    public InGameConsoleSystem(){
        consoleCommands = new ConsoleCommands();

    }

    @Override
    public void update(float deltaTime) {
        consoleCommands.draw();
    }
}
