package cn.mark.network.retrofit;


import javax.inject.Inject;

import cn.mark.network.moduls.component.DaggerServerServiceComponent;
import cn.mark.network.moduls.component.ServerServiceComponent;
import cn.mark.network.moduls.provider.ServerServiceProvider;
import cn.mark.network.retrofit.service.UserService;

/**
 * Created by yaoping on 2016/5/26.
 */

public class RetrofitServer {
    public static RetrofitServer retrofitServer;

    public static RetrofitServer getInstance() {
        if (null == retrofitServer) {
            retrofitServer = new RetrofitServer();
        }
        return retrofitServer;
    }

    public RetrofitServer() {
        ServerServiceComponent retrofitComponent = DaggerServerServiceComponent.builder()
                .serverServiceProvider(new ServerServiceProvider()).build();
        retrofitComponent.inject(this);
    }

    @Inject
    UserService userService;

    public UserService getUserService() {
        return userService;
    }
}
