package com.gocode.sharkfeed;

import javax.inject.Singleton;

import dagger.Component;
import com.gocode.sharkfeed.api.ApiModule;
import com.gocode.sharkfeed.flickr.PhotoComponent;
import com.gocode.sharkfeed.flickr.PhotoModule;
import com.gocode.sharkfeed.flickr.detail.PhotoDetailComponent;
import com.gocode.sharkfeed.flickr.detail.PhotoDetailModule;

@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {
    void inject(BaseApplication baseApplication);
    PhotoComponent newPhotoComponent(PhotoModule photoModule);

    PhotoDetailComponent newPhotoDetailComponent(PhotoDetailModule photoDetailModule);
}