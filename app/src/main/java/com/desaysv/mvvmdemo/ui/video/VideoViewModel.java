package com.desaysv.mvvmdemo.ui.video;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;

import com.desaysv.mvvmdemo.model.VideoResponse;
import com.desaysv.mvvmdemo.repository.VideoRepository;
import com.desaysv.mvvmdemo.ui.base.BaseViewModel;

/**
 * VideoFragment数据提供
 */
public class VideoViewModel extends BaseViewModel {
    public LiveData<VideoResponse> video;

    private final VideoRepository videoRepository;

    @ViewModelInject
    public VideoViewModel(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public void getVideo() {
        failed = videoRepository.failed;
        video = videoRepository.getVideo();
    }
}