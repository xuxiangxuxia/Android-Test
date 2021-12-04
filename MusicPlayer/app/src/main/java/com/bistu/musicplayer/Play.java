package com.bistu.musicplayer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Play extends AppCompatActivity implements View.OnClickListener{

        List<String> data = new ArrayList<String>();


    private MediaPlayer mediaPlayer = new MediaPlayer();
    SeekBar seek ;
    ObjectAnimator animator;
    private TextView music_time_now_seconds  ;
    private TextView music_time_now_minute;
    private TextView music_time_minute;
    private TextView music_time_seconds;
    private boolean isplay = false;
    String name = "";
    String pathname = null;
    int index = -1;
    static final int UPDATE_TEXT = 1;
    int music_time = 0;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT:
                    //主线程中，实时更新音乐播放器的进度
                    int music_time_now = mediaPlayer.getCurrentPosition();
                    int minutes_now = music_time_now/1000/60;
                    int seconds_now = music_time_now/1000%60;
                    int minutes = music_time/1000/60;
                    int seconds = music_time/1000%60;
                    //实时显示时间进度
                    music_time_now_minute.setText(String.valueOf(minutes_now));
                    music_time_now_seconds.setText(String.valueOf(":"+seconds_now));
                    music_time_minute.setText(String.valueOf(minutes));
                    music_time_seconds.setText(":"+String.valueOf(seconds));
                    //实时显示进度条
                    seek.setProgress((int)((music_time_now/(float)music_time)*100));
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        data.add("fly");
        data.add("Shot");
        data.add("月光");
        data.add("Been Through");
        data.add("夜的第七章");
        data.add("超市");

        Intent intent_get = getIntent();
        name = intent_get.getStringExtra("name");
        index = Integer.parseInt(intent_get.getStringExtra("index"));

        ImageButton play = findViewById(R.id.button_player);
        ImageButton last = findViewById(R.id.last);
        ImageButton next = findViewById(R.id.next);
        TextView music_name = findViewById(R.id.song);
        music_time_now_minute = findViewById(R.id.time_now_minute);
        music_time_now_seconds = findViewById(R.id.time_now_second);
        music_time_minute = findViewById(R.id.music_time_minute);
        music_time_seconds = findViewById(R.id.music_time_seconds);
        seek = findViewById(R.id.seekbar);
         CircularImageView image = findViewById(R.id.picture);

        //设置图片的旋转效果
        View.inflate(this, R.layout.activity_play, null);
        animator = ObjectAnimator.ofFloat(image, "rotation", 0.0F, 360.0F);
        animator.setDuration(10 * 1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);



        play.setOnClickListener(this);
        last.setOnClickListener(this);
        next.setOnClickListener(this);


        //ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(discImagv,"rational",0f,360f);

        //动态申请权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else
        {
            Play(name);
        }
    }

    protected void Play(String str){
        pathname = searchFile(str);
        if(pathname.equals("找不到文件!!")){
            Toast.makeText(this, "没有文件，准备下载", Toast.LENGTH_SHORT).show();
            Intent intent_1 = new Intent(this,DownLoad.class);
            intent_1.putExtra("name",str);
            startActivity(intent_1);

        }
        else{
            initMediaPlayer(pathname);
        }
    }


    //在指定文件夹下寻找音乐文件
    private String searchFile(String keyword) {
        String result = "";
        File[]files = new File("/sdcard/music-test").listFiles();
        for (File file : files) {
            if (file.getName().indexOf(keyword) >= 0){
                result += file.getPath();
            }
        }
        if (result.equals("")){
            result = "找不到文件!!";
        }
        return result;
    }

    //初始化音乐播放器
    private void initMediaPlayer(String pathname) {
        try {
            Log.i("音乐文件路径", pathname);
            mediaPlayer.setDataSource(pathname); // 指定音频文件的路径
            mediaPlayer.prepare();// 让MediaPlayer进入到准备状态
            music_time = mediaPlayer.getDuration();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断用户是否授予权限
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Play(name);
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    public void onClick(View v) {
        TextView music_name = findViewById(R.id.song);
        music_name.getPaint().setFakeBoldText(true);
        ImageButton play = findViewById(R.id.button_player);
        switch (v.getId()) {
            case R.id.button_player:
                if(!isplay){
                    if(animator.isStarted()){
                        animator.resume();
                    }else
                        animator.start();
                    isplay = true;
                    play.setBackgroundResource(R.mipmap.pause);
                    if (!mediaPlayer.isPlaying()) {
                        music_name.setText(data.get(index));
                        mediaPlayer.start(); // 开始播放
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(true){
                                    Message message  = new Message();
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    message.what = 1;
                                    handler.sendMessage(message);
                                }
                            }
                        }).start();
                    }
                }
                else{
                    animator.pause();
                    isplay = false;
                    play.setBackgroundResource(R.mipmap.play);
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause(); // 暂停播放
                    }
                }
                break;
//            case R.id.button_pause:
//                if (mediaPlayer.isPlaying()) {
//                    mediaPlayer.pause(); // 暂停播放
//                }
//                break;

            case R.id.last:
                if(isplay){
                    play.setBackgroundResource(R.mipmap.play);
                    isplay = false;
                }
                    animator.pause();
                    //mediaPlayer.reset(); // 停止播放
                    if((index != 0) && (index != -1)){
                        mediaPlayer.reset();
                        Play(data.get(index - 1));
                        index = index - 1;
                        music_name.setText(data.get(index));
                        Toast.makeText(this, "切换至上一首", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        mediaPlayer.pause();
                        Toast.makeText(this, "已经是第一首", Toast.LENGTH_SHORT).show();
                    }
                break;
            case R.id.next:
                if(isplay){
                    play.setBackgroundResource(R.mipmap.play);
                    isplay = false;
                }
                animator.pause();
                    //mediaPlayer.reset(); // 停止播放
                    if((index != -1) && (index != data.size() - 1)){
                        mediaPlayer.reset();
                        Play(data.get(index + 1));
                        index = index + 1;
                        music_name.setText(data.get(index));
                        Toast.makeText(this, "切换至下一首", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        mediaPlayer.pause();
                        Toast.makeText(this, "已经是最后一首", Toast.LENGTH_SHORT).show();
                    }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    }