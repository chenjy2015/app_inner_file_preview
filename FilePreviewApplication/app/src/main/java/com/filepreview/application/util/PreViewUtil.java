package com.filepreview.application.util;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.filepreview.application.activity.BrowserActivity;
import com.filepreview.application.activity.PDFPreviewActivity;
import com.filepreview.application.office.ExcelToHtml;
import com.filepreview.application.office.WordToHtml;

import java.io.File;
import java.util.Objects;


/**
 * File Preview Business Processing
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/27
 */
public class PreViewUtil {

    public static void open(Context context, String filePath) {
        open(context, new File(filePath));
    }

    public static void open(Context context, File file) {
        MediaFileUtil.MediaFileType type = MediaFileUtil.getFileType(file.getPath());
        if (type == null) {
            return;
        }
        //text file
        if (MediaFileUtil.isTextFileType(type.fileType)) {
            if (Objects.requireNonNull(MediaFileUtil.getFileType(file.getPath())).fileType == MediaFileUtil.FILE_TYPE_HTML) {
                //html
                Log.d("html", file.getPath());
                BrowserActivity.launch(context, getFileLoadHead() + file.getPath());
            } else if (Objects.requireNonNull(MediaFileUtil.getFileType(file.getPath())).fileType == MediaFileUtil.FILE_TYPE_PDF) {
                //pdf
                Log.d("pdf", file.getPath());
                PDFPreviewActivity.launch(context, file.getPath());
            } else if (MediaFileUtil.isExcelFileType(type.fileType)) {
                //excel
                String htmlPath = ExcelToHtml.readExcelToHtml(file.getPath());
                Log.d("ExcelToHtml", htmlPath);
                BrowserActivity.launchHtml(context, getFileLoadHead() + htmlPath);
            } else if (MediaFileUtil.isWordFileType(type.fileType)) {
                //word
                WordToHtml wordToHtml = new WordToHtml(file);
                Log.d("wordToHtml", wordToHtml.getContent());
                BrowserActivity.launch(context, getFileLoadHead() + wordToHtml.htmlPath);
            } else if (type.fileType == MediaFileUtil.FILE_TYPE_TXT) {
                Log.d("Txt", file.getPath());
                BrowserActivity.launch(context, getFileLoadHead() + file.getPath());
//                TxtActivity.launch(context, FileUtil.readTxtFile(file.getPath()));
            }
        }
    }

    /**
     * note : just use by html,excel,doc filetype
     * the pdf file has special invoke
     * @return
     */
    private static String getFileLoadHead() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            return "file://";
        }
        return "";
    }
}
