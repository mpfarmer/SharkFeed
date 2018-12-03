package com.gocode.sharkfeed.api;

import com.gocode.sharkfeed.models.response.PhotoResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ApiService {

    @GET("services/rest/?method=flickr.photos.search&tags=shark&extras=url_t,url_c,url_l,url_o")
    Observable<PhotoResponse> listPhotos(@QueryMap Map<String, Object> params);
}
