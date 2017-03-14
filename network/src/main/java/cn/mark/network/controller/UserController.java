package cn.mark.network.controller;

import android.content.Context;
import android.util.Base64;
import cn.mark.network.Task.user.ActionUserLogin;
import cn.mark.network.Task.utils.ActionDownloadApk;
import cn.mark.network.retrofit.bean.userjson.UserBean;
import cn.mark.utils.AppSystemUtil;

/**
 * Created by yaoping on 2016/5/26.
 */
public class UserController extends BaseUIController<UserController.UserUi, UserController.UserUiCallback> {

    public UserController() {
    }

    public interface UserUi extends BaseUIController.Ui<UserUiCallback> {

    }

    public interface UserUiCallback {

    }

    public interface LoginUi extends UserUi {
        void userLoginBack(UserBean userBean);
    }

    public interface UpdateApkUi extends UserUi {
//        void FeachUpdateInfo(UpdateApkBean bean);

//        void downloadApk(DownloadBean bean);
    }

    @Override
    protected UserUiCallback createUiCallbacks(UserUi ui) {
        if (ui instanceof LoginUi) {
            return new UserLoginCallback() {
                @Override
                public void userLogin(String account, String password) {
                    new UserLoginTask(account, password).execute();
                }
            };
        }
        if (ui instanceof UpdateApkUi) {
            return new UpdateApkCallback() {
                @Override
                public void feachUpdateInfo(String version_code) {
//                    new UpdateApkTask(applicationState.getLoginedUser().getToken(), version_code).execute();
                }

                @Override
                public void downloadApk(Context context, String downloadUrl) {
//                    new ActionDownloadApk(context, downloadUrl).execute();
                }
            };
        }
        return null;
    }

    public interface UpdateApkCallback extends UserUiCallback {
        void feachUpdateInfo(String version_code);

        void downloadApk(Context context, String downloadUrl);
    }


    public interface UserLoginCallback extends UserUiCallback {
        void userLogin(String account, String password);
    }

//    public class UpdateApkTask extends FetchUpdateInfo {
//
//        public UpdateApkTask(String token, String version_code) {
//            super(token, version_code);
//        }
//
//        @Override
//        public void onNext(UpdateApkBean data) {
//            //循环获取UI，判断是否为登录UI，设置获取到数据给view层
//            for (Ui ui : getUis()) {
//                if (ui instanceof UpdateApkUi) {
//                    ((UpdateApkUi) ui).FeachUpdateInfo(data);
//                    break;
//                }
//            }
//        }
//    }

    public class UserLoginTask extends ActionUserLogin {
        public UserLoginTask(String account, String password) {
            super(account, password);
        }

        @Override
        public void onNextBackgroundSuccess(UserBean data) {
            if (null != data.data) {
                data.data.setIsLogin(Boolean.TRUE);//设置登录标记

                //两次base64解密
                data.data.setToken(new String(
                        Base64.decode(
                                Base64.decode(data.data.getToken().getBytes(), Base64.DEFAULT),
                                Base64.DEFAULT))
                );
                applicationState.saveUser(data.data);
                AppSystemUtil.setUserToken(applicationState.getLoginedUser().getToken());
            }
        }

        @Override
        public void onNext(UserBean data) {
            //循环获取UI，判断是否为登录UI，设置获取到数据给view层
            for (Ui ui : getUis()) {
                if (ui instanceof LoginUi) {
                    ((LoginUi) ui).userLoginBack(data);
                    break;
                }
            }

        }
    }

}
