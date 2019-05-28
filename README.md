<<<<<<< HEAD
# 基于Android平台的移动流媒体播放器的开发
主页界面如下  
<img src="https://github.com/youcoding98/videoplay/blob/master/Picture/%E4%B8%BB%E9%A1%B5.png" height="450" width="250" align=center />

## 第一部分 课题相关介绍
与普通播放器相比，流媒体播放器最主要的不同点在于其能够实现实时的视频播放，用户可以实现边加载边播放，不需要一次全下载完视频。
#### 流媒体技术
流媒体技术简单来说就是将完整视频先行数据压缩，再分段发送数据，实时传播时由于容量较小，传输就非常快速，可以基本实现实时浏览的一种技术。
简单概括为：采用了"流式传输"技术，文件象水流那样流动
流媒体主要分为两种：  
1.顺序流式传输 HTTP渐进下载  
在下载文件的同时用户可观看在线媒体，由于标准的HTTP服务器可发送这种形式的文件，它经常被称作HTTP流式传输。顺序流式文件易于管理，但不支持现场直播，严格地说是一种点播技术。  
HTTP网络视频的实现  
2.实时流式传输 基于RTSP/RTP的实时流媒体协议  
需要专用的流媒体服务器与传输协议。实时流式传输总是实时传送，特别适合现场事件，可用于直播等的实现。  

#### 平台选择
Android平台的优点  
目前移动手机市场

## 第二部分 系统分析及相应实现
首先明确系统功能以及性能需求，之后对系统进行总体设计，将系统分为视频采集模块，视频播放模块以及视频裁剪模块。
#### 需求分析
1.基本需求：视频播放     
（1）本地视频播放    
（2）视频点播     
（3）视频直播     
2.界面美观，操作简便      
（1）实现动态切换全屏/原屏、横屏/竖屏的自由切换     
（2）监控视频播放器窗口的滑动，实现亮度加减，音量加减，快进和快退功能        
（3）本地视频资源管理   
#### 采集子系统
Android6.0发布以来，在权限上做出了很大的变动，不再是之前的只要在manifest设置就可以任意获取权限，不会再强迫用户因拒绝不该拥有的权限而导致的无法安装的事情，也不会再不征求用户授权的情况下，就可以任意的访问用户隐私，而且即使在授权之后也可以及时的更改权限。
```
     //对于6.0以后的机器动态权限申请
    public void Accessibility() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA);
            int checkCallPhonePermission2 = ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int checkCallPhonePermission3 = ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.RECORD_AUDIO);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED &&checkCallPhonePermission2 != PackageManager.PERMISSION_GRANTED && checkCallPhonePermission3 != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_ASK_CALL_PHONE);
                return;
            } else {
                initViews();

            }
        } else {
            initViews();
        }
    }

```

1.初始化设置  
采集过程主要是设置 MediaRecorder 类的一些参数和采集数据的缓存，使用了 Android 系统自带的MediaRecorder 类进行采集。  
 定义一个录制视频的 MediaRecorder 子类 mRecorder，指定录制的采集方式、文件的输出方式以及编码格式，录音方式采用AMR_NB 编码，视频采用H.264编码。  
```
     mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
     mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
     mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
     mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
     mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
```
2.设置输出文件的本地路径  
```
     String path = Environment.getExternalStorageDirectory().getPath();
     if (path != null) {
         File dir = new File(path + "/360");
         if (!dir.exists()) {
             dir.mkdir();
             }
     path = dir + "/" + System.currentTimeMillis() + ".mp4";
     mRecorder.setOutputFile(path);
```
3.开始录制   
调用 prepare 方法，准备采集,prepare 方法将使用 Android 系统自带的本地 LocalSocket 和 LocalServerSocket 进行数据的缓存,然后使用start方法开始录制
```
     mRecorder.prepare();
     mRecorder.start();  //开始录制
```
4.停止录制，释放MediaRecorder以及相机资源   
<img src="https://github.com/youcoding98/videoplay/blob/master/Picture/%E5%BD%95%E5%83%8F.png" height="450" width="250" align=center />
#### 播放子系统
1.本地视频播放    
使用surfaceview＋MediaPlayer自定义播放器 播放本地视频   
surfaceView创建完成再开始播放视频
surfaceCreated()  
surfaceChanged()  
surfaceDestroyed()  
```
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
```
<img src="https://github.com/youcoding98/videoplay/blob/master/Picture/%E6%9C%AC%E5%9C%B0.png" height="450" width="250" align=center />
2.视频点播：被观众任意观看，每次都可从头播放   
播放控件使用Android自带的 VideoView 控件，采用MediaController媒体控制器实现播放   
(1)视频缓冲Gif加载     
```
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
```
(2)网络视频地址跳转播放   
测试地址:  
rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov  
http://mirror.aarnet.edu.au/pub/TED-talks/911Mothers_2010W-480p.mp4  
```
     //输入网络视频地址
     mediaUri=Uri.parse(((EditText)findViewById(R.id.id_internetUri)).getText().toString());
     videoView1.setMediaController(new MediaController(InternetActivity.this));
     videoView1.setVideoURI(mediaUri);
     videoView1.requestFocus(); // 获取焦点
     videoView1.start();

```  
<img src="https://github.com/youcoding98/videoplay/blob/master/Picture/%E7%82%B9%E6%92%AD.png" height="450" width="250" align=center />
3.视频直播: 正在发生的画面     
(1) ijkplayer简化    
android studio集成ijkplayer，将其作为moudle导入我们需要使用播放器的工程project中，由于要实现简单的网络直播播放功能，这里生成fastvideoplay来将ijkplay的功能进行简化    
IjkVideoView 这个类是使用ijkplayer播放的View，这里为了操作方便，直接使用IjkVideoView作为直播的播放器
```
     <com.hengyi.fastvideoplayer.library.mediaplayer.IjkVideoView
        android:id="@+id/video_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```
初始化视图    
```
       try {
			IjkMediaPlayer.loadLibrariesOnce(null);
			IjkMediaPlayer.native_profileBegin("libijkplayer.so");
			playerSupport = true;
		} catch (Throwable e) {
			Log.e("GiraffePlayer", "loadLibraries error", e);
		}
```
(2) 网络直播视频处理
```
 	videoPlayer = findViewById(R.id.fastvideo_player);
        play = findViewById(R.id.play);
        videoPlayer.setLive(true);
        videoPlayer.setScaleType(FastVideoPlayer.SCALETYPE_FITXY);
        videoPlayer.setTitle("CCTV5直播");//设置标题
        videoPlayer.setUrl("http://ivi.bupt.edu.cn/hls/cctv5phd.m3u8");
```

<img src="https://github.com/youcoding98/videoplay/blob/master/Picture/%E7%9B%B4%E6%92%AD.png" height="450" width="250" align=center />
4.播放功能的优化  
（1）监控视频播放窗口的滑动，实现亮度加减，音量加减，快进和快退功能  
以音量加减举例：   
```
     private float downX, downY;//手指刚开始滑动时记录点 X轴 Y轴
     
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
                        //减小音量条件：右半边屏幕，滑动距离一定且向下滑动
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
```   
音量管理函数    
```
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

```   
（2）动态切换全屏/原屏、横屏/竖屏的自由切换。       
利用全屏/原屏切换函数，即可自由切换全/原屏    
```
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
```  
对于横竖屏的切换：设置权限，在AndroidManifest.xml中对Activity属性进行设置android:configChanges属性   
```
     android:configChanges="keyboardHidden|orientation|screenSize"
```
(3)本地视频资源管理  
在Activity Action中有一个“ACTION_ GET_ CONTENT”字符串常量。 此常量允许用户选择特定类型的数据并返回数据的URI。 我们利用这个常量，然后将类型设置为 video / * 以获取Android手机中的所有视频。   
```
     Intent intent = new Intent();
     intent.setType("video/*");
     intent.setAction(Intent.ACTION_GET_CONTENT);
     startActivityForResult(intent, 1);
```
之后从媒体管理器返回，利用mediaUri = data.getData()获取本地视频文件路径，然后在视频播放端使用VideoView进行播放。  
#### 裁剪子系统
代码详见：https://github.com/iknow4/Android-Video-Trimmer   
(1)FFmpeg移植到Android平台   
在本项目开发中采用Java封装FFmpeg命令行，而没有采用一般的Java+C语言来进行开发，使之在Android项目中轻松执行FFmpeg和FFprobe命令。采用开源项目Bravobit FFmpeg-Android加载FFmpeg库：

(2)裁剪功能的实现   
=======
# FastVideoPlayer
基于ijkplayer开发万能播放器，支持点播、直播播放。

![](https://github.com/fanhua1994/FastVideoPlayer/blob/master/image/16CDF9C1CDBD0E54F934E532C8351A5B.jpg?raw=true)
![](https://github.com/fanhua1994/FastVideoPlayer/blob/master/image/B43F89B4D9B5AE374BB9AE8A966D2006.png?raw=true)


# 如何引用
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
```
dependencies {
        compile 'com.github.fanhua1994:FastVideoPlayer:1.0.6'
}
```

# demo演示
AndroidMainist.xml
```
 <activity android:name=".MainActivity"
    android:configChanges="orientation|screenSize|keyboardHidden">
</activity>
```

```
<com.hengyi.fastvideoplayer.library.FastVideoPlayer
    android:id="@+id/fastvideo_player"
    android:layout_width="match_parent"
    android:layout_height="200dp"></com.hengyi.fastvideoplayer.library.FastVideoPlayer>
```

```
videoPlayer = findViewById(R.id.fastvideo_player);
videoPlayer.setLive(false);//是直播还是点播  false为点播
videoPlayer.setScaleType(FastVideoPlayer.SCALETYPE_FITXY);
videoPlayer.setTitle("TiDB宣传视频");//设置标题
videoPlayer.setUrl("https://download.pingcap.com/videos/pingcap-intro-converted.mp4");
videoPlayer.play();//自动播放
//封面图加载
Glide.with(this).load("https://download.pingcap.com/images/video-poster.jpg").into(videoPlayer.getCoverImage());
```
屏幕监听
```
videoPlayer.setScreenListener(new FastVideoPlayerScreenListener() {
    @Override
    public void onFullScreen() {
        Toast.makeText(MainActivity.this,"进入全屏",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSmallScreen() {
        Toast.makeText(MainActivity.this,"进入小屏",Toast.LENGTH_SHORT).show();
    }
});
```
```
/**
 * 下面的这几个Activity的生命状态很重要
 */
@Override
protected void onPause() {
    super.onPause();
    if (videoPlayer != null) {
        videoPlayer.onPause();
    }
}

@Override
protected void onResume() {
    super.onResume();
    if (videoPlayer != null) {
        videoPlayer.onResume();
    }
}

@Override
protected void onDestroy() {
    super.onDestroy();
    if (videoPlayer != null) {
        videoPlayer.onDestroy();
    }
}

@Override
public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    if (videoPlayer != null) {
        videoPlayer.onConfigurationChanged(newConfig);
    }
}

@Override
public void onBackPressed() {
    if (videoPlayer != null && videoPlayer.onBackPressed()) {
        return;
    }
    super.onBackPressed();
}
```
>>>>>>> 第一次通过git修改文件并提交到仓库
