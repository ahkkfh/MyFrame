package cn.mark.network.retrofit.param;

import cn.mark.utils.AppSystemUtil;
import cn.mark.utils.DeviceUtil;

/**
 * Created by yaoping on 2016/5/26.
 */

public class PutParams {
    public String os_version;
    public String device;
    public String device_type;
    public int version_code;
    public String device_id;
    public int user_id;
    public String token;
    public long time_stamp;

    public PutParams() {

        os_version = AppSystemUtil.getOSVERSION();
        device = AppSystemUtil.getDeviceName();
        device_type = AppSystemUtil.getDeviceType();
        version_code = AppSystemUtil.getVersionCode();
        device_id = AppSystemUtil.getDeviceId();
        user_id = AppSystemUtil.getAppUserID();
    }

    public PutParams(String token) {
        os_version = AppSystemUtil.getOSVERSION();
        device = AppSystemUtil.getDeviceName();
        device_type = AppSystemUtil.getDeviceType();
        version_code = AppSystemUtil.getVersionCode();
        device_id = AppSystemUtil.getDeviceId();
        user_id = AppSystemUtil.getAppUserID();
        this.token = DeviceUtil.getMD5Value(time_stamp + token);
    }

    public void setToken(String token) {
        time_stamp = System.currentTimeMillis();
        this.token = DeviceUtil.getMD5Value(time_stamp + token);
    }
}
