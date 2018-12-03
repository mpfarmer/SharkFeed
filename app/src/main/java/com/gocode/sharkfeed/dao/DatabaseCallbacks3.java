package com.gocode.sharkfeed.dao;

import com.gocode.sharkfeed.models.response.Photos;

public interface DatabaseCallbacks3 {
    void onDataInserted(Photos data);

    void onFailed(Throwable throwable);
}
