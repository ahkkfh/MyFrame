package cn.mark.network.moduls.component;

import javax.inject.Singleton;

import cn.mark.network.moduls.provider.RetrofitProvider;
import cn.mark.network.moduls.provider.ServerServiceProvider;
import dagger.Component;

/**
 * Created by yaoping on 2016/5/26.
 */
@Singleton
@Component(modules = {RetrofitProvider.class})
public interface RetrofitComponent {
    void inject(ServerServiceProvider serviceProvider);
}
