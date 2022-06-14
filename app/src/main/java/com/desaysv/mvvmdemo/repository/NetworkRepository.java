package com.desaysv.mvvmdemo.repository;

import com.desaysv.mvvmdemo.network.BaseResponse;
import com.desaysv.mvvmdemo.network.errorhandler.ExceptionHandle;
import com.desaysv.mvvmdemo.network.errorhandler.HttpErrorHandler;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * 网络请求Repository
 */
public class NetworkRepository {
    private Retrofit.Builder retrofitBuilder;

    /**
     * 设置访问Url类型
     * @param type 0 必应 1 壁纸列表
     */
    private String getBaseUrl(int type) {
        switch (type) {
            case 0:
                //必应
                return "https://cn.bing.com";
            case 1:
                //热门壁纸
                return "http://service.picasso.adesk.com";
            case 2:
                //聚合API 1
                return "http://v.juhe.cn";
            case 3:
            default:
                //聚合API 2
                return "http://apis.juhe.cn";
        }
    }

    @Inject
    public NetworkRepository(Retrofit.Builder retrofitBuilder) {
        this.retrofitBuilder = retrofitBuilder;
    }

    /**
     * 创建serviceClass的实例
     */
    public <T> T createService(Class<T> serviceClass, int type) {
        //设置Url类型
        retrofitBuilder.baseUrl(getBaseUrl(type));
        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(serviceClass);
    }

    /**
     * 配置RxJava 完成线程的切换
     *
     * @param observer 这个observer要注意不要使用lifecycle中的Observer
     * @param <T>      泛型
     * @return Observable
     */
    public <T> ObservableTransformer<T, T> applySchedulers(final Observer<T> observer) {
        return upstream -> {
            Observable<T> observable = upstream
                    .subscribeOn(Schedulers.io())//线程订阅
                    .observeOn(AndroidSchedulers.mainThread())//观察Android主线程
                    .map(getAppErrorHandler())//判断有没有500的错误，有则进入getAppErrorHandler
                    .onErrorResumeNext(new HttpErrorHandler<>());//判断有没有400的错误
            //订阅观察者
            observable.subscribe(observer);
            return observable;
        };
    }

    /**
     * 错误码处理
     */
    protected <T> Function<T, T> getAppErrorHandler() {
        return response -> {
            //当response返回出现500之类的错误时
            if (response instanceof BaseResponse && ((BaseResponse) response).responseCode >= 500) {
                //通过这个异常处理，得到用户可以知道的原因
                ExceptionHandle.ServerException exception = new ExceptionHandle.ServerException();
                exception.code = ((BaseResponse) response).responseCode;
                exception.message = ((BaseResponse) response).responseError != null ? ((BaseResponse) response).responseError : "";
                throw exception;
            }
            return response;
        };
    }
}
