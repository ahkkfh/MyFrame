package cn.mark.network.retrofit.service;

import java.util.Map;

import cn.mark.network.retrofit.bean.userjson.UserBean;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by yaoping on 2016/5/26.
 */
public interface UserService {
    //登录
    @POST("/User/login")
    @FormUrlEncoded
    Observable<UserBean> login(@QueryMap Map<String, Object> params, @Field("account") String account, @Field("password") String password);

}
