package com.gocode.sharkfeed.flickr.detail;

import com.gocode.sharkfeed.dao.DatabaseCallbacks2;
import com.gocode.sharkfeed.dao.DatabaseInteractor;
import com.gocode.sharkfeed.models.response.Photo;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class PhotoDetailPresenter implements Contracts.Presenter, DatabaseCallbacks2 {

    private Contracts.View photoDetailView;
    private final DatabaseInteractor databaseInteractor;

    public PhotoDetailPresenter(Contracts.View photoDetailView,
                                DatabaseInteractor databaseInteractor) {
        this.photoDetailView = photoDetailView;
        this.databaseInteractor = databaseInteractor;
        databaseInteractor.setCallbacks2(this);
    }

    @Override
    public void init() {
        photoDetailView.PopulateData();
    }

    @Override
    public void updatePhoto(Photo photo) {
        photoDetailView.showLoading();
        databaseInteractor.setPhotoData(photo);
    }

    @Override
    public void findPhoto(String id) {
        databaseInteractor.getPhotoData(id).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(photoDetailView::onCompleted, this::onFailed);
    }

    @Override
    public void onDataInserted(Photo data) {
        findPhoto(data.getId());
    }


    @Override
    public void onFailed(Throwable throwable) {
        Timber.d("data inserted"+throwable.getCause());
    }
}

