package com.example.day017;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.day017.adapter.MusicAdapter;
import com.example.day017.entity.Music;
import com.example.day017.util.MusicUtil;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicActivity_text extends AppCompatActivity {
    private ListView lv;
    private LinearLayout ll;
    private Button pause;
    private Button restart;
    private Button top;
    private Button next;
    private List<Music> musicList;
    private int index;
    private SeekBar seekBar;

    private Timer timer;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_music);

        mediaPlayer = new MediaPlayer();

        ininView();
        musicList = MusicUtil.getMusic(this);
        final MusicAdapter musicAdapter = new MusicAdapter(musicList, this);
        lv.setAdapter(musicAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                index = i;
                Toast.makeText(MusicActivity_text.this, i+"", Toast.LENGTH_SHORT).show();
                startMusic(i);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (++index > musicList.size() - 1){
                    index = 0;
                }
                startMusic(index);
            }
        });

        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(--index <= 0){
                    index = 0;
                }
                startMusic(index);
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    pause.setText("继续");
                }else{
                    mediaPlayer.start();
                    pause.setText("暂停");
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
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


    }

    private void startMusic(int i) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(musicList.get(i).getData());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer1) {
                    mediaPlayer.start();
                    seekBar.setMax(mediaPlayer.getDuration());
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        }
                    },0,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 3:05
    // 4:15

    private void ininView() {
        lv = (ListView) findViewById(R.id.lv);

        seekBar = (SeekBar) findViewById(R.id.seek_bar);

        ll = (LinearLayout) findViewById(R.id.ll);
        pause = (Button) findViewById(R.id.pause);
        restart = (Button) findViewById(R.id.restart);
        top = (Button) findViewById(R.id.top);
        next = (Button) findViewById(R.id.next);
    }
}
