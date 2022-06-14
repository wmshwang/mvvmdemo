package com.desaysv.mvvmdemo.ui.picture;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;

import com.desaysv.mvvmdemo.db.bean.WallPaper;
import com.desaysv.mvvmdemo.repository.PictureRepository;
import com.desaysv.mvvmdemo.ui.base.BaseViewModel;

import java.util.List;

public class PictureViewViewModel extends BaseViewModel {
    private final PictureRepository pictureRepository;
    public LiveData<List<WallPaper>> wallPaper;

    @ViewModelInject
    public PictureViewViewModel(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    public void getWallPaper() {
        failed = pictureRepository.failed;
        wallPaper = pictureRepository.getWallPaper();
    }
}