package com.gocode.sharkfeed.flickr;

import dagger.Subcomponent;

@PhotoScope
@Subcomponent(modules = {PhotoModule.class})
public interface PhotoComponent {
    void inject(PhotoListFragment photoListFragment);
}
