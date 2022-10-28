package com.filepreview.application.util;

import android.content.Context;

import com.filepreview.application.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * load album filter
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/21
 */
public class AlbumUtil {
    //    public static final int COUNT = 4;
    private static HashMap<String, String> convertData;
    protected static List<String> defaultBuckets = Arrays.asList("Camera", "Screenshots", "WeiXin", "bluetooth");

    public static int getCapacity(int count) {
        return (int) (count / 0.75) + 1;
    }

    public static void initData(Context context) {
        if (convertData == null) {
            convertData = new HashMap<>(getCapacity(5));
            convertData.put("bluetooth", context.getString(R.string.bluetooth));
            convertData.put("Camera", context.getString(R.string.camera));
            convertData.put("Screenshots", context.getString(R.string.shortcut));
            convertData.put("Video", context.getString(R.string.video));
            convertData.put("WeiXin", context.getString(R.string.wechat));
        }
    }

    public static String getConvertKey(String key) {
        if (convertData == null) {
            return key;
        } else {
            return convertData.get(key);
        }
    }

    public static String getValue(String key) {
        if (convertData == null || !convertData.containsValue(key)) {
            return key;
        }

        Iterator<Map.Entry<String, String>> iterator = convertData.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if (entry.getValue().equals(key)) {
                return entry.getKey();
            }
        }
        return key;
    }
}
