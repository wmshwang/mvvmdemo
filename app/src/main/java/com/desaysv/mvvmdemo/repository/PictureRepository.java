package com.desaysv.mvvmdemo.repository;

import androidx.lifecycle.MutableLiveData;

import com.desaysv.mvvmdemo.db.bean.WallPaper;
import com.desaysv.mvvmdemo.db.dao.WallPaperDao;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * 获取PictureViewFragment所需数据
 */
public class PictureRepository {
    private WallPaperDao wallPaperDao;

    private final MutableLiveData<List<WallPaper>> wallPaper = new MutableLiveData<>();

    public final MutableLiveData<String> failed = new MutableLiveData<>();

    @Inject
    PictureRepository(WallPaperDao wallPaperDao){
        this.wallPaperDao = wallPaperDao;
    }

    public MutableLiveData<List<WallPaper>> getWallPaper() {
        Flowable<List<WallPaper>> listFlowable = wallPaperDao.getAll();
        CustomDisposable.addDisposable(listFlowable, wallPapers -> {
            if (wallPapers.size() > 0) {
                wallPaper.postValue(wallPapers);
            } else {
                failed.postValue("暂无数据");
            }
        });
        return wallPaper;
    }
}
