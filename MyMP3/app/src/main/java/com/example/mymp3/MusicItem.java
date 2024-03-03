package com.example.mymp3;

import android.media.MediaPlayer;
import android.provider.MediaStore;

public class MusicItem {
    private String name;
    private String url;
    private boolean isPlaying = false;
    private String filePath = "";
    private MediaPlayer mediaPlayer;
    private boolean hasDataSource = false;
    MusicItem(String name, String url){
        this.name = name;
        this.url = url;
        mediaPlayer = new MediaPlayer();

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public boolean isHasDataSource() {
        return hasDataSource;
    }

    public void setHasDataSource(boolean hasDataSource) {
        this.hasDataSource = hasDataSource;
    }
}
