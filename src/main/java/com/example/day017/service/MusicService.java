package com.example.day017.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.day017.MainActivity;
import com.example.day017.MusicActivity;
import com.example.day017.R;
import com.example.day017.entity.Music;
import com.example.day017.util.MusicUtil;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 可以一直在后台运行
 * 并且可以调用服务中的方法.
 * 1,startService()
 * 2,bindService();
 * 3,Unbind
 * 4,stop
 */
public class MusicService extends Service {

    private static final String TAG = "MusicService";
    private int index;
    private MediaPlayer mediaPlayer;
    private List<Music> musicList;
    private Timer timer;

    //默认顺序
    private int palyMode = Music.MODE_ORDER;

    public MusicService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        mediaPlayer = new MediaPlayer();
        musicList = MusicUtil.getMusic(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Notification.Builder builder = new Notification.Builder(this);

        builder.setSmallIcon(R.mipmap.ic_launcher);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.remote_layout);

        Intent intent1 = new Intent();
        intent1.setAction("com.music.pause");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.pause,pendingIntent);


        Intent intent2 = new Intent();
        intent2.setAction("com.music.next");
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 100, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.next,pendingIntent2);


        builder.setCustomContentView(remoteViews);
        Notification build = builder.build();

        startForeground(1,build);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBinder();

    }

    public class MusicBinder extends Binder{
        public void callPaly(int position){
            index = position;
            play(position);
        }
        public void callPause(){
            pause();
        }

        public void callNext(){
            next();
        }

        public void callTop(){
            top();
        }
        public void callSeek(int position){
            seek(position);
        }

        public void callMode(int mode){
            playMode(mode);
        }

    }

    //服务中有一个播放的方法
    private void play(int position){
        if (timer != null){
            timer.cancel();
        }
        final int a = position;
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(musicList.get(position).getData());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Message obtain = Message.obtain();
                            obtain.what = 888;
                            obtain.arg1 = mediaPlayer.getCurrentPosition();
                            obtain.arg2 = Integer.parseInt(musicList.get(a).getDuration());
                            MusicActivity.handler.sendMessage(obtain);
                        }
                    },0,1000);
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mode(palyMode);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //暂停的方法
    private void pause(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    private void next(){
        if(++index > musicList.size() - 1){
            index = 0;
        }
        play(index);
    }

    private void top(){
        if(--index <= 0){
            index = 0;
        }
        play(index);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        stopForeground(true);
    }

    public void seek(int position){
        mediaPlayer.seekTo(position);
    }


    public void playMode(int mode){
        switch (mode){
            case Music.MODE_SIGNEL:
                palyMode = Music.MODE_SIGNEL;
                break;
            case Music.MODE_ORDER:
                palyMode = Music.MODE_ORDER;
                break;
            case Music.MODE_RANDOM:
                palyMode = Music.MODE_RANDOM;
                break;
            default:
                break;
        }
    }

    //歌曲播放的逻辑
    public void mode(int mode){
        switch (mode){
            case Music.MODE_SIGNEL:
                mediaPlayer.setLooping(true);
                play(index);
                palyMode = Music.MODE_SIGNEL;
                break;
            case Music.MODE_ORDER:
                next();
                palyMode = Music.MODE_ORDER;
                break;
            case Music.MODE_RANDOM:
                int i = new Random().nextInt(musicList.size());
                play(i);
                //为什么要在这里赋值
                index = i;
                palyMode = Music.MODE_RANDOM;
                break;
                default:
                    break;
        }


    }
}
