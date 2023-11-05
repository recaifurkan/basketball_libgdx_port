package com.rfbsoft.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;

import java.io.FileNotFoundException;

public class VideoPlaneComponent implements Component {
    public VideoPlayer videoPlayer;

    public VideoPlaneComponent(String fileName) {
        videoPlayer = VideoPlayerCreator.createVideoPlayer();
        videoPlayer.setOnCompletionListener(file -> Gdx.app.log("VideoTest", file.name() + " fully played."));
        videoPlayer.setOnVideoSizeListener((width, height) -> Gdx.app.log("VideoTest", "The video has a size of " + width + "x" + height + "."));
        try {
            switch (Gdx.app.getType()) {
                case Android:
                case iOS:
                    videoPlayer.play(Gdx.files.internal(String.format("video/%s.mp4", fileName)));
                    break;
                case WebGL:
                case Desktop:
                    videoPlayer.play(Gdx.files.internal(String.format("video/%s.webm", fileName)));
                    break;
            }
            //
        } catch (FileNotFoundException e) {
            Gdx.app.error("gdx-video", "Oh no!");
        }
    }
}
