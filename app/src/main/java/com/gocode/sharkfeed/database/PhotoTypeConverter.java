package com.gocode.sharkfeed.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import com.gocode.sharkfeed.models.Photo;

public class PhotoTypeConverter {

    @TypeConverter
    public static List<Photo> toPhotos(String value) {
        Type listType = new TypeToken<List<Photo>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toString(List<Photo> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
