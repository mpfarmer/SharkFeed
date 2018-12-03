package com.gocode.sharkfeed.flickr.detail;

import com.gocode.sharkfeed.models.response.Photo;

public interface Contracts {

    interface View {
        void initView();

        void PopulateData();

        void onCompleted(Photo photo);

        void showLoading();

        void hideLoading();
    }

    interface Presenter {
        void init();

        void updatePhoto(Photo photo);

        void findPhoto(String id);
    }
}
