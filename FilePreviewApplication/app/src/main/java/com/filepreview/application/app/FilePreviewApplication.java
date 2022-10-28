package com.filepreview.application.app;

import android.app.Application;

import com.filepreview.application.util.VibratorUtil;


/**
 * application
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/21
 */
public class FilePreviewApplication extends Application {

    public static String TAG = FilePreviewApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        VibratorUtil.getInstance().init(this);
    }
}
