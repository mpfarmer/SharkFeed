package com.gocode.sharkfeed.flickr.detail;

import dagger.Module;
import dagger.Provides;
import com.gocode.sharkfeed.dao.DatabaseInteractor;
import com.gocode.sharkfeed.flickr.PhotoScope;
import timber.log.Timber;

@Module
public class PhotoDetailModule {

    private final Contracts.View photoDetailView;

    public PhotoDetailModule(Contracts.View photoDetailView) {
        this.photoDetailView = photoDetailView;
    }

    @Provides
    @PhotoScope
    PhotoDetailPresenter providePhotoDetailPresenter(DatabaseInteractor databaseInteractor) {
        Timber.d("Creating new presenter for movie detail");
        return new PhotoDetailPresenter(photoDetailView, databaseInteractor);
    }
}
