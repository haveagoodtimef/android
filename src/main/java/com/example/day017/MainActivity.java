package com.example.day017;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.day017.entity.Music;
import com.example.day017.util.MusicUtil;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button start;
    private Button pause;
    private Button restart;
    private Button stop;
    private SeekBar seek;

    private List<Music> music;

    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();
        mediaPlayer = new MediaPlayer();

        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},100);

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //如果是用户拖动才开始改变播放位置
                if(b){
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        music = MusicUtil.getMusic(this);
        Toast.makeText(this, music.size()+"", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

        }else{
            Toast.makeText(this, "必须同意", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.start:
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(music.get(1).getData());
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                            //mediaPlayer.getDuration()文件的播放时长
                            seek.setMax(mediaPlayer.getDuration());
                        }
                    });

                    //播放错误时,回调.
                    mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
//                            Toast.makeText(MainActivity.this, "失败了.", Toast.LENGTH_SHORT).show();
                            //当返回值是false时候回调用完成的方法.当时true时候,不调用.
                            return false;
                        }
                    });
                    //唱完一首的时候回调
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            Toast.makeText(MainActivity.this, "失败了,被调用", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //修改seek的位置.
                        //mediaPlayer.getCurrentPosition()得到当前播放的位置.
                        seek.setProgress(mediaPlayer.getCurrentPosition());
                    }
                },0,1000);

                break;
            case R.id.pause:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                break;
            case R.id.restart:
                mediaPlayer.start();
                break;
            case R.id.stop:
                mediaPlayer.stop();
                seek.setProgress(0);

                break;
        }
    }






    private void initView() {

        seek = (SeekBar) findViewById(R.id.seek);

        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(this);
        pause = (Button) findViewById(R.id.pause);
        pause.setOnClickListener(this);
        restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(this);
        stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(this);
    }


}
