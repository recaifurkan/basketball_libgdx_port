package com.rfbsoft;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Comparator;

public class TextureAtlasVideo extends ApplicationAdapter {
    public Animation<TextureRegion> runningAnimation;


    // Objects used
    Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
    SpriteBatch spriteBatch;

    TextureAtlas videoAtlas;
    Music sound;

    @Override
    public void create() {
        videoAtlas = new TextureAtlas(Gdx.files.internal("video/atlas/video.atlas"));
        sound = Gdx.audio.newMusic(Gdx.files.internal("video/sound/video.ogg"));
        videoAtlas.getRegions().sort(new Comparator<TextureAtlas.AtlasRegion>() {
            @Override
            public int compare(TextureAtlas.AtlasRegion o1, TextureAtlas.AtlasRegion o2) {
                return Integer.parseInt(o1.name) - Integer.parseInt(o2.name);
            }
        });

        // Initialize the Animation with the frame interval and array of frames
        walkAnimation = new Animation<TextureRegion>(0.04f, videoAtlas.getRegions(), Animation.PlayMode.LOOP);

        // Instantiate a SpriteBatch for drawing and reset the elapsed animation
        // time to 0
        spriteBatch = new SpriteBatch();

        sound.play();


    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Get current frame of animation for the current stateTime
        TextureRegion currentFrame = walkAnimation.getKeyFrame(sound.getPosition(), true);
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, 50, 50); // Draw current frame at (50, 50)
        spriteBatch.end();
    }

    @Override
    public void dispose() { // SpriteBatches and Textures must always be disposed
        spriteBatch.dispose();
        videoAtlas.dispose();
    }
}
