package com.gocode.sharkfeed.dao;

import com.gocode.sharkfeed.models.Photo;
import com.gocode.sharkfeed.models.Photos;

import io.reactivex.Single;
import lombok.NonNull;

/**
 * @author Go Code
 */

public interface DatabaseInteractor {


    void setPhotoData(Photo data);

    void setPhotos(Photos data);

    Single<Photo> getPhotoData(@NonNull String id);

    Single<Photos> getPhotos(int page);

    void setCallbacks2(DatabaseCallbacks callbacks);

    void setCallbacks3(DatabaseCallbacks1 callbacks);

}
