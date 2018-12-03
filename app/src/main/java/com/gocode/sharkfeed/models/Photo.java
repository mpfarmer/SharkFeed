package com.gocode.sharkfeed.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Photo implements Parcelable {
    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NonNull
    private String id;
    @SerializedName("title")
    @ColumnInfo
    private String title;
    @ColumnInfo
    @SerializedName("url_o")
    private String url_o;
    @ColumnInfo
    @SerializedName("url_t")
    private String url_t;
    @ColumnInfo
    @SerializedName("url_c")
    private String url_c;
    @ColumnInfo
    @SerializedName("url_l")
    private String url_l;


    public Photo() {
    }

    protected Photo(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.url_o = in.readString();
        this.url_t = in.readString();
        this.url_c = in.readString();
        this.url_l = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl_t() {
        return url_t;
    }

    public void setUrl_t(String url_t) {
        this.url_t = url_t;
    }

    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getUrl_o() {
        return url_o;
    }

    public void setUrl_o(String url_o) {
        this.url_o = url_o;
    }


    public String getUrl_c() {
        return url_c;
    }

    public void setUrl_c(String url_c) {
        this.url_c = url_c;
    }

    public String getUrl_l() {
        return url_l;
    }

    public void setUrl_l(String url_l) {
        this.url_l = url_l;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title == null ? "": this.title);
        dest.writeString(this.url_o == null ? "": this.url_o);
        dest.writeString(this.url_t == null ? "": this.url_t);
        dest.writeString(this.url_c == null ? "": this.url_c);
        dest.writeString(this.url_l == null ? "": this.url_l);
    }

    public String getUrl() {
        return !TextUtils.isEmpty(url_o) ?
                url_o : (!TextUtils.isEmpty(url_l) ?
                url_l : (!TextUtils.isEmpty(url_c) ?
                url_c : (!TextUtils.isEmpty(url_t) ?
                url_t: "blank:about")));
    }
}
