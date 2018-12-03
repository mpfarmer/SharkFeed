package com.gocode.sharkfeed.dao;

import com.gocode.sharkfeed.models.Photo;

public interface DatabaseCallbacks {
    void onDataInserted(Photo data);

    void onFailed(Throwable throwable);
}
