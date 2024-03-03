package com.example.mymp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 10;
    private BroadcastReceiver onComplete;
    private MusicItem item;
    private CustomAdapter adapterListView;
    private Album currentAlbum;
    private List<MusicItem> itemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner spinner = findViewById(R.id.spinner);

        List<MusicItem> listMusic1 = new ArrayList<>();
        listMusic1.add(new MusicItem("Bao Nhiêu Cho Vừa", ""));
        listMusic1.add(new MusicItem("Dây Dứt Nỗi Đau", ""));
        listMusic1.add(new MusicItem("Thêm Bao Nhiêu Lâu", ""));

        List<MusicItem> listMusic2 = new ArrayList<>();
        listMusic2.add(new MusicItem("Anh Ơi Ở Lại", ""));
        listMusic2.add(new MusicItem("Tháng Tư Là Lời Nói Dối Của Em", ""));
        listMusic2.add(new MusicItem("Thanh Xuân", ""));

        List<MusicItem> listMusic3 = new ArrayList<>();
        listMusic3.add(new MusicItem("Em Là Kẻ Đáng Thương", ""));
        listMusic3.add(new MusicItem("Thất Tình", ""));
        listMusic3.add(new MusicItem("Từng Quen", ""));

        List<MusicItem> listMusic4 = new ArrayList<>();
        listMusic4.add(new MusicItem("Đời Là Đi", ""));
        listMusic4.add(new MusicItem("Ghệ Iu Dấu Của Em Ơi", ""));
        listMusic4.add(new MusicItem("Yêu (EDM ver)", ""));

        List<MusicItem> listMusic5 = new ArrayList<>();
        listMusic5.add(new MusicItem("Hơn Cả Yêu", ""));
        listMusic5.add(new MusicItem("Người Lạ Ơi", ""));
        listMusic5.add(new MusicItem("Rồi Tới Luôn", ""));
        //Tạo dữ liệu
        Album[] albums = {new Album("Nhạc của Mr.Siro", listMusic1),
                            new Album("Nhạc lofi chill", listMusic2),
                            new Album("Nhạc suy", listMusic3),
                            new Album("Nhạc titok on trend", listMusic4),
                            new Album("Remix, tình yêu", listMusic5)};

        String[] options = {"Nhạc của Mr.Siro", "Nhạc lofi chill","Nhạc suy", "Nhạc titok on trend","Remix, tình yêu"};
        currentAlbum = albums[0];
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ListView listView = findViewById(R.id.list_music);
        adapterListView = new CustomAdapter(this, currentAlbum.getListMusic(), this);
        listView.setAdapter(adapterListView);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                for (Album al: albums) {
                    if(al.getName().equals(selectedOption)){
                        currentAlbum = al;
                        itemList = al.getListMusic();
                        adapterListView.setItemList(itemList);
                        listView.setAdapter(adapterListView);

                    }
                }
            }

            @Override
        public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where no option is selected
            }
        });




    }

    public void checkPermission(MusicItem item){
        this.item = item;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, REQUEST_PERMISSION_CODE);
            }else{
                startDownloadFile(this.item);
            }
        }else{
            startDownloadFile(this.item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDownloadFile(this.item);
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startDownloadFile(MusicItem item) {
        String urlFile = "https://minhtuan1108.github.io/MP3PlayerAlbum/" + currentAlbum.getName() + "/" + item.getName() + ".mp3";
        Log.d("Link download", urlFile);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlFile));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Download");
        request.setDescription("Download file...");

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, String.valueOf(System.currentTimeMillis()));
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if(downloadManager != null){
            long downloadId = downloadManager.enqueue(request);
            onComplete = new BroadcastReceiver() {
                @SuppressLint("Range")
                @Override
                public void onReceive(Context context, Intent intent) {
                    long completedDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if (completedDownloadId == downloadId) {
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(completedDownloadId);
                        Cursor cursor = downloadManager.query(query);
                        if (cursor.moveToFirst()) {
                            @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                String downloadedFilePath = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                                Log.d("file path", downloadedFilePath);
                                item.setFilePath(downloadedFilePath);
                            }
                        }
                    }
                }
            };
            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onComplete);
        adapterListView.releaseMediaPlayer();
    }
}