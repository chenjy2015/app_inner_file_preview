package com.filepreview.application.constant;

import android.annotation.SuppressLint;


/**
 * static constants
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/28
 */
public class ConstValue {

    public static final String BROWSER_URL = "browser_url";
    public static final String HTML_CONTENT = "html_content";
    public static final String FILE_INFO = "file_info";
    public static final String FILE_PATH = "file_path";
    public static final String OUT_FILE_LIST = "out_file_list";
    public static final String OUT_FILE_TYPE = "out_file_type";
    public static final int DOCUMENT = 0;
    public static final int PICTURE = 1;
    @SuppressLint("SdCardPath")
    public static final String OUT_DOCUMENT_FILE_PATH_ROOT = "/sdcard/Download/Document/";
    @SuppressLint("SdCardPath")
    public static final String OUT_PICTURE_FILE_PATH_ROOT = "/sdcard/Download/Picture/";
}
