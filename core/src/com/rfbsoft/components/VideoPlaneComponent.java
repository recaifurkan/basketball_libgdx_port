package com.rfbsoft.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.video.VideoPlayer;
import com.rfbsoft.utils.VideoPlayerCreator;
import com.rfbsoft.utils.VideoPlayerStub;

import java.io.FileNotFoundException;

public class VideoPlaneComponent implements Component {
    public VideoPlayer videoPlayer;

    public VideoPlaneComponent(String fileName) {
        videoPlayer = VideoPlayerCreator.createVideoPlayer(fileName);
        videoPlayer.setOnCompletionListener(new VideoPlayer.CompletionListener() {
            @Override
            public void onCompletionListener(FileHandle file) {
                Gdx.app.log("VideoTest", file.name() + " fully played.");
            }
        });
        videoPlayer.setOnVideoSizeListener(new VideoPlayer.VideoSizeListener() {
            @Override
            public void onVideoSize(float width, float height) {
                Gdx.app.log("VideoTest", "The video has a size of " + width + "x" + height + ".");
            }
        });

        try {
            videoPlayer.play(Gdx.files.internal(String.format("video/%s.webm", fileName)));
        } catch (FileNotFoundException e) {
            Gdx.app.error("gdx-video", "Oh no!");
        }
    }
}
