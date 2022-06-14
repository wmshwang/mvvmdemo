package com.desaysv.mvvmdemo.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.SPUtils;
import com.desaysv.mvvmdemo.api.ApiService;
import com.desaysv.mvvmdemo.db.bean.Image;
import com.desaysv.mvvmdemo.db.bean.WallPaper;
import com.desaysv.mvvmdemo.db.dao.ImageDao;
import com.desaysv.mvvmdemo.db.dao.WallPaperDao;
import com.desaysv.mvvmdemo.model.BiYingResponse;
import com.desaysv.mvvmdemo.model.WallPaperResponse;
import com.desaysv.mvvmdemo.network.BaseObserver;
import com.desaysv.mvvmdemo.network.utils.DateUtil;
import com.desaysv.mvvmdemo.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * Main存储库 用于对数据进行处理
 */
public class MainRepository {
    private static final String TAG = MainRepository.class.getSimpleName();
    private NetworkRepository networkRepository;
    private WallPaperDao wallPaperDao;
    private ImageDao imageDao;

    /**
     * 必应数据
     */
    final MutableLiveData<BiYingResponse> biyingImage = new MutableLiveData<>();
    /**
     * 热门壁纸数据
     */
    final MutableLiveData<WallPaperResponse> wallPaper = new MutableLiveData<>();

    public final MutableLiveData<String> failed = new MutableLiveData<>();

    @Inject
    public MainRepository(NetworkRepository networkRepository, WallPaperDao wallPaperDao, ImageDao imageDao) {
        this.networkRepository = networkRepository;
        this.wallPaperDao = wallPaperDao;
        this.imageDao = imageDao;
    }

    public MutableLiveData<BiYingResponse> getBiYing() {
        //今日此接口是否已请求
        if (SPUtils.getInstance().getBoolean(Constant.IS_TODAY_REQUEST)) {
            if (System.currentTimeMillis() <= SPUtils.getInstance().getLong(Constant.REQUEST_TIMESTAMP)) {
                //当前时间未超过次日0点，从本地获取
                getLocalDB();
            } else {
                //大于则数据需要更新，从网络获取
                requestNetworkApi();
            }
        } else {
            //没有请求过接口 或 当前时间，从网络获取
            requestNetworkApi();
        }
        return biyingImage;
    }

    /**
     * 获取壁纸数据
     * @return wallPaper
     */
    public MutableLiveData<WallPaperResponse> getWallPaper() {
        //今日此接口是否已经请求
        if (SPUtils.getInstance().getBoolean(Constant.IS_TODAY_REQUEST_WALLPAPER)) {
            if (System.currentTimeMillis() <= SPUtils.getInstance().getLong(Constant.REQUEST_TIMESTAMP_WALLPAPER)) {
                getLocalDBForWallPaper();
            } else {
                requestNetworkApiForWallPaper();
            }
        } else {
            requestNetworkApiForWallPaper();
        }
        return wallPaper;
    }

    /**
     * 从本地数据库获取热门壁纸
     */
    private void getLocalDBForWallPaper() {
        Log.d(TAG, "getLocalDBForWallPaper: 从本地数据库获取 热门壁纸");
        WallPaperResponse wallPaperResponse = new WallPaperResponse();
        WallPaperResponse.ResBean resBean = new WallPaperResponse.ResBean();
        List<WallPaperResponse.ResBean.VerticalBean> verticalBeanList = new ArrayList<>();
        Flowable<List<WallPaper>> listFlowable = wallPaperDao.getAll();
        CustomDisposable.addDisposable(listFlowable, wallPapers -> {
            for (WallPaper paper : wallPapers) {
                WallPaperResponse.ResBean.VerticalBean verticalBean = new WallPaperResponse.ResBean.VerticalBean();
                verticalBean.setImg(paper.getImg());
                verticalBeanList.add(verticalBean);
            }
            resBean.setVertical(verticalBeanList);
            wallPaperResponse.setRes(resBean);
            wallPaper.postValue(wallPaperResponse);
        });
    }

    /**
     * 从本地数据库获取
     */
    private void getLocalDB() {
        Log.d(TAG, "getLocalDB: 从本地数据库获取 必应壁纸");
        BiYingResponse biYingImgResponse = new BiYingResponse();
        //从数据库获取
        Flowable<Image> imageFlowable = imageDao.queryById(1);
        //RxJava处理Room数据获取
        CustomDisposable.addDisposable(imageFlowable, image -> {
            BiYingResponse.ImagesBean imagesBean = new BiYingResponse.ImagesBean();
            imagesBean.setUrl(image.getUrl());
            imagesBean.setUrlbase(image.getUrlbase());
            imagesBean.setCopyright(image.getCopyright());
            imagesBean.setCopyrightlink(image.getCopyrightlink());
            imagesBean.setTitle(image.getTitle());
            List<BiYingResponse.ImagesBean> imagesBeanList = new ArrayList<>();
            imagesBeanList.add(imagesBean);
            biYingImgResponse.setImages(imagesBeanList);
            biyingImage.postValue(biYingImgResponse);
        });
    }

    /**
     * 从网络上请求数据
     */
    @SuppressLint("CheckResult")
    private void requestNetworkApi() {
        Log.d(TAG, "requestNetworkApi: 从网络获取 必应壁纸");
        ApiService apiService = networkRepository.createService(ApiService.class, 0);
        apiService.biying().compose(networkRepository.applySchedulers(new BaseObserver<BiYingResponse>() {
            @Override
            public void onSuccess(BiYingResponse biYingImgResponse) {
                if (biYingImgResponse.getImages() != null) {
                    //存储到本地数据库中，并记录今日已请求了数据
                    saveImageData(biYingImgResponse);
                    biyingImage.setValue(biYingImgResponse);
                } else {
                    failed.postValue("必应数据为空");
                }
            }

            @Override
            public void onFailure(Throwable e) {
                failed.postValue("BiYing Error: " + e.toString());
            }
        }));
    }

    /**
     * 保存必应壁纸数据
     */
    private void saveImageData(BiYingResponse biYingImgResponse) {
        //记录今日已请求
        SPUtils.getInstance().put(Constant.IS_TODAY_REQUEST, true);
        //记录此次请求的时最晚有效时间戳
        SPUtils.getInstance().put(Constant.REQUEST_TIMESTAMP, DateUtil.getMillisNextEarlyMorning());
        BiYingResponse.ImagesBean bean = biYingImgResponse.getImages().get(0);
        //保存到数据库
        Completable insert = imageDao.insertAll(
                new Image(1, bean.getUrl(), bean.getUrlbase(), bean.getCopyright(),
                        bean.getCopyrightlink(), bean.getTitle()));
        //RxJava处理Room数据存储
        CustomDisposable.addDisposable(insert, () -> Log.d(TAG, "saveImageData: 必应数据保存成功"));
    }

    /**
     * 从网络获取壁纸数据
     */
    @SuppressLint("CheckResult")
    private void requestNetworkApiForWallPaper() {
        Log.d(TAG, "requestNetworkApiForWallPaper: 从网络获取 热门壁纸");
        networkRepository.createService(ApiService.class, 1).
                wallPaper().compose(networkRepository.applySchedulers(new BaseObserver<WallPaperResponse>() {
            @Override
            public void onSuccess(WallPaperResponse wallPaperResponse) {
                if (wallPaperResponse.getCode() == 0) {
                    //保存本地数据
                    saveWallPaper(wallPaperResponse);
                    wallPaper.setValue(wallPaperResponse);
                } else {
                    failed.postValue(wallPaperResponse.getMsg());
                }
            }

            @Override
            public void onFailure(Throwable e) {
                failed.postValue("WallPaper Error: " + e.toString());
            }
        }));
    }

    /**
     * 保存热门壁纸数据
     */
    private void saveWallPaper(WallPaperResponse wallPaperResponse) {
        SPUtils.getInstance().put(Constant.IS_TODAY_REQUEST_WALLPAPER, true);
        SPUtils.getInstance().put(Constant.REQUEST_TIMESTAMP_WALLPAPER, DateUtil.getMillisNextEarlyMorning());

        Completable deleteAll = wallPaperDao.deleteAll();
        CustomDisposable.addDisposable(deleteAll, () -> {
            Log.d(TAG, "saveWallPaper: 删除数据成功");
            List<WallPaper> wallPaperList = new ArrayList<>();
            for (WallPaperResponse.ResBean.VerticalBean verticalBean : wallPaperResponse.getRes().getVertical()) {
                wallPaperList.add(new WallPaper(verticalBean.getImg()));
            }
            //保存到数据库
            Completable insertAll = wallPaperDao.insertAll(wallPaperList);
            Log.d(TAG, "saveWallPaper: 插入数据：" + wallPaperList.size() + "条");
            //RxJava处理Room数据存储
            CustomDisposable.addDisposable(insertAll, () -> Log.d(TAG, "saveWallPaper: 热门天气数据保存成功"));
        });
    }
}
