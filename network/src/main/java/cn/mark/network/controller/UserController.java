package cn.mark.network.controller;

import android.util.Base64;

import cn.mark.network.Task.user.ActionUserLogin;
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
        return null;
    }

    public interface UserLoginCallback extends UserUiCallback {
        void userLogin(String account, String password);
    }

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
