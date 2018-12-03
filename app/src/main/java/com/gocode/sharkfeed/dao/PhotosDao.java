package com.gocode.sharkfeed.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.gocode.sharkfeed.models.response.Photos;
import io.reactivex.Single;

@Dao
public interface PhotosDao {

    @Query("SELECT * FROM Photos WHERE page = :page")
    Single<Photos> getPhoto(int page);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addPhotos(Photos photo);

    @Update
    void updatePhotos(Photos photo);

}