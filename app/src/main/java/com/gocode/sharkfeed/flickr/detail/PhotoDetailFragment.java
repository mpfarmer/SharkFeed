package com.gocode.sharkfeed.flickr.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.gocode.sharkfeed.BaseApplication;
import com.gocode.sharkfeed.BaseFragment;
import com.gocode.sharkfeed.Constants;
import com.gocode.sharkfeed.R;
import com.gocode.sharkfeed.ZoomImageView;
import com.gocode.sharkfeed.models.response.Photo;

public class PhotoDetailFragment extends BaseFragment implements Contracts.View {

    @BindView(R.id.ll_parent)
    LinearLayout llParent;
    Unbinder unbinder;
    @BindView(R.id.iv_photo_original)
    ZoomImageView ivPhotoOriginal;
    @BindView(R.id.rl_parent)
    RelativeLayout rlParent;
    @Inject
    PhotoDetailPresenter presenter;

    private Photo photo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication) getActivity().getApplication())
                .getAppComponent()
                .newPhotoDetailComponent(new PhotoDetailModule(this))
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //View Methods
    @Override
    public void initView() {
    }

    @Override
    public void PopulateData() {
        rlParent.setVisibility(View.VISIBLE);
        if (getActivity().getIntent() != null) {
            photo = getActivity().getIntent().getParcelableExtra(Constants.PHOTO_DETAIL);
            if (photo != null) {
                presenter.findPhoto(photo.getId());
                Glide.with(getActivity()).asDrawable()
                        .load(photo.getUrl_o() == null ? photo.getUrl_l() : photo.getUrl_o())
                        .into(ivPhotoOriginal);

            }
        }
    }

    @Override
    public void onCompleted(Photo photoData) {
        cancelLoadingDialog();
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {

    }
}
