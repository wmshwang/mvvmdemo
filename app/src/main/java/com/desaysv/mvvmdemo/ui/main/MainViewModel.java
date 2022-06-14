package com.desaysv.mvvmdemo.ui.main;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.SPUtils;
import com.desaysv.mvvmdemo.db.bean.User;
import com.desaysv.mvvmdemo.model.BiYingResponse;
import com.desaysv.mvvmdemo.model.WallPaperResponse;
import com.desaysv.mvvmdemo.repository.MainRepository;
import com.desaysv.mvvmdemo.ui.base.BaseViewModel;
import com.desaysv.mvvmdemo.utils.Constant;

public class MainViewModel extends BaseViewModel {
    public LiveData<BiYingResponse> biying;

    public LiveData<WallPaperResponse> wallPaper;

    private final MainRepository mainRepository;

    @ViewModelInject
    public MainViewModel(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    public void getBiying() {
        failed = mainRepository.failed;
        biying = mainRepository.getBiYing();
    }

    public void getWallPaper() {
        failed = mainRepository.failed;
        wallPaper = mainRepository.getWallPaper();
    }
}
