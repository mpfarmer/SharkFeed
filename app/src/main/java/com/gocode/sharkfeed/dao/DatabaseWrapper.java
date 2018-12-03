package com.gocode.sharkfeed.dao;

import android.os.AsyncTask;

import com.gocode.sharkfeed.database.AppDatabase;
import com.gocode.sharkfeed.models.response.Photo;
import com.gocode.sharkfeed.models.response.Photos;

import io.reactivex.Single;

/**
 * @author Go Code
 */

public class DatabaseWrapper implements DatabaseInteractor {

    private final AppDatabase appDatabase;
    private DatabaseCallbacks2 callbacks2;
    private DatabaseCallbacks3 callbacks3;

    public DatabaseWrapper(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    @Override
    public void setPhotoData(Photo data) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.photoDao().addPhoto(data);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                callbacks2.onDataInserted(data);
            }
        }.execute();
    }

    @Override
    public void setPhotos(Photos data) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.photosDao().addPhotos(data);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                callbacks3.onDataInserted(data);
            }
        }.execute();
    }

    @Override
    public Single<Photo> getPhotoData(String id) {
        return appDatabase.photoDao().getPhoto(id);
    }

    @Override
    public Single<Photos> getPhotos(int page) {
        return appDatabase.photosDao().getPhoto(page);
    }

    @Override
    public void setCallbacks2(DatabaseCallbacks2 callbacks) {
        this.callbacks2 = callbacks;
    }

    @Override
    public void setCallbacks3(DatabaseCallbacks3 callbacks) {
        this.callbacks3 = callbacks;
    }
}
