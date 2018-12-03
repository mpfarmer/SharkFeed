package com.gocode.sharkfeed.flickr.detail;

import dagger.Subcomponent;
import com.gocode.sharkfeed.flickr.PhotoScope;

@PhotoScope
@Subcomponent(modules = {PhotoDetailModule.class})
public interface PhotoDetailComponent {
    void inject(PhotoDetailFragment photoDetailFragment);
}
