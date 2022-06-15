package com.desaysv.mvvmdemo.di;

import android.app.Application;

import androidx.room.Room;

import com.desaysv.mvvmdemo.db.AppDatabase;
import com.desaysv.mvvmdemo.db.dao.ImageDao;
import com.desaysv.mvvmdemo.db.dao.NewsDao;
import com.desaysv.mvvmdemo.db.dao.UserDao;
import com.desaysv.mvvmdemo.db.dao.VideoDao;
import com.desaysv.mvvmdemo.db.dao.WallPaperDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@Module
@InstallIn(ApplicationComponent.class)
public class DataBaseModule {
    @Provides
    @Singleton
    public static AppDatabase provideAppDatabase(Application application){
        return Room.databaseBuilder(application, AppDatabase.class,AppDatabase.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    public static UserDao provideUserDao(AppDatabase database){
        return database.userDao();
    }

    @Provides
    @Singleton
    public static WallPaperDao provideWallPaperDao(AppDatabase database){
        return database.wallPaperDao();
    }

    @Provides
    @Singleton
    public static ImageDao provideImageDao(AppDatabase database){
        return database.imageDao();
    }

    @Provides
    @Singleton
    public static NewsDao provideNewsDao(AppDatabase database){
        return database.newsDao();
    }

    @Provides
    @Singleton
    public static VideoDao provideVideoDao(AppDatabase database){
        return database.videoDao();
    }
}
