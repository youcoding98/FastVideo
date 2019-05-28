package com.hengyi.fastvideoplayer.library;

import android.content.Context;

public class FastVideoPlayerManage {
    public static FastVideoPlayerManage videoPlayViewManage;
    private FastVideoPlayer videoPlayView;

    private FastVideoPlayerManage() {

    }

    public static FastVideoPlayerManage getSuperManage() {
        if (videoPlayViewManage == null) {
            videoPlayViewManage = new FastVideoPlayerManage();
        }
        return videoPlayViewManage;
    }

    public FastVideoPlayer initialize(Context context) {
        if (videoPlayView == null) {
            videoPlayView = new FastVideoPlayer(context);
        }
        return videoPlayView;
    }
}
