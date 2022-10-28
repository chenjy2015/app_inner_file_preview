package com.filepreview.application.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;


/**
 * file info bean
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/21
 */
public class FileVO implements Parcelable {
    File file;
    int duration;
    int drawableId;
    boolean selected;

    public FileVO() {
    }

    public FileVO(File file, int drawableId) {
        this.file = file;
        this.drawableId = drawableId;
    }

    protected FileVO(Parcel in) {
        duration = in.readInt();
        drawableId = in.readInt();
        selected = in.readByte() != 0;
    }

    public static final Creator<FileVO> CREATOR = new Creator<FileVO>() {
        @Override
        public FileVO createFromParcel(Parcel in) {
            return new FileVO(in);
        }

        @Override
        public FileVO[] newArray(int size) {
            return new FileVO[size];
        }
    };

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(duration);
        dest.writeInt(drawableId);
        dest.writeByte((byte) (selected ? 1 : 0));
    }
}
