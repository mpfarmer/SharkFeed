package com.gocode.sharkfeed.flickr.detail;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gocode.sharkfeed.BaseApplication;
import com.gocode.sharkfeed.BaseFragment;
import com.gocode.sharkfeed.Constants;
import com.gocode.sharkfeed.R;
import com.gocode.sharkfeed.ZoomImageView;
import com.gocode.sharkfeed.models.Photo;
import com.gocode.sharkfeed.util.ImageSaver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PhotoDetailFragment extends BaseFragment implements Contracts.View {

    @BindView(R.id.ll_parent)
    LinearLayout llParent;
    Unbinder unbinder;
    @BindView(R.id.iv_photo_original)
    ZoomImageView ivPhotoOriginal;
    @BindView(R.id.rl_parent)
    RelativeLayout rlParent;
    @BindView(R.id.ll_download)
    LinearLayout llDownload;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.ll_open_app)
    LinearLayout llOpenApp;

    @Inject
    PhotoDetailPresenter presenter;

    Boolean per = false;
    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private Photo photo;

    @OnClick(R.id.ll_download)
    void OnClick(View view) {
        checkPermissions();
    }

    void OnOpenApp(View view) {
        if (getContext() == null) {
            return;
        }
        Uri imageUri = getImageUri(Objects.requireNonNull(getContext()), ((BitmapDrawable) ivPhotoOriginal.getDrawable()).getBitmap());
        if (imageUri == null) {
            return;
        }
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share images..."));
    }

    public Uri getImageUri(@NonNull Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        if (path == null) {
            return null;
        } else {
            return Uri.parse(path);
        }
    }


    private boolean checkPermissions() {
        int result;
        List listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), (String[]) listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), 100);
            return false;
        } else {
            saveImage();
        }
        return true;
    }

    /*
     * save to external first then internal
     */
    private void saveImage() {
        String dirName = "/SharkFeed/";
        String fileName = "shark_" + System.currentTimeMillis() + ".jpg";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String sdCardDir = Environment.getExternalStorageDirectory() + dirName;
            File dirFile = new File(sdCardDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            File file = new File(sdCardDir, fileName);
            BitmapDrawable drawable = (BitmapDrawable) ivPhotoOriginal.getDrawable();
            FileOutputStream out = null;
            Bitmap bitmap = drawable.getBitmap();
            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                new ImageSaver(getContext())
                        .setDirectoryName(dirName)
                        .setFileName(fileName).save(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                new ImageSaver(getContext())
                        .setDirectoryName(dirName)
                        .setFileName(fileName).save(bitmap);

            }
            Toast.makeText(getContext(), "Saved to: " + Environment.getExternalStorageDirectory() + "/SharkFeed/", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
            }
            return;
        }
    }

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
    public void populateData() {
        rlParent.setVisibility(View.VISIBLE);
        if (getActivity().getIntent() != null) {
            photo = getActivity().getIntent().getParcelableExtra(Constants.PHOTO_DETAIL);
            if (photo != null) {
                tvTitle.setText(photo.getTitle());
                tvUser.setText(photo.getId());
                presenter.findPhoto(photo.getId());
                Glide.with(getActivity()).asDrawable()
                        .load(photo.getUrl())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                llOpenApp.setOnClickListener(v -> OnOpenApp(v));
                                return false;
                            }
                        })
                        .into(ivPhotoOriginal);

            }
        }
    }

    @Override
    public void onCompleted(Photo photoData) {
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {

    }
}
