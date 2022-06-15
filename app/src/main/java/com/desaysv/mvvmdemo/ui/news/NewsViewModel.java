package com.desaysv.mvvmdemo.ui.news;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;

import com.desaysv.mvvmdemo.model.NewsResponse;
import com.desaysv.mvvmdemo.repository.NewsRepository;
import com.desaysv.mvvmdemo.ui.base.BaseViewModel;

/**
 * NewsFragment数据提供
 */
public class NewsViewModel extends BaseViewModel {
    public LiveData<NewsResponse> news;

    private final NewsRepository newsRepository;
    @ViewModelInject
    public NewsViewModel(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public void getNews() {
        failed = newsRepository.failed;
        news = newsRepository.getNews();
    }
}