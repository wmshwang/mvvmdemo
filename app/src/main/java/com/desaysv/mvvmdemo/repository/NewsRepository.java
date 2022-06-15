package com.desaysv.mvvmdemo.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.SPUtils;
import com.desaysv.mvvmdemo.api.ApiService;
import com.desaysv.mvvmdemo.db.bean.News;
import com.desaysv.mvvmdemo.db.dao.NewsDao;
import com.desaysv.mvvmdemo.model.NewsResponse;
import com.desaysv.mvvmdemo.network.BaseObserver;
import com.desaysv.mvvmdemo.network.utils.DateUtil;
import com.desaysv.mvvmdemo.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * 对新闻数据进行处理
 *
 * @author llw
 */
@SuppressLint("CheckResult")
public class NewsRepository {

    private static final String TAG = NewsRepository.class.getSimpleName();
    final MutableLiveData<NewsResponse> news = new MutableLiveData<>();

    public final MutableLiveData<String> failed = new MutableLiveData<>();

    private NewsDao newsDao;
    private NetworkRepository networkRepository;

    @Inject
    NewsRepository(NewsDao newsDao, NetworkRepository networkRepository) {
        this.newsDao = newsDao;
        this.networkRepository = networkRepository;
    }

    /**
     * 获取新闻数据
     * @return news
     */
    public MutableLiveData<NewsResponse> getNews() {
        //今日此接口是否已经请求
        if (SPUtils.getInstance().getBoolean(Constant.IS_TODAY_REQUEST_NEWS)) {
            if (System.currentTimeMillis() <= SPUtils.getInstance().getLong(Constant.REQUEST_TIMESTAMP_NEWS)) {
                getNewsForLocalDB();
            } else {
                getNewsForNetwork();
            }
        } else {
            getNewsForNetwork();
        }
        return news;
    }

    /**
     * 从本地数据库获取新闻
     */
    private void getNewsForLocalDB() {
        Log.d(TAG, "getNewsForLocalDB: 从本地数据库获取 新闻数据");
        NewsResponse newsResponse = new NewsResponse();
        NewsResponse.ResultBean resultBean = new NewsResponse.ResultBean();

        List<NewsResponse.ResultBean.DataBean> dataBeanList = new ArrayList<>();
        Flowable<List<News>> listFlowable = newsDao.getAll();
        CustomDisposable.addDisposable(listFlowable, newss -> {
            for (News news1 : newss) {
                NewsResponse.ResultBean.DataBean dataBean = new NewsResponse.ResultBean.DataBean();
                dataBean.setUniquekey(news1.getUniquekey());
                dataBean.setTitle(news1.getTitle());
                dataBean.setDate(news1.getDate());
                dataBean.setAuthor_name(news1.getAuthor_name());
                dataBean.setCategory(news1.getCategory());
                dataBean.setThumbnail_pic_s(news1.getThumbnail_pic_s());
                dataBean.setIs_content(news1.getIs_content());
                dataBeanList.add(dataBean);
            }
            resultBean.setData(dataBeanList);
            newsResponse.setResult(resultBean);
            news.postValue(newsResponse);
        });
    }

    /**
     * 从网络获取壁纸数据
     */
    private void getNewsForNetwork() {
        Log.d(TAG, "getNewsForNetwork: 从网络获取 热门壁纸");
        networkRepository.createService(ApiService.class, 2).
                news().compose(networkRepository.applySchedulers(new BaseObserver<NewsResponse>() {
            @Override
            public void onSuccess(NewsResponse newsResponse) {
                if (newsResponse.getError_code() == 0) {
                    //保存本地数据
                    saveNews(newsResponse);
                    news.setValue(newsResponse);
                } else {
                    failed.postValue(newsResponse.getReason());
                }
            }

            @Override
            public void onFailure(Throwable e) {
                failed.postValue("News Error: " + e.toString());
            }
        }));
    }

    /**
     * 保存热门壁纸数据
     */
    private void saveNews(NewsResponse newsResponse) {
        SPUtils.getInstance().put(Constant.IS_TODAY_REQUEST_NEWS, true);
        SPUtils.getInstance().put(Constant.REQUEST_TIMESTAMP_NEWS, DateUtil.getMillisNextEarlyMorning());

        Completable deleteAll = newsDao.deleteAll();
        CustomDisposable.addDisposable(deleteAll, () -> {
            Log.d(TAG, "saveNews: 删除数据成功");
            List<News> newsList = new ArrayList<>();
            for (NewsResponse.ResultBean.DataBean dataBean : newsResponse.getResult().getData()) {
                newsList.add(new News(dataBean.getUniquekey(),dataBean.getTitle(),dataBean.getDate(),dataBean.getCategory(),
                        dataBean.getAuthor_name(),dataBean.getUrl(),dataBean.getThumbnail_pic_s(),dataBean.getIs_content()));
            }
            //保存到数据库
            Completable insertAll = newsDao.insertAll(newsList);
            Log.d(TAG, "saveNews: 插入数据：" + newsList.size() + "条");
            //RxJava处理Room数据存储
            CustomDisposable.addDisposable(insertAll, () -> Log.d(TAG, "saveNews: 新闻数据保存成功"));
        });
    }
}
