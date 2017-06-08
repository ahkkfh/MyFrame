package cn.mark.network.Task.user;

import org.reactivestreams.Subscription;

import cn.mark.network.Task.PubParamNetExecuter;
import cn.mark.network.retrofit.RetrofitServer;
import cn.mark.network.retrofit.bean.userjson.UserBean;
import cn.mark.utils.DeviceUtil;

/**
 * Created by yaoping on 2016/5/26.
 */
public class ActionUserLogin extends PubParamNetExecuter<UserBean> {
    private String account;
    private String password;

    public ActionUserLogin(String account, String password) {
        this.account = account;
        this.password = password;
        srvObservable = RetrofitServer.getInstance()
                .getUserService()
                .login(params, account, DeviceUtil.getMD5Value(getTimeStamp() + DeviceUtil.getMD5Value(password)));
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public void onSubscribe(Subscription s) {
        
    }

    @Override
    public void onNext(UserBean data) {

    }

}
