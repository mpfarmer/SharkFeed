package com.gocode.sharkfeed.dao;

import com.gocode.sharkfeed.models.Photos;

public interface DatabaseCallbacks1 {
    void onDataInserted(Photos data);

    void onFailed(Throwable throwable);
}
