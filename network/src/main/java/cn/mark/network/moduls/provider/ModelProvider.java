package cn.mark.network.moduls.provider;

import javax.inject.Singleton;

import cn.mark.network.model.UserModel;
import dagger.Module;
import dagger.Provides;

/**
 * Created by yaoping on 2016/5/26.
 */
@Module
public class ModelProvider {
    @Provides
    @Singleton
    UserModel providerUserModel() {
        return new UserModel();
    }
}
