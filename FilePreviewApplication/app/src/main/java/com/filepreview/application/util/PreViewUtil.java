package com.filepreview.application.util;

import android.content.Context;
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
                BrowserActivity.launch(context, file.getPath());
            } else if (Objects.requireNonNull(MediaFileUtil.getFileType(file.getPath())).fileType == MediaFileUtil.FILE_TYPE_PDF) {
                //pdf
                PDFPreviewActivity.launch(context, file.getPath());
            } else if (MediaFileUtil.isExcelFileType(type.fileType)) {
                //excel
                String htmlPath = ExcelToHtml.readExcelToHtml(file.getPath());
                Log.d("ExcelToHtml", htmlPath);
                BrowserActivity.launchHtml(context, htmlPath);
            } else if (MediaFileUtil.isWordFileType(type.fileType)) {
                //word
                WordToHtml wordToHtml = new WordToHtml(file);
                Log.d("wordToHtml", wordToHtml.getContent());
                BrowserActivity.launch(context, wordToHtml.htmlPath);
            } else if (type.fileType == MediaFileUtil.FILE_TYPE_TXT) {
                Log.d("Txt", file.getName());
                BrowserActivity.launch(context, file.getPath());
//                TxtActivity.launch(context, FileUtil.readTxtFile(file.getPath()));
            }
        }
    }
}
