package com.filepreview.application.util;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;


/**
 * vibrator util
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/28
 */
public class VibratorUtil {

    Vibrator vibrator;

    private static VibratorUtil INSTANCE;

    private long[] pattern = {100, 50, 50};

    private VibratorUtil() {
    }

    public static VibratorUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VibratorUtil();
        }
        return INSTANCE;
    }

    public void init(Context context) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            VibrationEffect vibrationEffect = VibrationEffect.createWaveform(pattern, VibrationEffect.DEFAULT_AMPLITUDE);
            vibrator.vibrate(vibrationEffect);
        } else {
            vibrator.vibrate(pattern, 0);
        }
    }

    public void vibrate(long[] pattern) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            VibrationEffect vibrationEffect = VibrationEffect.createWaveform(pattern, VibrationEffect.DEFAULT_AMPLITUDE);
            vibrator.vibrate(vibrationEffect);
        } else {
            vibrator.vibrate(pattern, 0);
        }
    }
}
