package cn.mark.network.moduls.provider;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.mark.network.db.DaoSession;
import cn.mark.network.db.UserDao;
import cn.mark.network.moduls.component.DaggerDaoSessionComponent;
import cn.mark.network.moduls.component.DaoSessionComponent;
import dagger.Module;
import dagger.Provides;

/**
 * Created by yaoping on 2016/5/26.
 */
@Module
public class GreenDaoProvider {
    @Inject
    DaoSession daoSession;

    public GreenDaoProvider() {
        DaoSessionComponent component = DaggerDaoSessionComponent.builder()
                .daoSessionProvider(new DaoSessionProvider()).build();
        component.inject(this);
    }

    @Provides
    @Singleton
    UserDao providerUserDao() {
        return daoSession.getUserDao();
    }
}
