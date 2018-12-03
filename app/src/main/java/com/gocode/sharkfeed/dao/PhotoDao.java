package com.gocode.sharkfeed.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.gocode.sharkfeed.models.Photo;
import io.reactivex.Single;

@Dao
public interface PhotoDao {

    @Query("SELECT * FROM Photo WHERE id = :id")
    Single<Photo> getPhoto(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addPhoto(Photo photo);

    @Update
    void updatePhoto(Photo photo);

}
