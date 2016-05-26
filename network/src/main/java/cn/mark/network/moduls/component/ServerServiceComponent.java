package cn.mark.network.moduls.component;

import javax.inject.Singleton;

import cn.mark.network.moduls.provider.ServerServiceProvider;
import cn.mark.network.retrofit.RetrofitServer;
import dagger.Component;

/**
 * Created by yaoping on 2016/5/26.
 */
@Component(modules = {ServerServiceProvider.class})
@Singleton
public interface ServerServiceComponent {
    void inject(RetrofitServer retrofitServer);
}
