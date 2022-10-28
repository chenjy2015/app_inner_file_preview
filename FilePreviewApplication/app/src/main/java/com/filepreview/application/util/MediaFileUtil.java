package com.filepreview.application.util;

import java.util.HashMap;
import java.util.Iterator;


/**
 * media file util
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/21
 */
public class MediaFileUtil {
    //Delete the address parameter to avoid wrong judgment when judging the type
    public static String removeParams(String url) {
        return url.replaceAll("\\?.*", "");
    }

    private static HashMap<String, MediaFileType> sFileTypeMap = new HashMap<String, MediaFileType>();
    private static HashMap<String, Integer> sMimeTypeMap = new HashMap<String, Integer>();

    public static String sFileExtensions;
    // Audio
    public static final int FILE_TYPE_MP3 = 1;
    public static final int FILE_TYPE_M4A = 2;
    public static final int FILE_TYPE_WAV = 3;
    public static final int FILE_TYPE_AMR = 4;
    public static final int FILE_TYPE_AWB = 5;
    public static final int FILE_TYPE_WMA = 6;
    public static final int FILE_TYPE_OGG = 7;
    private static final int FIRST_AUDIO_FILE_TYPE = FILE_TYPE_MP3;
    private static final int LAST_AUDIO_FILE_TYPE = FILE_TYPE_OGG;
    // MIDI
    public static final int FILE_TYPE_MID = 11;
    public static final int FILE_TYPE_SMF = 12;
    public static final int FILE_TYPE_IMY = 13;
    private static final int FIRST_MIDI_FILE_TYPE = FILE_TYPE_MID;
    private static final int LAST_MIDI_FILE_TYPE = FILE_TYPE_IMY;

    // Video
    public static final int FILE_TYPE_MP4 = 21;
    public static final int FILE_TYPE_M4V = 22;
    public static final int FILE_TYPE_3GPP = 23;
    public static final int FILE_TYPE_3GPP2 = 24;
    public static final int FILE_TYPE_WMV = 25;
    private static final int FIRST_VIDEO_FILE_TYPE = FILE_TYPE_MP4;
    private static final int LAST_VIDEO_FILE_TYPE = FILE_TYPE_WMV;
    // Image
    public static final int FILE_TYPE_JPEG = 31;
    public static final int FILE_TYPE_GIF = 32;
    public static final int FILE_TYPE_PNG = 33;
    public static final int FILE_TYPE_BMP = 34;
    public static final int FILE_TYPE_WBMP = 35;
    private static final int FIRST_IMAGE_FILE_TYPE = FILE_TYPE_JPEG;
    private static final int LAST_IMAGE_FILE_TYPE = FILE_TYPE_WBMP;

    //text
    public static final int FILE_TYPE_TXT = 41;
    public static final int FILE_TYPE_JAVA = 42;
    public static final int FILE_TYPE_PHP = 43;
    public static final int FILE_TYPE_C = 44;
    public static final int FILE_TYPE_H = 45;
    public static final int FILE_TYPE_LOG = 46;
    public static final int FILE_TYPE_SH = 47;
    public static final int FILE_TYPE_PDF = 48;
    public static final int FILE_TYPE_XML = 49;
    public static final int FILE_TYPE_CSS = 50;
    public static final int FILE_TYPE_HTML = 51;
    public static final int FILE_TYPE_HTM = 52;
    public static final int FILE_TYPE_XLS = 53;
    public static final int FILE_TYPE_XLSX = 54;
    public static final int FILE_TYPE_DOC = 55;
    public static final int FILE_TYPE_DOCX = 56;
    public static final int FILE_TYPE_PPS = 57;
    public static final int FILE_TYPE_PPT = 58;
    public static final int FILE_TYPE_PPTX = 59;
    private static final int FIRST_TEXT_FILE_TYPE = FILE_TYPE_TXT;
    private static final int LAST_TEXT_FILE_TYPE = FILE_TYPE_PPT;

    // Playlist
    public static final int FILE_TYPE_M3U = 61;
    public static final int FILE_TYPE_PLS = 62;
    public static final int FILE_TYPE_WPL = 63;
    private static final int FIRST_PLAYLIST_FILE_TYPE = FILE_TYPE_M3U;
    private static final int LAST_PLAYLIST_FILE_TYPE = FILE_TYPE_WPL;

    public static class MediaFileType {
        public int fileType;
        public String mimeType;

        MediaFileType(int fileType, String mimeType) {
            this.fileType = fileType;
            this.mimeType = mimeType;
        }
    }

    static void addFileType(String extension, int fileType, String mimeType) {
        sFileTypeMap.put(extension, new MediaFileType(fileType, mimeType));
        sMimeTypeMap.put(mimeType, new Integer(fileType));
    }

    static {
        addFileType("MP3", FILE_TYPE_MP3, "audio/mpeg");
        addFileType("M4A", FILE_TYPE_M4A, "audio/mp4");
        addFileType("WAV", FILE_TYPE_WAV, "audio/x-wav");
        addFileType("AMR", FILE_TYPE_AMR, "audio/amr");
        addFileType("AWB", FILE_TYPE_AWB, "audio/amr-wb");
        addFileType("WMA", FILE_TYPE_WMA, "audio/x-ms-wma");
        addFileType("OGG", FILE_TYPE_OGG, "application/ogg");
        addFileType("MID", FILE_TYPE_MID, "audio/midi");
        addFileType("XMF", FILE_TYPE_MID, "audio/midi");
        addFileType("RTTTL", FILE_TYPE_MID, "audio/midi");
        addFileType("SMF", FILE_TYPE_SMF, "audio/sp-midi");
        addFileType("IMY", FILE_TYPE_IMY, "audio/imelody");
        addFileType("MP4", FILE_TYPE_MP4, "video/mp4");
        addFileType("M4V", FILE_TYPE_M4V, "video/mp4");
        addFileType("3GP", FILE_TYPE_3GPP, "video/3gpp");
        addFileType("3GPP", FILE_TYPE_3GPP, "video/3gpp");
        addFileType("3G2", FILE_TYPE_3GPP2, "video/3gpp2");
        addFileType("3GPP2", FILE_TYPE_3GPP2, "video/3gpp2");
        addFileType("WMV", FILE_TYPE_WMV, "video/x-ms-wmv");
        addFileType("JPG", FILE_TYPE_JPEG, "image/jpeg");
        addFileType("JPEG", FILE_TYPE_JPEG, "image/jpeg");
        addFileType("GIF", FILE_TYPE_GIF, "image/gif");
        addFileType("PNG", FILE_TYPE_PNG, "image/png");
        addFileType("BMP", FILE_TYPE_BMP, "image/x-ms-bmp");
        addFileType("WBMP", FILE_TYPE_WBMP, "image/vnd.wap.wbmp");

        addFileType("TXT", FILE_TYPE_TXT, "text/plain");
        addFileType("JAVA", FILE_TYPE_JAVA, "text/plain");
        addFileType("PHP", FILE_TYPE_PHP, "text/plain");
        addFileType("C", FILE_TYPE_C, "text/plain");
        addFileType("H", FILE_TYPE_H, "text/plain");
        addFileType("LOG", FILE_TYPE_LOG, "text/plain");
        addFileType("SH", FILE_TYPE_SH, "text/plain");
        addFileType("PDF", FILE_TYPE_PDF, "application/pdf");
        addFileType("XML", FILE_TYPE_XML, "text/xml");
        addFileType("CSS", FILE_TYPE_CSS, "text/css");
        addFileType("HTML", FILE_TYPE_HTML, "text/html");
        addFileType("HTM", FILE_TYPE_HTM, "text/html");
        addFileType("XLS", FILE_TYPE_XLS, "application/vnd.ms-excel");
        addFileType("XLSX", FILE_TYPE_XLSX, "application/vnd.ms-excel");
        addFileType("DOC", FILE_TYPE_DOC, "application/msword");
        addFileType("DOCX", FILE_TYPE_DOCX, "application/msword");
        addFileType("PPS", FILE_TYPE_PPS, "pplication/vnd.ms-powerpoint");
        addFileType("PPT", FILE_TYPE_PPT, "pplication/vnd.ms-powerpoint");
        addFileType("PPTX", FILE_TYPE_PPTX, "pplication/vnd.ms-powerpoint");

        addFileType("M3U", FILE_TYPE_M3U, "audio/x-mpegurl");
        addFileType("PLS", FILE_TYPE_PLS, "audio/x-scpls");
        addFileType("WPL", FILE_TYPE_WPL, "application/vnd.ms-wpl");

        // compute file extensions list for native Media Scanner
        StringBuilder builder = new StringBuilder();
        Iterator<String> iterator = sFileTypeMap.keySet().iterator();
        while (iterator.hasNext()) {
            if (builder.length() > 0) {
                builder.append(',');
            }
            builder.append(iterator.next());
        }
        sFileExtensions = builder.toString();
    }

    public static boolean isAudioFileType(int fileType) {
        return ((fileType >= FIRST_AUDIO_FILE_TYPE &&
                fileType <= LAST_AUDIO_FILE_TYPE) ||
                (fileType >= FIRST_MIDI_FILE_TYPE &&
                        fileType <= LAST_MIDI_FILE_TYPE));
    }

    public static boolean isVideoFileType(int fileType) {
        return (fileType >= FIRST_VIDEO_FILE_TYPE &&
                fileType <= LAST_VIDEO_FILE_TYPE);
    }

    public static boolean isImageFileType(int fileType) {
        return (fileType >= FIRST_IMAGE_FILE_TYPE &&
                fileType <= LAST_IMAGE_FILE_TYPE);

    }

    public static boolean isTextFileType(int fileType) {
        return (fileType >= FIRST_TEXT_FILE_TYPE &&
                fileType <= LAST_TEXT_FILE_TYPE);
    }

    public static boolean isPlayListFileType(int fileType) {
        return (fileType >= FIRST_PLAYLIST_FILE_TYPE &&
                fileType <= LAST_PLAYLIST_FILE_TYPE);
    }

    public static boolean isOfficeFileType(int fileType) {
        return (fileType >= FILE_TYPE_XLS &&
                fileType <= FILE_TYPE_PPTX);
    }

    public static boolean isExcelFileType(int fileType) {
        return (fileType >= FILE_TYPE_XLS &&
                fileType <= FILE_TYPE_XLSX);
    }

    public static boolean isWordFileType(int fileType) {
        return (fileType >= FILE_TYPE_DOC &&
                fileType <= FILE_TYPE_DOCX);
    }

    public static boolean isPPTFileType(int fileType) {
        return (fileType >= FILE_TYPE_PPS &&
                fileType <= FILE_TYPE_PPS);
    }

    public static MediaFileType getFileType(String path) {
        int lastDot = path.lastIndexOf(".");
        if (lastDot < 0)
            return null;
        return sFileTypeMap.get(path.substring(lastDot + 1).toUpperCase());
    }

    public static boolean isVideoFileType(String path) {
        MediaFileType type = getFileType(path);
        if (null != type) {
            return isVideoFileType(type.fileType);
        }
        return false;
    }

    public static boolean isAudioFileType(String path) {
        MediaFileType type = getFileType(path);
        if (null != type) {
            return isAudioFileType(type.fileType);
        }
        return false;
    }

    public static boolean isPlayListFileType(String path) {
        MediaFileType type = getFileType(path);
        if (null != type) {
            return isPlayListFileType(type.fileType);
        }
        return false;
    }

    public static int getFileTypeForMimeType(String mimeType) {
        Integer value = sMimeTypeMap.get(mimeType);
        return (value == null ? 0 : value.intValue());
    }


    public static boolean isImageFileType(String path) {
        MediaFileType type = getFileType(path);
        if (null != type) {
            return isImageFileType(type.fileType);
        }
        return false;
    }

    public static boolean isTextFileType(String path) {
        MediaFileType type = getFileType(path);
        if (null != type) {
            return isTextFileType(type.fileType);
        }
        return false;
    }

    public static boolean isOfficeFileType(String path) {
        MediaFileType type = getFileType(path);
        if (null != type) {
            return isOfficeFileType(type.fileType);
        }
        return false;
    }

    /**
     * Get the suffix of the file format  like .jpg
     *
     * @param path
     * @return
     */
    public static String getSuffix(String path) {
        if (path == null) {
            return null;
        }

        int dot = path.lastIndexOf(".");
        if (dot >= 0) {
            return path.substring(dot);
        } else {
            // No extension.
            return "";
        }
    }
}