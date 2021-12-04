package com.bistu.musicplayer;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;



public class DownloadIntentService extends IntentService {

    private final String TAG="LOGCAT";

    public DownloadIntentService() {
        super("DownloadIntentService");//这就是个name
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    protected void onHandleIntent(Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            String name = bundle.getString("url");
            String downloadUrl = "https://freemusicarchive.org/track/"+bundle.getString("url")+"/download";
            //文件保存地址
            File dirs = new File("/sdcard/music-test");
            //输出文件名
            File file = new File(dirs, name +".mp3");
            Log.d(TAG,"下载启动："+downloadUrl+" --to-- "+ file.getPath());
            // 开始下载
            downloadFile(downloadUrl, file);
            Intent intent_3 = new Intent(this,DownLoad.class);
            Log.d(TAG,"下载结束");
           intent_3.putExtra("finish","true");
            intent_3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(intent_3);
            startActivity(intent_3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //文件下载的具体实现
    private void downloadFile(String downloadUrl, File file){
        FileOutputStream _outputStream;//文件输出流
        try {
            _outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "找不到目录！");
            e.printStackTrace();
            return;
        }
        InputStream _inputStream = null;//文件输入流
        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection _downLoadCon = (HttpURLConnection) url.openConnection();
            _downLoadCon.setRequestMethod("GET");
            _inputStream = _downLoadCon.getInputStream();
            //服务器返回的响应码
            int respondCode = _downLoadCon.getResponseCode();
            if (respondCode == 200) {
                // 缓冲数据块，把读取到的数据储存在这个数组
                byte[] buffer = new byte[1024*8];
                int len;
                while ((len = _inputStream.read(buffer)) != -1) {
                    _outputStream.write(buffer, 0, len);
                }
            } else {
                Log.d(TAG, "respondCode:" + respondCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (_outputStream != null) {
                    _outputStream.close();
                }
                if (_inputStream != null) {
                    _inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

}