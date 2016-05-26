package cn.mark.network.state;

import cn.mark.network.db.User;

/**
 * Created by yaoping on 2016/5/26.
 */

public interface UserState {
    User getLoginedUser();

    void loginOut();

    void saveUser(User user);
}
