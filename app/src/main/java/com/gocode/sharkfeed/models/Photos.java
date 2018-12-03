package com.gocode.sharkfeed.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.gocode.sharkfeed.database.PhotoTypeConverter;

@Entity
public class Photos implements Parcelable {
    @TypeConverters(PhotoTypeConverter.class)
    @SerializedName("photo")
    @Expose
    List<Photo> photo = null;
    @PrimaryKey
    @SerializedName("page")
    @Expose
    @ColumnInfo
    private int page;
    @SerializedName("pages")
    @Expose
    @ColumnInfo
    private String pages;
    @SerializedName("perpage")
    @Expose
    private int perpage;
    @ColumnInfo
    @SerializedName("total")
    @Expose
    private String total;

    public Photos() {
    }

    protected Photos(Parcel in) {
        this.page = in.readInt();
        this.pages = in.readString();
        this.perpage = in.readInt();
        this.total = in.readString();
        this.photo = in.createTypedArrayList(Photo.CREATOR);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Photo> getPhoto() {
        return photo;
    }

    public void setPhoto(List<Photo> photo) {
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.page);
        dest.writeString(this.pages);
        dest.writeInt(this.perpage);
        dest.writeString(this.total);
        dest.writeTypedList(this.photo);
    }

    public static final Creator<Photos> CREATOR = new Creator<Photos>() {
        @Override
        public Photos createFromParcel(Parcel source) {
            return new Photos(source);
        }

        @Override
        public Photos[] newArray(int size) {
            return new Photos[size];
        }
    };
}
