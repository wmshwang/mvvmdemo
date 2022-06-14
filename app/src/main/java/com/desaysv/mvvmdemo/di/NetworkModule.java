package com.desaysv.mvvmdemo.di;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.desaysv.mvvmdemo.BuildConfig;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(ApplicationComponent.class)
public class NetworkModule {
    private static final String TAG = NetworkModule.class.getSimpleName();

    /**
     * 请求拦截器
     * @return Interceptor
     */
    @Provides
    @Singleton
    public static Interceptor getRequestInterceptor() {
        return chain -> {
            String nowDateTime = TimeUtils.getNowString();
            //构建器
            Request.Builder builder = chain.request().newBuilder();
            //添加使用环境
            builder.addHeader("os","android");
            //添加版本号
            builder.addHeader("appVersionCode",String.valueOf(BuildConfig.VERSION_CODE));
            //添加版本名
            builder.addHeader("appVersionName",BuildConfig.VERSION_NAME);
            //添加日期时间
            builder.addHeader("datetime",nowDateTime);
            //返回
            return chain.proceed(builder.build());
        };
    }

    /**
     * 返回拦截器(响应拦截器)
     * @return Interceptor
     */
    private static Interceptor getResponseInterceptor() {
        return chain -> {
            long requestTime = System.currentTimeMillis();
            Response response = chain.proceed(chain.request());
            LogUtils.i(TAG, "requestSpendTime=" + (System.currentTimeMillis() - requestTime) + "ms");
            return response;
        };
    }

    /**
     * 配置OkHttp
     * @return OkHttpClient
     */
    @Provides
    @Singleton
    public static OkHttpClient getOkHttpClient(Interceptor requestInterceptor) {
        //OkHttp构建器
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置网络请求超时时长，这里设置为6s
        builder.connectTimeout(10, TimeUnit.SECONDS);
        //添加请求拦截器，如果接口有请求头的话，可以放在这个拦截器里面
        builder.addInterceptor(requestInterceptor);
        //添加返回拦截器，可用于查看接口的请求耗时，对于网络优化有帮助
        builder.addInterceptor(getResponseInterceptor());
        //当程序在debug过程中则打印数据日志，方便调试用。
        if (BuildConfig.DEBUG) {
            //iNetworkRequiredInfo不为空且处于debug状态下则初始化日志拦截器
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            //设置要打印日志的内容等级，BODY为主要内容，还有BASIC、HEADERS、NONE。
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //将拦截器添加到OkHttp构建器中
            builder.addInterceptor(httpLoggingInterceptor);
        }
        //OkHttp配置完成
        return builder.build();
    }

    /**
     * 配置Retrofit
     * @return Retrofit
     */
    @Provides
    @Singleton
    public static Retrofit.Builder getRetrofitBuilder(OkHttpClient okHttpClient) {
        //初始化Retrofit  Retrofit是对OKHttp的封装，通常是对网络请求做处理，也可以处理返回数据。
        //Retrofit构建器
        Retrofit.Builder builder = new Retrofit.Builder();
        //设置访问地址
        builder.baseUrl("https://cn.bing.com");
        //设置OkHttp客户端，传入上面写好的方法即可获得配置后的OkHttp客户端。
        builder.client(okHttpClient);
        //设置数据解析器 会自动把请求返回的结果（json字符串）通过Gson转化工厂自动转化成与其结构相符的实体Bean
        builder.addConverterFactory(GsonConverterFactory.create());
        //设置请求回调，使用RxJava 对网络返回进行处理
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        //最后返回即可
        return builder;
    }
}
