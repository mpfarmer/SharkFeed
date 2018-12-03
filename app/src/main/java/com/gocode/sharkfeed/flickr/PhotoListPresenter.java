package com.gocode.sharkfeed.flickr;

import java.util.HashMap;
import java.util.Map;

import com.gocode.sharkfeed.Constants;
import com.gocode.sharkfeed.api.ApiService;
import com.gocode.sharkfeed.dao.DatabaseCallbacks1;
import com.gocode.sharkfeed.dao.DatabaseInteractor;
import com.gocode.sharkfeed.models.response.PhotoResponse;
import com.gocode.sharkfeed.models.Photos;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class PhotoListPresenter implements Contracts.Presenter, DatabaseCallbacks1 {

    public static final String QUERY_PARAM_API_KEY = "api_key";
    public static final String QUERY_PARAM_NO_JSON_CALLBACK = "nojsoncallback";
    public static final String QUERY_PARAM_FORMAT = "format";
    public static final String QUERY_PARAM_VALUE_JSON = "json";
    public static final String QUERY_PARAM_VALUE_NO_JSON_CALLBACK = "1";
    public static final String QUERY_PARAM_PER_PAGE = "per_page";
    public static final String QUERY_PARAM_VALUE_PER_PAGE = "12";
    public static final String QUERY_PARAM_PAGE = "page";
    private Contracts.View photoView;
    private boolean isUpdating;
    private Map<String, Object> queryMap;

    private ApiService apiService;
    private DatabaseInteractor databaseInteractor;

    public PhotoListPresenter(Contracts.View photoView, DatabaseInteractor databaseInteractor, ApiService apiService) {
        super();
        this.photoView = photoView;
        this.apiService = apiService;
        this.databaseInteractor = databaseInteractor;
        databaseInteractor.setCallbacks3(this);
        queryMap = new HashMap<>();
        queryMap.put(QUERY_PARAM_API_KEY, Constants.API_KEY);
        queryMap.put(QUERY_PARAM_PER_PAGE, QUERY_PARAM_VALUE_PER_PAGE);
        queryMap.put(QUERY_PARAM_NO_JSON_CALLBACK,
                QUERY_PARAM_VALUE_NO_JSON_CALLBACK);
        queryMap.put(QUERY_PARAM_FORMAT, QUERY_PARAM_VALUE_JSON);
    }

    @Override
    public void init() {
        photoView.initView();
    }

    @Override
    public void fetchBackPhotos(int pageIndex) {
        if (pageIndex <= 0) {
            return;
        }
        queryMap.put(QUERY_PARAM_PAGE, String.valueOf(pageIndex));
        databaseInteractor.getPhotos(pageIndex).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .map(Photos::getPhoto)
                .subscribe(photos -> photoView.populateBackData(photos),
                        throwable -> fetchPhotoList(queryMap, true));
    }

    @Override
    public void fetchPhotos(int pageIndex) {
        queryMap.put(QUERY_PARAM_PAGE, String.valueOf(pageIndex));
//        for (int i = 1; i < pageIndex; i++) {
            databaseInteractor.getPhotos(pageIndex).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.computation())
                    .map(Photos::getPhoto)
                    .subscribe(photos -> photoView.populateData(photos),
                            throwable -> fetchPhotoList(queryMap, false));
//        }
//        ?method=flickr.photos.search&format=json&nojsoncallback=1&extras=url_t,url_o
//        fetchPhotoList(queryMap);
    }


    private void fetchPhotoList(Map<String, Object> map, boolean back) {
        isUpdating = true;
        apiService.listPhotos(map).observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(() -> isUpdating = false)
                .map(PhotoResponse::getPhotos)
                .subscribe(photos -> {
                    databaseInteractor.setPhotos(photos);
                    if (back) {
                        photoView.populateBackData(photos.getPhoto());
                    } else {
                        photoView.populateData(photos.getPhoto());
                    }
                }, this::onFailed);
//                .flatMapIterable(list -> list)
//                .filter(item -> item.getUrl_o() != null)
//                .toList()
//                .subscribe(photoView::populateData, photoView::onError);
    }

    @Override
    public boolean shouldUpdate() {
        return !isUpdating;
    }

    @Override
    public void sortByDateDesc(int pageIndex) {
        photoView.sortingList();
        queryMap.put("sort", "date-posted-desc");
        queryMap.put("page", pageIndex);
        fetchPhotoList(queryMap, true);
    }

    @Override
    public void sortByDateAcs(int pageIndex) {
        photoView.sortingList();
        queryMap.put("sort_by", "date-posted-asc");
        queryMap.put("page", pageIndex);
        fetchPhotoList(queryMap, false);
    }

    @Override
    public void showLoading() {
        photoView.showLoading();
    }

    @Override
    public void hideLoading() {
        photoView.hideLoading();
    }

    @Override
    public void onDataInserted(Photos data) {
//        fetchPhotos(data.getPage());
    }

    @Override
    public void onFailed(Throwable throwable) {
        Timber.d("photos inserted" + throwable.getCause());
    }
}
