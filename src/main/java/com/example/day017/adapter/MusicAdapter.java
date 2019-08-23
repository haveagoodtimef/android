package com.example.day017.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.day017.R;
import com.example.day017.entity.Music;

import java.util.List;

public class MusicAdapter extends BaseAdapter {
    private List<Music> list;
    private Context context;

    public MusicAdapter(List<Music> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.music_layout, null);
            viewHolder.artist = view.findViewById(R.id.artist);
            viewHolder.duration = view.findViewById(R.id.duration);
            viewHolder.title = view.findViewById(R.id.title);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.title.setText(list.get(i).getTitle());
        int i1 = Integer.parseInt(list.get(i).getDuration());
        viewHolder.duration.setText(formatTime(i1));
        viewHolder.artist.setText(list.get(i).getArtist());
        return view;
    }

    class ViewHolder{
        TextView title;
        TextView duration;
        TextView artist;
    }

    private String formatTime(int i1){
        if(i1 / 1000 % 60 < 0){
            return i1 / 1000 / 60 +":0"+ i1 / 1000 % 60;
        }else {
            return  i1 / 1000 / 60 + ":" + i1 / 1000 % 60;
        }
    }
}
