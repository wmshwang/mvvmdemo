package com.desaysv.mvvmdemo.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.desaysv.mvvmdemo.db.bean.Image;
import com.desaysv.mvvmdemo.db.bean.News;
import com.desaysv.mvvmdemo.db.bean.User;
import com.desaysv.mvvmdemo.db.bean.Video;
import com.desaysv.mvvmdemo.db.bean.WallPaper;
import com.desaysv.mvvmdemo.db.dao.ImageDao;
import com.desaysv.mvvmdemo.db.dao.NewsDao;
import com.desaysv.mvvmdemo.db.dao.UserDao;
import com.desaysv.mvvmdemo.db.dao.VideoDao;
import com.desaysv.mvvmdemo.db.dao.WallPaperDao;

@Database(entities = {User.class, WallPaper.class, Image.class, News.class, Video.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "mvvm_demo";

    public abstract UserDao userDao();

    public abstract WallPaperDao wallPaperDao();

    public  abstract ImageDao imageDao();

    public abstract NewsDao newsDao();

    public abstract VideoDao videoDao();
}
