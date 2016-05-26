package cn.mark.network.state;

import android.util.Log;

import javax.inject.Inject;

import cn.mark.network.db.User;
import cn.mark.network.model.UserModel;
import cn.mark.network.moduls.component.DaggerModelComponent;
import cn.mark.network.moduls.component.ModelComponent;

/**
 * Created by yaoping on 2016/5/26.
 */

public class ApplicationState implements UserState {
    @Inject
    UserModel userModel;

    public ApplicationState() {
        ModelComponent component = DaggerModelComponent.create();
        component.inject(this);
        Log.v("loginout", "ApplicationState " + this);
    }

    @Override
    public User getLoginedUser() {
        return userModel.getLoginedUser();
    }

    @Override
    public void loginOut() {
        userModel.loginOut();
    }

    @Override
    public void saveUser(User user) {
        userModel.setLoginedUser(user);
    }
}
