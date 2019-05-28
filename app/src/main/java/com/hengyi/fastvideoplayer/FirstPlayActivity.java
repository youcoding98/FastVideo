package com.hengyi.fastvideoplayer;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;

public class FirstPlayActivity extends AppCompatActivity implements SurfaceHolder.Callback
{

    private VideoView surfaceView;

    private SurfaceHolder holder;

    private Button button_start,button_end,button_full;

    private TextView tvSound, tvSound1,tvCurrentT, tvDuration;

    private ProgressBar progressBar;

    private MediaPlayer mediaPlayer;

    private Uri uri;

    private Handler handler;

    private float downX, downY;//手指刚开始滑动时记录点 X轴 Y轴

    private int screenWidth;

    private int FACTOR = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_first_play);
        //TODO　将屏幕设置为横屏()
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //TODO 将屏幕设置为竖屏()
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        surfaceView = (VideoView) findViewById(R.id.surface_view);
        surfaceView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN://手指按下
                        downX = event.getX();//按下时记录相关值
                        downY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE://手指滑动
                        // TODO 音量
                        float distanceX = event.getX() - downX;//滑动距离
                        float distanceY = event.getY() - downY;
                        //右半边屏幕，向下滑动则减小音量
                        if (downX > 0.75*screenWidth
                                && Math.abs(distanceX) < 50
                                && distanceY > FACTOR)
                        {
                            // TODO 减小音量
                            setVolume(false);
                        }
                        else if (downX > 0.75*screenWidth
                                && Math.abs(distanceX) < 50
                                && distanceY < -FACTOR)
                        {
                            // TODO 增加音量
                            setVolume(true);

                        }
                        if (downX  < 0.25*screenWidth && Math.abs(distanceX) < 50 && distanceY > 100) {
                            // TODO 减小亮度
                            setBrightness(-6);
                        }
                        else if ( downX  < 0.25*screenWidth && Math.abs(distanceX) < 50 && distanceY < -100) {
                            // TODO 增加亮度
                            setBrightness(6);
                        }
                        // TODO 播放进度调节
                        if (Math.abs(distanceY) < 50 && distanceX > FACTOR)
                        {
                            // TODO 快进
                            int currentT = mediaPlayer.getCurrentPosition();//播放的位置
                            mediaPlayer.seekTo(currentT + 15000);
                            downX = event.getX();
                            downY = event.getY();
                            Log.i("info", "distanceX快进=" + distanceX);
                        }
                        else if (Math.abs(distanceY) < 50
                                && distanceX < -FACTOR)
                        {
                            // TODO 快退
                            int currentT = mediaPlayer.getCurrentPosition();
                            mediaPlayer.seekTo(currentT - 15000);
                            downX = event.getX();
                            downY = event.getY();
                            Log.i("info", "distanceX=" + distanceX);
                        }
                        break;
                }
                return true;
            }
        });
        holder = surfaceView.getHolder();
        holder.addCallback(this);
        tvSound = (TextView) findViewById(R.id.tv_sound);
        tvSound1 = (TextView) findViewById(R.id.tv_sound1);
        tvCurrentT = (TextView) findViewById(R.id.tv_current);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        mediaPlayer = new MediaPlayer();
        //TODO 给viedeoview设置播放源（通过资源文件来设置），PS 在调用资源文件时，在协议头后加上如“://”
        uri = Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.video1);//TODO 在raw下添加video1视频（）
        //String videoUrl = " http://mirror.aarnet.edu.au/pub/TED-talks/911Mothers_2010W-480p.mp4" ;
        //uri = Uri.parse(videoUrl);
        handler = new Handler();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                // 开始播放视频
                mediaPlayer.start();
                // 设置总时长
                tvDuration.setText(mp.getDuration() / 1000 + "");
                tvCurrentT.setText(mp.getCurrentPosition() / 1000 + "");
                progressBar.setMax(mp.getDuration());
                updateView();
            }
        });

        mediaPlayer
                .setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                {
                    @Override
                    public void onCompletion(MediaPlayer mp)
                    {
                        mediaPlayer.start();
                    }
                });
        initView();
    }

    /////////////////////////////////
    private void initView(){
        surfaceView = (VideoView) findViewById(R.id.surface_view);
        button_start= (Button) findViewById(R.id.button_start);
        button_end= (Button) findViewById(R.id.button_end);
        button_full= (Button) findViewById(R.id.button_full);

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //init();
                mediaPlayer.start();
                //mediaPlayer.
            }
        });
        button_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //surfaceView.stopPlayback();
                //mediaPlayer.stop();
                mediaPlayer.pause();
            }
        });
        button_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullScreenChange();

            }
        });
    }
    //
    public void fullScreenChange() {
        SharedPreferences mPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        boolean fullScreen = mPreferences.getBoolean("fullScreen", false);
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        System.out.println("fullScreen的值:" + fullScreen);
        if (fullScreen) {
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            // 取消全屏设置
            getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            mPreferences.edit().putBoolean("fullScreen", false).commit();
        } else {
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            mPreferences.edit().putBoolean("fullScreen", true).commit();
        }
    }



    // 设置屏幕亮度 lp = 0 最暗 lp = 1 最亮
    private void setBrightness( float brightness) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = lp.screenBrightness + brightness / 255.0f;
        if (lp.screenBrightness > 1) {
            lp.screenBrightness = 1;
        } else if ( lp.screenBrightness < 0.1) {
            lp.screenBrightness = (float) 0.1;
        }
        getWindow().setAttributes(lp);

        float sb = lp.screenBrightness;
        tvSound1.setVisibility(View.VISIBLE);
        tvSound1.setText("亮度：" + (int) Math.ceil(sb * 100) + "%");
        handler.postDelayed(new Runnable()//可以创建多线程消息的函数
        {
            @Override//每一秒实现一次的定时器操作
            public void run()
            {
                tvSound1.setVisibility(View.GONE);
            }
        }, 1000);


    }
    private void setVolume(boolean flag)
    {
        // 获取音量管理器
        AudioManager manager = (AudioManager) getSystemService(AUDIO_SERVICE);
        // 获取当前音量
        int curretnV = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (flag)
        {
            curretnV++;
        }
        else
        {
            curretnV--;
        }
        manager.setStreamVolume(AudioManager.STREAM_MUSIC, curretnV,
                AudioManager.FLAG_SHOW_UI);
        tvSound.setVisibility(View.VISIBLE);
        tvSound.setText("音量:" + curretnV);
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                tvSound.setVisibility(View.GONE);
            }
        }, 1000);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        // 当surfaceView被创建完成之前才能绘制画布,所以只能在此回调方法之后开始播放
        try
        {
            // 1.指定播放源
            mediaPlayer.setDataSource(this, uri);
            // 2.将mediaplayer和surfaceView时行绑定
            mediaPlayer.setDisplay(holder);
            // 3.准备进行异步播放(当prepareAsync被调用后会执行mediaPlayer的onPrepared回调方法)
            mediaPlayer.prepareAsync();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        try
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 更新播放进度的递归
     */
    private void updateView()
    {
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                // TODO 设置进度控件
                if (mediaPlayer != null && mediaPlayer.isPlaying())
                {
                    tvCurrentT.setText("进度:" + mediaPlayer.getCurrentPosition()
                            / 1000);
                    progressBar.setProgress(mediaPlayer.getCurrentPosition());
                }
                updateView();
            }
        }, 1000);
    }
}

