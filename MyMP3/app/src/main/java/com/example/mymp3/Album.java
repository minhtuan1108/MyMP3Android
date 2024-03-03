package com.example.mymp3;

import java.util.List;

public class Album {
    private String name;
    private List<MusicItem> listMusic;

    public Album(String name, List<MusicItem> listMusic) {
        this.name = name;
        this.listMusic = listMusic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MusicItem> getListMusic() {
        return listMusic;
    }

    public void setListMusic(List<MusicItem> listMusic) {
        this.listMusic = listMusic;
    }
}
