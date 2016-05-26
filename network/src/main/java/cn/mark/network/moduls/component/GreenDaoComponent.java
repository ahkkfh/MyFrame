package cn.mark.network.moduls.component;

import javax.inject.Singleton;

import cn.mark.network.model.UserModel;
import cn.mark.network.moduls.provider.GreenDaoProvider;
import dagger.Component;

/**
 * Created by yaoping on 2016/5/26.
 */
@Component(modules = {GreenDaoProvider.class})
@Singleton
public interface GreenDaoComponent {
    void inject(UserModel userModel);
}
