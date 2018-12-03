package com.gocode.sharkfeed.flickr;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.gocode.sharkfeed.R;
import com.gocode.sharkfeed.models.response.Photo;
import lombok.Getter;

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder> {
    private Context context;

    @Getter
    private List<Photo> photoList;
    private LayoutInflater layoutInflater;
    private Contracts.View photoView;

    public PhotoListAdapter(Context context, Contracts.View photoView) {
        this.context = context;
        photoList = new ArrayList<>();
        layoutInflater = LayoutInflater.from(context);
        this.photoView = photoView;
    }

    public void addAll(List<Photo> photos) {
        photoList.addAll(photos);
        notifyDataSetChanged();
    }

    public List<Photo> getList() {
        return photoList;
    }

    public void clear() {
        if (photoList != null) {
            photoList.clear();
        }
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(layoutInflater.inflate(R.layout.view_photo_item, parent,
                false));
    }


    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        holder.bindViews(photoList.get(position));
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_photo_thumbnail)
        ImageView ivPhotoThumbnail;

        PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("CheckResult")
        void bindViews(Photo photo) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.ic_placeholder);
            requestOptions.error(R.drawable.ic_placeholder);
            requestOptions.fitCenter();
            Glide.with(context)
                    .asDrawable()
                    .apply(requestOptions)
                    .load(photo.getUrl_t())
                    .into(ivPhotoThumbnail);
            itemView.setOnClickListener(v -> photoView.onPhotoItemSelected(photo));
        }
    }
}