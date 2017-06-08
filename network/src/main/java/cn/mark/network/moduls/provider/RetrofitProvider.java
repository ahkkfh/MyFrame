package cn.mark.network.moduls.provider;


import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.inject.Singleton;

import cn.mark.utils.Constant;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yaoping on 2016/5/26.
 */
@Module
public class RetrofitProvider {
    public RetrofitProvider() {
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit() {//提供Retrofit对象
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())//使用Stetho的抓包数据
//                .addInterceptor(interceptor)
                .build();
        //
        return new Retrofit.Builder()
                .baseUrl(Constant.SERVER)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())//使用Gson解析数据
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


}
