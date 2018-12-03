package com.gocode.sharkfeed;

import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import com.gocode.sharkfeed.dao.DatabaseInteractor;
import com.gocode.sharkfeed.dao.DatabaseWrapper;
import com.gocode.sharkfeed.database.AppDatabase;

/**
 * Provides application class
 */

@Module
public class AppModule {

    private final BaseApplication application;

    public AppModule(BaseApplication application) {
        this.application = application;
    }

    @Provides
    DatabaseInteractor providesDatabaseInteractor(AppDatabase appDatabase) {
        return new DatabaseWrapper(appDatabase);
    }

    @Provides
    @Singleton
    BaseApplication providesApplication () {
        return application;
    }

    @Provides
    @Singleton
    AppDatabase providesAppDatabase() {
        return Room.databaseBuilder(application, AppDatabase.class, "photoData").build();
    }
}
