package com.example.day017;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.day017.adapter.MusicAdapter;
import com.example.day017.broadcast.MusicReceiver;
import com.example.day017.entity.Music;
import com.example.day017.service.MusicService;
import com.example.day017.util.MusicUtil;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView lv;
    private static final String TAG = "MusicActivity";
    private LinearLayout ll;
    private ImageView pause1;
    private ImageView restart;
    private ImageView top;
    private ImageView next;
    private List<Music> musicList;
    private int index;
    private static SeekBar seekBar;
    private ImageView mode;

    //让他 % 4
    private int playMode = 0;

    private ServiceConnection connection;
    private static MusicService.MusicBinder binder;
    private Timer timer;
    private MediaPlayer mediaPlayer;


    public static Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 888){
                seekBar.setMax(msg.arg2);
                seekBar.setProgress(msg.arg1);
            }else if(msg.what == 100){
                Log.i(TAG, "handleMessage: ");
                binder.callPause();
            }else if(msg.what == 110){
                binder.callNext();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_music);


        ininView();

        //动态注册一个广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.music.pause");
        intentFilter.addAction("com.music.next");
        MusicReceiver musicReceiver = new MusicReceiver();
        registerReceiver(musicReceiver,intentFilter);


        musicList = MusicUtil.getMusic(this);
        MusicAdapter musicAdapter = new MusicAdapter(musicList, this);
        lv.setAdapter(musicAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //调用服务中的方法,播放音乐.
                binder.callPaly(i);
                seekBar.setMax(Integer.parseInt(musicList.get(i).getDuration()));
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                   binder.callSeek(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //1, 启动服务
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);

        //2,绑定服务
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                //ibinder
                binder = (MusicService.MusicBinder) iBinder;
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };

        bindService(intent,connection, Service.BIND_AUTO_CREATE);


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.pause1:
                binder.callPause();
                break;
            case R.id.next:
                binder.callNext();
                break;
            case R.id.top:
                binder.callTop();
                break;
            case R.id.mode:
                //单曲
                int i = playMode % 3;
                binder.callMode(i);
                playMode++;
                switch (i){
                    case Music.MODE_SIGNEL:
//                        mode.s("单曲");
                        break;
                    case Music.MODE_ORDER:
//                        mode.setText("顺序");
                        break;
                    case Music.MODE_RANDOM:
//                        mode.setText("随机");
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    private void ininView() {
        lv = (ListView) findViewById(R.id.lv);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        ll = (LinearLayout) findViewById(R.id.ll);
        pause1 =  findViewById(R.id.pause1);
        pause1.setOnClickListener(this);
        restart =  findViewById(R.id.restart);
        restart.setOnClickListener(this);
        top =  findViewById(R.id.top);
        top.setOnClickListener(this);
        next =  findViewById(R.id.next);
        next.setOnClickListener(this);
        mode =  findViewById(R.id.mode);
        mode.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        unbindService(connection);

    }
}
