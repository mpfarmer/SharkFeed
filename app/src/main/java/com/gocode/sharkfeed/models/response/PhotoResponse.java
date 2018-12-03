package com.gocode.sharkfeed.models.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotoResponse implements Parcelable {
    @SerializedName("photos")
    @Expose
    private Photos photos;
    @SerializedName("stat")
    @Expose
    private String stat;


    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.photos, flags);
        dest.writeString(this.stat);
    }

    public PhotoResponse() {
    }

    protected PhotoResponse(Parcel in) {
        this.photos = in.readParcelable(Photos.class.getClassLoader());
        this.stat = in.readString();
    }

    public static final Creator<PhotoResponse> CREATOR = new Creator<PhotoResponse>() {
        @Override
        public PhotoResponse createFromParcel(Parcel source) {
            return new PhotoResponse(source);
        }

        @Override
        public PhotoResponse[] newArray(int size) {
            return new PhotoResponse[size];
        }
    };
}
