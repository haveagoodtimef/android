package com.example.day017.util;

import android.media.MediaPlayer;

//单利的模式
public class MediaPlayerFactory {
    //汉
    private static MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();

    private MediaPlayerFactory(){
    }

    public static MediaPlayerFactory getMediaPlayerFactory(){
        return mediaPlayerFactory;
    }





}
