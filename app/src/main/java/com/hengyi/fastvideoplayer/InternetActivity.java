package com.hengyi.fastvideoplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import pl.droidsonroids.gif.GifImageView;

public class InternetActivity extends AppCompatActivity {
    private VideoView videoView1;
    private ImageButton imageButton_sureInternetVideo;
    static Uri mediaUri; // 视频路径
    private GifImageView loadingGif; // 等待动态图的加载
    final String TAG = "测试 PlayerActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);

        //
        videoView1 = (VideoView) findViewById(R.id.mVideoView);

        // 网络视频地址跳转按钮点击
        imageButton_sureInternetVideo = (ImageButton)findViewById(R.id.id_internetSure);
        imageButton_sureInternetVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaUri = Uri.parse(((EditText) findViewById(R.id.id_internetUri)).getText().toString());
                Log.w(TAG, "点中网络地址确定按钮..");
                videoView1.setMediaController(new MediaController(InternetActivity.this));
                videoView1.setVideoURI(mediaUri);
                videoView1.requestFocus(); // 获取焦点
                videoView1.start();
                //imageButton_pause.setImageDrawable( getDrawable(R.drawable.ic_pause));
                //isInternetMedia = true;
                //internetControlBar.setVisibility(View.GONE);
                loadingGif.setVisibility(View.VISIBLE);

            }
        });

        // 视频缓冲监听
        loadingGif = (GifImageView) findViewById(R.id.id_loading_gif);
        videoView1.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if(what == MediaPlayer.MEDIA_INFO_BUFFERING_START){
                    //Toast.makeText(MainActivity.this, "正在缓冲", Toast.LENGTH_LONG).show();

                    loadingGif.setVisibility(View.VISIBLE);
                    Log.w(TAG,"正在缓冲");
                }else if(what == MediaPlayer.MEDIA_INFO_BUFFERING_END){
                    //此接口每次回调完START就回调END,若不加上判断就会出现缓冲图标一闪一闪的卡顿现象
                    if(mp.isPlaying()){
                        //Toast.makeText(MainActivity.this, "缓冲结束", Toast.LENGTH_LONG).show();
                        loadingGif.setVisibility(View.GONE);
                        Log.w(TAG,"缓冲结束");
                        videoView1.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }
        });

        // 加载网络资源黑屏结束监听
        videoView1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                loadingGif.setVisibility(View.GONE);
            }
        });

        // 视频完成监听
        videoView1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(InternetActivity.this, "视频结束", Toast.LENGTH_SHORT).show();
                Log.w(TAG,"播放完成了");
                loadingGif.setVisibility(View.GONE);
                //videoView.start();
            }
        });

    }
}

