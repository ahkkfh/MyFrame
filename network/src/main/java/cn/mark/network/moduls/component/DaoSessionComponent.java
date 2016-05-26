package cn.mark.network.moduls.component;

import javax.inject.Singleton;

import cn.mark.network.moduls.provider.DaoSessionProvider;
import cn.mark.network.moduls.provider.GreenDaoProvider;
import dagger.Component;

/**
 * Created by yaoping on 2016/5/26.
 */
@Component(modules = {DaoSessionProvider.class})
@Singleton
public interface DaoSessionComponent {
    void inject(GreenDaoProvider greenDaoProvider);
}
