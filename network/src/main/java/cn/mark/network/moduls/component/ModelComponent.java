package cn.mark.network.moduls.component;

import javax.inject.Singleton;

import cn.mark.network.moduls.provider.ModelProvider;
import cn.mark.network.state.ApplicationState;
import dagger.Component;

/**
 * Created by yaoping on 2016/5/26.
 */
@Component(modules = {ModelProvider.class})
@Singleton
public interface ModelComponent {
    void inject(ApplicationState states);
}
