package com.gocode.sharkfeed;

import android.app.Application;

import lombok.Getter;
import timber.log.Timber;

public class BaseApplication extends Application {
    @Getter
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        appComponent.inject(this);
    }
}
