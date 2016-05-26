package cn.mark.network.model;

import javax.inject.Inject;

import cn.mark.network.db.User;
import cn.mark.network.db.UserDao;
import cn.mark.network.moduls.component.DaggerGreenDaoComponent;
import cn.mark.network.moduls.component.GreenDaoComponent;
import cn.mark.utils.AppSystemUtil;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by yaoping on 2016/5/26.
 * 对数据库中数据进行操作
 */
public class UserModel {
    @Inject
    UserDao mUserDao;

    User loginedUser;

    public UserModel() {
        GreenDaoComponent component = DaggerGreenDaoComponent.create();
        component.inject(this);
    }

    //获取user信息
    public User getLoginedUser() {
        if (null == loginedUser) {//判断是否存在user对象，不存在查询数据库
            QueryBuilder<User> queryBuilder = mUserDao.queryBuilder();
            queryBuilder.where(UserDao.Properties.IsLogin.eq(Boolean.TRUE));//设置查询条件
            if (0 < queryBuilder.list().size()) {
                loginedUser = queryBuilder.list().get(0);
            }
        }
        if (null != loginedUser) {//存在的情况直接获取数据
            AppSystemUtil.setAppUserID(loginedUser.getUser_id());
        }
        return loginedUser;
    }


    public void loginOut() {
        User muser = getLoginedUser();
        if (null != muser) {
            muser.setIsLogin(Boolean.FALSE);
            AppSystemUtil.setAppUserID(-1);
            AppSystemUtil.setUserToken("-1");
            updateUser(muser);
        }
        loginedUser = null;
    }

    private void updateUser(User muser) {
        if (null == muser) {
            return;
        }
        mUserDao.update(muser);
    }

    public void setLoginedUser(User user) {
        if (null == user)
            return;

        //清空原来的数据
        loginOut();
        user.setIsLogin(Boolean.TRUE);
        QueryBuilder<User> queryBuilder = mUserDao.queryBuilder();
        queryBuilder.where(UserDao.Properties.User_id.eq(user.getUser_id()));

        if (0 < queryBuilder.list().size()) {
            User mUser = queryBuilder.list().get(0);
            setData(mUser, user);
            mUserDao.update(mUser);
        } else {
            mUserDao.insert(user);
        }
        setData(loginedUser, user);
    }

    public void setData(User targetUser, User sourceUser) {
        if (null != targetUser && null != sourceUser) {
            targetUser.setEmail(sourceUser.getEmail());
            targetUser.setImage(sourceUser.getImage());
            targetUser.setIsLogin(sourceUser.getIsLogin());
            targetUser.setNickname(sourceUser.getNickname());
            targetUser.setPhone(sourceUser.getPhone());
            targetUser.setPhone_verify(sourceUser.getPhone_verify());
            targetUser.setTaobao_phone(sourceUser.getTaobao_phone());
            targetUser.setTaobao_phone_verify(sourceUser.getTaobao_phone_verify());
            targetUser.setUser_id(sourceUser.getUser_id());
            targetUser.setUsername(sourceUser.getUsername());
            targetUser.setToken(sourceUser.getToken());
        }
    }
}
