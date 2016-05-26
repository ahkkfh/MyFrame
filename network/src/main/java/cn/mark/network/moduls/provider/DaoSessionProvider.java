package cn.mark.network.moduls.provider;

import android.util.Log;

import javax.inject.Singleton;

import cn.mark.network.ApplicationHelper;
import cn.mark.network.db.DaoMaster;
import cn.mark.network.db.DaoSession;
import dagger.Module;
import dagger.Provides;

/**
 * Created by yaoping on 2016/5/26.
 */
@Module
public class DaoSessionProvider {
    private static final String DB_NAME = "MyFrame-db";

    @Provides
    @Singleton
    public DaoSession providerDaoSession() {
        Log.v("dagger", "test provideDaoSession ");
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(ApplicationHelper.instance(), DB_NAME, null);
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        return daoMaster.newSession();
    }
}
