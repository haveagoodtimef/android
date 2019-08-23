package com.example.day017.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.day017.MusicActivity;

public class MusicReceiver extends BroadcastReceiver {
    private static final String TAG = "MusicReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

       if(intent.getAction().equals("com.music.pause")){
           MusicActivity.handler.sendEmptyMessage(100);
       }else if(intent.getAction().equals("com.music.next")){
           MusicActivity.handler.sendEmptyMessage(110);
       }
    }
}
