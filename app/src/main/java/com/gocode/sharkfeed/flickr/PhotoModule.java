package com.gocode.sharkfeed.flickr;

import dagger.Module;
import dagger.Provides;
import com.gocode.sharkfeed.api.ApiService;
import com.gocode.sharkfeed.dao.DatabaseInteractor;

@Module
public class PhotoModule {

    private final Contracts.View photoView;

    public PhotoModule(Contracts.View photoView) {
        this.photoView = photoView;
    }

    @Provides
    @PhotoScope
    PhotoListPresenter providePhotoListPresenter(DatabaseInteractor databaseInteractor, ApiService apiService) {
        return new PhotoListPresenter(photoView, databaseInteractor, apiService);
    }
}
