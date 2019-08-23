package com.example.day017.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.day017.entity.Music;

import java.util.ArrayList;
import java.util.List;

public class MusicUtil {

    private static int index = 0;


    public static List<Music> getMusic(Context context){
        ArrayList<Music> ms = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if(cursor!=null){
            while(cursor.moveToNext()){
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                //path地址
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                Music music = new Music(title, artist, duration, data, size,index);
                index++;
                ms.add(music);
            }
            cursor.close();
            return ms;
        }else{
            Toast.makeText(context, "数据为空", Toast.LENGTH_SHORT).show();
        }

        return null;
    }

}
