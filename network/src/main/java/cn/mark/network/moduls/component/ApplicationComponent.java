package cn.mark.network.moduls.component;

import javax.inject.Singleton;

import cn.mark.network.ApplicationHelper;
import cn.mark.network.moduls.provider.ApplicationStateProvider;
import dagger.Component;

/**
 * Created by yaoping on 2016/5/26.
 */
@Component(modules = {ApplicationStateProvider.class})
@Singleton
public interface ApplicationComponent {
    void inject(ApplicationHelper applicationHelper);
}
