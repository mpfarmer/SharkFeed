package com.gocode.sharkfeed.flickr;

import java.util.List;

import com.gocode.sharkfeed.models.Photo;

public interface Contracts {

    interface View {
        void initView();

        void populateData(List<Photo> resultList);

        void onPhotoItemSelected(Photo result);

        void onError(Throwable throwable);

        void showLoading();

        void hideLoading();

        void sortingList();

        void populateBackData(List<Photo> photos);
    }

    interface Presenter {
        void init();

        void fetchPhotos(int pageIndex);

        boolean shouldUpdate();

        void sortByDateDesc(int pageIndex);

        void sortByDateAcs(int pageIndex);

        void showLoading();

        void hideLoading();

        void fetchBackPhotos(int pageIndex);
    }
}