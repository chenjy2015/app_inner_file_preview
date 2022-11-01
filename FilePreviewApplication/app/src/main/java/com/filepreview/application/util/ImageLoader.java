package com.filepreview.application.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;

import com.filepreview.application.R;
import com.filepreview.application.bean.FileVO;
import com.filepreview.application.services.AlbumThreadPoolExecutor;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;


/**
 * file load util
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/28
 */
public class ImageLoader {
    private static final String TAG = "ImageLoader";
    private static LruCache<String, Bitmap> bitmapLruCache;
    private static int minWidth, maxWidth;
    private static ArrayList<String> buckets;//Image bucket to be retrieved on the first page of the album
    public static final String KEY = "files";

    public static ImageLoader getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ImageLoader INSTANCE = new ImageLoader();
    }

    private ImageLoader() {
        if (bitmapLruCache == null) {
            int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取最大的运行内存
            int maxSize = maxMemory / 8;
            bitmapLruCache = new LruCache<>(maxSize);
        }
        if (buckets == null) {
            buckets = new ArrayList<>();
        } else {
            buckets.clear();
        }
        buckets.addAll(AlbumUtil.defaultBuckets);
    }

    public static void setScale(int width) {
        // The width of the large preview picture is one quarter of the screen width
        maxWidth = width >> 2;
        // The width of the small preview picture is one eighth of the screen width
        minWidth = maxWidth >> 1;
    }

    public interface UpdateUI {
        void updateUI(Bundle bundle);
    }

    public FileInputStream getFileInputStream(String path) {
        try {
            return new FileInputStream(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void loadVideoDetails(final Context context, final UpdateUI update) {
        AlbumThreadPoolExecutor executor = AlbumThreadPoolExecutor.getDefaultThreadPoolExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                loadVideoDetail(context, update);
            }
        });
    }

    private void loadVideoDetail(Context context, UpdateUI update) {
        Bundle bundle = new Bundle();
        String[] videoProjection = {Video.Media.TITLE, Video.Media.DATA, Video.Media.DURATION, Video.Media.DISPLAY_NAME};
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Video.Media.EXTERNAL_CONTENT_URI, videoProjection,
                null, null, Video.Media.DATE_MODIFIED + " desc");
        if (cursor != null) {
            ArrayList<FileVO> fileVOS = new ArrayList<>();
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(Video.Media.DATA));
                int duration = cursor.getInt(cursor.getColumnIndex(Video.Media.DURATION));
                String title = cursor.getString(cursor.getColumnIndex(Video.Media.TITLE));
                String name = cursor.getString(cursor.getColumnIndex(Video.Media.DISPLAY_NAME));

                FileVO fileVO = new FileVO();
                fileVO.setFile(new File(path));
                fileVO.setDuration(duration);
                fileVOS.add(fileVO);
            }
            cursor.close();
            bundle.putSerializable(ImageLoader.KEY, fileVOS);
        }
        update.updateUI(bundle);
    }

    public void loadImages(final Context context, final String type, final UpdateUI update) {
        AlbumThreadPoolExecutor executor = AlbumThreadPoolExecutor.getDefaultThreadPoolExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = getImageCursor(context, type);
                if (cursor == null) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("type", type);
                ArrayList<FileVO> paths = new ArrayList<>(AlbumUtil.getCapacity(cursor.getCount()));
                String imagePath;
                int index = 0;
                while (cursor.moveToNext()) {
                    index++;
                    imagePath = cursor.getString(cursor.getColumnIndex(Images.Media.DATA));
                    FileVO fileVO = new FileVO();
                    fileVO.setFile(new File(imagePath));
                    paths.add(fileVO);

//                    if (index % 32 == 0) {
//                        update.updateUI(bundle);
//                    }
                }
                cursor.close();
                bundle.putSerializable(ImageLoader.KEY, paths);
                update.updateUI(bundle);
            }
        });
    }


    public boolean checkSDCardExist() {
        return (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED));
    }


    public void loadDocument(UpdateUI update) {
        if (!checkSDCardExist()) {
            update.updateUI(null);
            return;
        }
        File root = Environment.getExternalStorageDirectory();
//        File root = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "Download" + File.separator);
        ArrayList<FileVO> fileVOS = new ArrayList<>();
        AlbumThreadPoolExecutor executor = AlbumThreadPoolExecutor.getDefaultThreadPoolExecutor();
        executor.execute(() -> {
            getAllFiles(root, fileVOS);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ImageLoader.KEY, fileVOS);
            update.updateUI(bundle);
        });
    }

    public boolean checkFileIsDocument(File f) {
        Log.d("checkFileIsDocument", "name : " + f.getName() + "  path :  " + f.getPath());
//        return MediaFileUtil.isTextFileType(f.getPath());
        return !MediaFileUtil.isAudioFileType(f.getPath())
                && !MediaFileUtil.isVideoFileType(f.getPath())
                && !MediaFileUtil.isImageFileType(f.getPath())
                && !MediaFileUtil.isPlayListFileType(f.getPath());
    }

    /**
     * recursive lookup
     *
     * @param root
     * @param fileVOS
     */
    public void getAllFiles(File root, ArrayList<FileVO> fileVOS) {
        File files[] = root.listFiles();
        if (files != null) {
            for (File f : files) {
                Log.d("getAllFiles", "f :  " + f.getPath() + " name : " + f.getName());
                if (f.isFile() && checkFileIsDocument(f)) {
                    Log.d("fileVOS.add", "name : " + f.getName() + "  path :  " + f.getPath());
                    fileVOS.add(new FileVO(f, getDrawable(f.getPath())));
                } else if (f.isDirectory()) {
                    getAllFiles(f, fileVOS);
                }
            }
        }
    }

    public int getDrawable(String path) {
        MediaFileUtil.MediaFileType fileType = MediaFileUtil.getFileType(path);
        if (fileType != null) {
            if (fileType.fileType == MediaFileUtil.FILE_TYPE_XLS || fileType.fileType == MediaFileUtil.FILE_TYPE_XLSX) {
                return R.drawable.ic_excel;
            } else if (fileType.fileType == MediaFileUtil.FILE_TYPE_DOC || fileType.fileType == MediaFileUtil.FILE_TYPE_DOCX) {
                return R.drawable.ic_word;
            } else if (fileType.fileType == MediaFileUtil.FILE_TYPE_PPS || fileType.fileType == MediaFileUtil.FILE_TYPE_PPT || fileType.fileType == MediaFileUtil.FILE_TYPE_PPTX) {
                return R.drawable.ic_ppt;
            } else if (fileType.fileType == MediaFileUtil.FILE_TYPE_PDF) {
                return R.drawable.ic_pdf;
            } else if (fileType.fileType == MediaFileUtil.FILE_TYPE_TXT) {
                return R.drawable.ic_txt;
            } else if (fileType.fileType == MediaFileUtil.FILE_TYPE_HTM || fileType.fileType == MediaFileUtil.FILE_TYPE_HTML) {
                return R.drawable.ic_html;
            }
        }
        return R.drawable.ic_unknow;
    }

    public Bitmap getBitmap(final String path) {
        Bitmap bitmap = bitmapLruCache.get(path);
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeFile(path);
            if (bitmap != null) {
                bitmapLruCache.put(path, bitmap);
            }
        }
        return bitmap;
    }

    public Bitmap getBitmap(final String path, int width, int height) {
        Bitmap bitmap = bitmapLruCache.get(path);
        if (bitmap == null) {
            bitmap = getThumbBitmap(path, width, height);
            if (bitmap != null) {
                bitmapLruCache.put(path, bitmap);
            }
        }
        return bitmap;
    }

    public void updateCache(final String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        Bitmap bitmap = bitmapLruCache.get(path);
        if (bitmap == null) {
            bitmap = getThumbBitmap(path, minWidth, minWidth);
            if (bitmap != null) {
                bitmapLruCache.put(path, bitmap);
            }
        }
    }

    private Bitmap getThumbBitmap(String path, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int maxWidth, int maxHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        while ((width >>= 1) >= maxWidth && (height >>= 1) >= maxHeight) {
            inSampleSize <<= 1;
        }
        return inSampleSize;
    }

    public Bitmap setVideoThumb(MediaMetadataRetriever media, String path) {
        Bitmap bitmap = bitmapLruCache.get(path);
        if (bitmap == null) {
            media.setDataSource(path);
            bitmap = media.getFrameAtTime();
            if (bitmap != null) {
                bitmapLruCache.put(path, bitmap);
            }
        }

        return bitmap;
    }

    // The image bucket to be retrieved on the first page of the album gets
    // the names of all subdirectories under the path (including subdirectories of subdirectories)
    private void setBucket(String path) {
        File root = new File(path);
        if (root.exists()) {
            File[] files = root.listFiles();
            if (files.length > 0) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        buckets.add(file.getName());
                        setBucket(file.getPath());
                    }
                }
            }
        }
    }

    public Cursor getImageCursor(Context context) {
        setBucket("/storage/emulated/0/DCIM/Camera");
        String[] projection = {Images.Media.BUCKET_DISPLAY_NAME, Images.Media.DATA};
        String selection = getSelection();
        String[] selectionArgs = new String[buckets.size()];
        buckets.toArray(selectionArgs);
        String order = Images.Media.BUCKET_DISPLAY_NAME + "," + Images.Media.DATE_MODIFIED + " desc";

        return context.getContentResolver().query(Images.Media.EXTERNAL_CONTENT_URI, projection,
                selection, selectionArgs, order);
    }

    public Cursor getOtherImageCursor(Context context) {
        String[] projection = {Images.Media.BUCKET_DISPLAY_NAME, Images.Media.DATA};
        String selection = getOtherSelection();
        String[] selectionArgs = new String[buckets.size()];
        buckets.toArray(selectionArgs);
        String order = Images.Media.BUCKET_DISPLAY_NAME + "," + Images.Media.DATE_MODIFIED + " desc";

        return context.getContentResolver().query(Images.Media.EXTERNAL_CONTENT_URI, projection,
                selection, selectionArgs, order);
    }

    public Cursor getOtherImageCountCursor(Context context) {
        String[] projection = {"count(*)"};
        String selection = getOtherSelection();
        String[] selectionArgs = new String[buckets.size()];
        buckets.toArray(selectionArgs);

        return context.getContentResolver().query(Images.Media.EXTERNAL_CONTENT_URI, projection,
                selection, selectionArgs, null);
    }

    private String getSelection() {
        if (buckets != null) {
            StringBuilder builder = new StringBuilder("(");
            for (int i = 0; i < buckets.size() - 1; i++) {
                builder.append(Images.Media.BUCKET_DISPLAY_NAME + "=? or ");
            }
            builder.append(Images.Media.BUCKET_DISPLAY_NAME + "=?)");
            return builder.toString();
        }
        return null;
    }

    private String getOtherSelection() {
        if (buckets != null) {
            StringBuilder builder = new StringBuilder("(");
            for (int i = 0; i < buckets.size() - 1; i++) {
                builder.append(Images.Media.BUCKET_DISPLAY_NAME + "!=? and ");
            }
            builder.append(Images.Media.BUCKET_DISPLAY_NAME + "!=?)");
            return builder.toString();
        }
        return null;
    }

    public Cursor getImageCursor(Context context, String type) {
        String[] projection = {Images.Media.DATA};
        String selection = "(" + Images.Media.BUCKET_DISPLAY_NAME + "=? and ("
                + Images.Media.MIME_TYPE + "=? or "
                + Images.Media.MIME_TYPE + "=? or "
                + Images.Media.MIME_TYPE + "=?))";
        String[] selectionArgs = new String[]{type, "image/jpeg", "image/jpg", "image/png"};
        String order = Images.Media.DATE_MODIFIED + " desc";

        return context.getContentResolver().query(Images.Media.EXTERNAL_CONTENT_URI, projection,
                selection, selectionArgs, order);
    }
}
