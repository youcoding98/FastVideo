package com.hengyi.fastvideoplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button btn_start,btn_start_2,btn_start_3;
    private ImageButton btn_location,btn_internet,btn_share,btn_camera,btn_cut,btn_people;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initInternet();
        initLogin();
        initCamera();
        initShare();
        initLocation();

    }
    private void initView() {
        imageView= (ImageView) findViewById(R.id.imageView);
        btn_start= (Button) findViewById(R.id.btn_start);
        btn_start_2= (Button) findViewById(R.id.btn_start_2);
        btn_start_3= (Button) findViewById(R.id.btn_start_3);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到
                Intent intent = new Intent(MainActivity.this,FirstPlayActivity.class);
                startActivity(intent);
            }
        });
        btn_start_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到
                Intent intent = new Intent(MainActivity.this,InternetActivity.class);
                startActivity(intent);
            }
        });
        btn_start_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到
                Intent intent = new Intent(MainActivity.this,LiveActivity.class);
                startActivity(intent);
            }
        });

    }
    private void initInternet(){
        btn_internet= (ImageButton)findViewById(R.id.btn_internet);
        btn_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到
                Intent intent1 = new Intent(MainActivity.this,InternetActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void initLogin(){
        btn_people= (ImageButton)findViewById(R.id.btn_people);
        btn_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到
                Intent intent1 = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void initCamera(){
        btn_camera= (ImageButton)findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到
                Intent intent = new Intent(MainActivity.this,CameraActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initShare(){
        btn_share= (ImageButton)findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到
                Intent textIntent = new Intent(Intent.ACTION_SEND);
                textIntent.setType("text/plain");
                textIntent.putExtra(Intent.EXTRA_TEXT, "这是一段分享的文字");
                startActivity(Intent.createChooser(textIntent, "分享"));
            }
        });
    }
    private void initLocation(){
        btn_location= (ImageButton)findViewById(R.id.btn_location);
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到
                Intent intent1 = new Intent(MainActivity.this,LocationActivity.class);
                startActivity(intent1);
            }
        });
    }



}