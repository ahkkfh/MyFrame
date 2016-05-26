package cn.mark.network.moduls.provider;

import javax.inject.Singleton;

import cn.mark.network.controller.MainController;
import cn.mark.network.state.ApplicationState;
import dagger.Module;
import dagger.Provides;

/**
 * Created by yaoping on 2016/5/26.
 */
@Module
public class ApplicationStateProvider {

    @Provides
    @Singleton
    MainController providerMainController() {
        return new MainController();
    }

    @Provides
    @Singleton
    ApplicationState providerApplicationState() {
        return new ApplicationState();
    }
}
