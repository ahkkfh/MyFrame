package cn.mark.network.moduls.component;

import javax.inject.Singleton;

import cn.mark.network.controller.MainController;
import cn.mark.network.moduls.provider.ControllersProvider;
import dagger.Component;

/**
 * Created by yaoping on 2016/5/26.
 */
@Component(modules = {ControllersProvider.class})
@Singleton
public interface ControllersComponent {
    void inject(MainController mainController);
}
