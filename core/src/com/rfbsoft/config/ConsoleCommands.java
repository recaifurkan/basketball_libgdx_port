package com.rfbsoft.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.rfbsoft.entities.StaticObject;
import com.strongjoshua.console.CommandExecutor;
import com.strongjoshua.console.GUIConsole;

import static com.rfbsoft.initializer.Init.INITIALIZER_MAP;

public class ConsoleCommands extends CommandExecutor {

    public static GUIConsole console;

    public ConsoleCommands() {

        Skin skin = new Skin(Gdx.files.classpath("default_skin/uiskin.json"), new TextureAtlas(Gdx.files.classpath("default_skin/uiskin.atlas")));
        console = new GUIConsole(skin, true, Input.Keys.PERIOD);
        console.setCommandExecutor(this);
        console.setSizePercent(50, 50);
        console.enableSubmitButton(true);
    }

    public void draw() {
        console.refresh();
        if (console.isVisible()) {
            console.draw();
        }
    }

    public void test() {
        Gdx.app.log("Hi!", "I am your friendly console");
    }


    public void spawn(String initalizer, float x, float y, float z) {
        INITIALIZER_MAP.get(initalizer).initialize(StaticObject.gameEngine, new Vector3(x, y, z));
    }

    public void spawn(String initalizer, float x, float y, float z, int count) {
        for (int i = 0; i < count; i++) {
            INITIALIZER_MAP.get(initalizer).initialize(StaticObject.gameEngine, new Vector3(x, y, z));
        }

    }

    public void hide() {
        console.setVisible(false);
    }
}