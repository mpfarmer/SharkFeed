package com.gocode.sharkfeed.dao;

import com.gocode.sharkfeed.models.response.Photo;

public interface DatabaseCallbacks2 {
    void onDataInserted(Photo data);

    void onFailed(Throwable throwable);
}
