package cn.mark.network.retrofit.service;

import java.util.Map;

import cn.mark.network.retrofit.bean.userjson.UserBean;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by yaoping on 2016/5/26.
 */
public interface UserService {
    //登录
    @POST("/User/login")
    @FormUrlEncoded
    Observable<UserBean> login(@QueryMap Map<String, Object> params, @Field("account") String account, @Field("password") String password);

    //查询是否更新apk信息
//    @POST("/Upgrade/Check")
//    @FormUrlEncoded
//    Observable<UpdateApkBean> updateApk(@QueryMap Map<String, Object> params, @Field("version_code") String version_code);


    //下载Apk
    @GET
    Call<ResponseBody> downloadApk(@Url String fileUrl);

}
