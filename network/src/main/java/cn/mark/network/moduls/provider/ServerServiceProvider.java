package cn.mark.network.moduls.provider;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.mark.network.moduls.component.DaggerRetrofitComponent;
import cn.mark.network.moduls.component.RetrofitComponent;
import cn.mark.network.retrofit.service.UserService;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by yaoping on 2016/5/26.
 */
@Module
public class ServerServiceProvider {
    @Inject
    Retrofit retrofit;

    public ServerServiceProvider() {
        RetrofitComponent retrofitComponent = DaggerRetrofitComponent.builder()
                .retrofitProvider(new RetrofitProvider())
                .build();
        retrofitComponent.inject(this);
        Log.i("lbxx", "retrofit=" + retrofit);
    }

    @Provides
    @Singleton
    UserService providerUserService() {
        return retrofit.create(UserService.class);
    }
}
