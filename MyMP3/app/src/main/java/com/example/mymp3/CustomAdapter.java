package com.example.mymp3;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private List<MusicItem> itemList;
    private LayoutInflater inflater;
    private RotateAnimation animation;
    private Context mContext;
    private MainActivity mActivity;
    public CustomAdapter(Context context, List<MusicItem> itemList, MainActivity activity) {
        this.itemList = itemList;
        inflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mActivity = activity;
        animation = new RotateAnimation(0f, 360f, 60f,  60f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(2000);
    }

    public List<MusicItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<MusicItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.music_item, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = convertView.findViewById(R.id.title_music_item);
            holder.playMusicImage = convertView.findViewById(R.id.play_btn);
            holder.diskMusic = convertView.findViewById(R.id.disk_music);
            holder.downloadBtn = convertView.findViewById(R.id.download);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MusicItem item = itemList.get(position);
        holder.titleTextView.setText(item.getName());

        //Xử lý sự kiện click chuột tại button play music
        holder.playMusicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: Play music button for " + holder.titleTextView.getText());
                if(item.isPlaying()){
                    Log.d("TAG", "Playing");
                    stopPlayMusic(holder.diskMusic, holder.playMusicImage, item);
                    item.getMediaPlayer().pause();
                    Log.d("Check status", String.valueOf(item.getMediaPlayer().isPlaying()));
                }else{
                    try {
                        if(item.getFilePath().equalsIgnoreCase("")){
                            Toast.makeText(mContext, "Please download before listening your music!", Toast.LENGTH_SHORT).show();
                        }else{
                            Log.d("Check status", String.valueOf(item.getMediaPlayer().isPlaying()));
                            if (!item.isHasDataSource()) {
                                item.getMediaPlayer().setDataSource(item.getFilePath());
                                item.setHasDataSource(true);
                                item.getMediaPlayer().setLooping(true);
                                item.getMediaPlayer().prepare();
                            }

                            item.getMediaPlayer().start();
                            holder.diskMusic.startAnimation(animation);
                            holder.playMusicImage.setImageResource(R.drawable.pause_button);
                            item.setPlaying(true);
                            Log.d("TAG", "Num of child: " + parent.getChildCount());
                            //Stop all item are playing to play this item
                            for( int pos = 0; pos < itemList.size(); pos++){
                                if(pos != position && itemList.get(pos).isPlaying()){
                                    View child = parent.getChildAt(pos);
                                    child.findViewById(R.id.disk_music).setAnimation(null);
                                    ImageView playButton = (ImageView) child.findViewById(R.id.play_btn);
                                    playButton.setImageResource(R.drawable.play_button);
                                    itemList.get(pos).setPlaying(false);
                                    itemList.get(pos).getMediaPlayer().pause();
                                }
                            }
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }

            }
        });

        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.checkPermission(item);
            }
        });
        return convertView;
    }

    public void stopPlayMusic(ImageView disk, ImageView btnPlay, MusicItem item){
        Log.d("Music status", "stop");
        disk.setAnimation(null);
        btnPlay.setImageResource(R.drawable.play_button);
        item.setPlaying(false);
    }

    public void releaseMediaPlayer() {
        for (MusicItem music : itemList) {
            if (music.getMediaPlayer() != null) {
                music.getMediaPlayer().release();
                music.setMediaPlayer(null);
            }
        }

    }
    private static class ViewHolder {
        TextView titleTextView;
        ImageView playMusicImage;
        ImageView diskMusic;
        Button downloadBtn;
    }
}