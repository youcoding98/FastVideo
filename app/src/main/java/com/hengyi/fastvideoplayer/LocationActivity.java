package com.hengyi.fastvideoplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class LocationActivity extends AppCompatActivity {
    private Button mButton;
    private VideoView videoView; // 播放器控件
    static Uri mediaUri;  // 视频路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }

        });
    }


    // 从媒体管理器返回
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        videoView = (VideoView)findViewById(R.id.videoView);  // 播放器
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                mediaUri = data.getData();
                videoView.setVideoURI(mediaUri);
                videoView.start();
                //isInternetMedia = false;
            }
        }
    }
}
