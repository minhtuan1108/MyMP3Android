package com.example.mymp3;



import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

// AsyncTask to download an MP3 file
public class DownloadTask extends AsyncTask<String, Integer, Void> {
    private File file;
    @Override
    protected Void doInBackground(String... strings) {
        try {
            file = new File(Environment.getExternalStorageDirectory(), "MyMP3s");
            if (!file.exists()) {
                file.mkdir();
            }
            URL url = new URL(strings[0]);
            URLConnection connection = url.openConnection();
            connection.connect();

            // Get the file length
            int lengthOfFile = connection.getContentLength();

            // Download the file
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(file.getPath() + "/audio.mp3");

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress((int) (total * 100 / lengthOfFile));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        // Update UI with download progress (if needed)
        Log.d("DownloadProgress", "Progress: " + values[0] + "%");
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        // File download completed
        Log.d("DownloadProgress", "Download completed!");
    }
}

