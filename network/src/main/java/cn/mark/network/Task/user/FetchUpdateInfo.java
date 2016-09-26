package cn.mark.network.Task.user;

import cn.mark.network.Task.PubParamNetExecuter;
import cn.mark.network.retrofit.RetrofitServer;
import cn.mark.network.retrofit.bean.userjson.UpdateApkBean;

/**
 * Created by yaoping on 2016/6/7.
 */
public class FetchUpdateInfo extends PubParamNetExecuter<UpdateApkBean> {

    public FetchUpdateInfo(String token, String version_code) {
        setTokenTag(token);
        srvObservable = RetrofitServer.getInstance().getUserService()
                .updateApk(params, version_code);
    }

    @Override
    public void onNext(UpdateApkBean data) {

    }
}
