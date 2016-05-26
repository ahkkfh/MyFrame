package cn.mark.network.moduls.provider;

import javax.inject.Singleton;

import cn.mark.network.controller.UserController;
import dagger.Module;
import dagger.Provides;

/**
 * Created by yaoping on 2016/5/26.
 */
@Module
public class ControllersProvider {
    @Singleton
    @Provides
    UserController providerUserController() {
        return new UserController();
    }
}
