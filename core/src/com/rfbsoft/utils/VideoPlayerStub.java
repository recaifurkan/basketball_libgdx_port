/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.rfbsoft.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.video.VideoPlayer;

import java.io.FileNotFoundException;
import java.util.Comparator;

public class VideoPlayerStub implements VideoPlayer {

    private final TextureAtlas videoAtlas;
    private final Music audio;
    private final Animation<TextureRegion> videoAnimation;
    private Texture texture;

    public VideoPlayerStub(String fileName) {
        videoAtlas = new TextureAtlas(Gdx.files.internal(String.format("video/atlas/%s.atlas", fileName)));
        audio = Gdx.audio.newMusic(Gdx.files.internal(String.format("video/sound/%s.mp3", fileName)));
        videoAtlas.getRegions().sort(new Comparator<TextureAtlas.AtlasRegion>() {
            @Override
            public int compare(TextureAtlas.AtlasRegion o1, TextureAtlas.AtlasRegion o2) {
                return Integer.parseInt(o1.name) - Integer.parseInt(o2.name);
            }
        });
        videoAnimation = new Animation<TextureRegion>(0.04f, videoAtlas.getRegions(), Animation.PlayMode.LOOP);
        audio.setLooping(true);
    }

    @Override
    public boolean play(FileHandle file) throws FileNotFoundException {
        audio.play();
        return true;
    }

    @Override
    public boolean update() {
        TextureRegion currentFrame = videoAnimation.getKeyFrame(audio.getPosition(), true);
        this.texture = currentFrame.getTexture();
        return true;
    }

    @Override
    @Null
    public Texture getTexture() {
        return this.texture;
    }

    @Override
    public boolean isBuffered() {
        return this.isPlaying();
    }

    @Override
    public void pause() {
        audio.pause();
    }

    @Override
    public void resume() {
        audio.play();
    }

    @Override
    public void stop() {
        audio.stop();
    }

    @Override
    public void setOnVideoSizeListener(VideoSizeListener listener) {
    }

    @Override
    public void setOnCompletionListener(CompletionListener listener) {
    }

    @Override
    public int getVideoWidth() {
        return 0;
    }

    @Override
    public int getVideoHeight() {
        return 0;
    }

    @Override
    public boolean isPlaying() {

        return audio.isPlaying();
    }

    @Override
    public int getCurrentTimestamp() {
        return (int) audio.getPosition();
    }

    @Override
    public void dispose() {
        audio.dispose();
        videoAtlas.dispose();

    }

    @Override
    public void setVolume(float volume) {
        audio.setVolume(volume);
    }

    @Override
    public float getVolume() {
        return audio.getVolume();
    }

    @Override
    public void setLooping(boolean looping) {
        audio.setLooping(looping);
    }

    @Override
    public boolean isLooping() {
        return audio.isLooping();
    }

    @Override
    public void setFilter(Texture.TextureFilter minFilter, Texture.TextureFilter magFilter) {

    }
}
