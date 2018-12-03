package com.gocode.sharkfeed.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.gocode.sharkfeed.dao.PhotoDao;
import com.gocode.sharkfeed.dao.PhotosDao;
import com.gocode.sharkfeed.models.response.Photo;
import com.gocode.sharkfeed.models.response.Photos;

/**
 * @author Go Code
 */
@TypeConverters({PhotoTypeConverter.class})
@Database(entities = {Photo.class, Photos.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PhotoDao photoDao();


    public abstract PhotosDao photosDao();
}
