package com.gocode.sharkfeed.flickr;

import android.os.Bundle;
import android.view.Menu;

import com.gocode.sharkfeed.BaseActivity;
import com.gocode.sharkfeed.R;

public class PhotoListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.gocode.sharkfeed.R.layout.activity_photo_list);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

    }
}
