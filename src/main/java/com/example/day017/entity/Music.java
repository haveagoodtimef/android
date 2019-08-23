package com.example.day017.entity;

public class Music {

    public static final int MODE_SIGNEL = 0; //单曲
    public static final int MODE_ORDER = 1;  //顺序
    public static final int MODE_RANDOM = 2; //随机


    private String title;
    private String artist;
    private String duration;
    private String data;
    private String size;

    //加一个位置标签
    private int position;

    public Music() {
    }

    public Music(String title, String artist, String duration, String data, String size, int position) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.data = data;
        this.size = size;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Music{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", duration='" + duration + '\'' +
                ", data='" + data + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
