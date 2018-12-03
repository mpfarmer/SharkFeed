package com.gocode.sharkfeed.flickr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gocode.sharkfeed.BaseApplication;
import com.gocode.sharkfeed.BaseFragment;
import com.gocode.sharkfeed.Constants;
import com.gocode.sharkfeed.R;
import com.gocode.sharkfeed.api.ApiService;
import com.gocode.sharkfeed.flickr.detail.PhotoDetailActivity;
import com.gocode.sharkfeed.models.Photo;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class PhotoListFragment extends BaseFragment implements Contracts.View {

    private static final String INDEX_PAGE = "INDEX_PAGE";
    @BindView(R.id.sl_photo_list)
    SwipeRefreshLayout slPhotoList;
    @BindView(R.id.rv_photo_list)
    RecyclerView rvPhotoList;
    @BindView(R.id.progress_bar)
    ImageView progressBar;
    @BindView(R.id.tv_info)
    AppCompatTextView tvInfo;
    Unbinder unbinder;
    @Inject
    PhotoListPresenter presenter;
    @Inject
    ApiService apiService;
    private int pageIndex = 1;
    private int historyIndex = 1;
    private GridLayoutManager gridLayoutManager;
    private PhotoListAdapter photoListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication) getActivity().getApplication())
                .getAppComponent()
                .newPhotoComponent(new PhotoModule(this))
                .inject(this);
        if (savedInstanceState != null) {
            pageIndex = savedInstanceState.getInt(INDEX_PAGE);
            historyIndex = savedInstanceState.getInt(INDEX_PAGE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.init();
        RxRecyclerView.scrollEvents(rvPhotoList)
                .filter(event -> !slPhotoList.isRefreshing() && presenter.shouldUpdate())
                .filter(event1 -> hasScrolledToLast())
                .map(event1 -> historyIndex = historyIndex - 1)
                .subscribe(event1 -> presenter.fetchBackPhotos(historyIndex));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private Boolean hasScrolledToLast() {
        int pastVisibleItems, visibleItemCount, totalItemCount;
        visibleItemCount = gridLayoutManager.getChildCount();
        totalItemCount = gridLayoutManager.getItemCount();
        pastVisibleItems = gridLayoutManager.findFirstVisibleItemPosition();
        Timber.d("totalItemCount: " + totalItemCount + " visibleItemCount: " + visibleItemCount + " pastVisibleItems: " + pastVisibleItems);
        return (visibleItemCount + pastVisibleItems) >= totalItemCount;
    }

    private void resetPageIndex() {
        pageIndex = 1;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(INDEX_PAGE, pageIndex);
        super.onSaveInstanceState(outState);
    }

    //View methods
    @Override
    public void initView() {
        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
//        rvPhotoList.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//                int position = parent.getChildAdapterPosition(view); // item position
//                int column = position % 3; // item column
//
//                if (includeEdge) {
//                    outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
//                    outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)
//
//                    if (position < spanCount) { // top edge
//                        outRect.top = spacing;
//                    }
//                    outRect.bottom = spacing; // item bottom
//                } else {
//                    outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
//                    outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
//                    if (position >= spanCount) {
//                        outRect.top = spacing; // item top
//                    }
//                }
//            }
//        });
        rvPhotoList.setLayoutManager(gridLayoutManager);
        photoListAdapter = new PhotoListAdapter(getContext(), this);
        rvPhotoList.setAdapter(photoListAdapter);
        presenter.fetchPhotos(pageIndex++);
        slPhotoList.setOnRefreshListener(() -> {
            if (pageIndex == 1) {
                return;
            }
            pageIndex++;
            presenter.fetchPhotos(pageIndex);
        });
    }

    @Override
    public void populateData(List<Photo> photoList) {
        slPhotoList.setRefreshing(false);
        photoListAdapter.getList().addAll(0, photoList);
        photoListAdapter.notifyDataSetChanged();
        rvPhotoList.smoothScrollToPosition(0);
        hideLoading();
    }

    @Override
    public void onPhotoItemSelected(Photo photo) {
        Intent intent = new Intent(getActivity(), PhotoDetailActivity.class);
        intent.putExtra(Constants.PHOTO_DETAIL, photo);
        startActivity(intent);
    }

    @Override
    public void onError(Throwable throwable) {
        hideLoading();
        Snackbar.make(tvInfo, R.string.something_went_wrong, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
        rvPhotoList.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        tvInfo.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        tvInfo.setVisibility(View.GONE);
        rvPhotoList.setVisibility(View.VISIBLE);
    }

    @Override
    public void sortingList() {
        showLoading();
        photoListAdapter.clear();
    }

    @Override
    public void populateBackData(List<Photo> photos) {
        slPhotoList.setRefreshing(false);
        photoListAdapter.addAll(photos);
        hideLoading();
    }
}

