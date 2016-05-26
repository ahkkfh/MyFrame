package cn.mark.network.Task;

import java.util.HashMap;
import java.util.Map;

import cn.mark.network.retrofit.bean.InfoBean;
import cn.mark.utils.AppSystemUtil;
import cn.mark.utils.DeviceUtil;

/**
 * Created by yaoping on 2016/5/26.
 */

public abstract class PubParamNetExecuter<T extends InfoBean> extends RxJavaNet<T> {
    public static final String os_version_tag = "os_version";
    public static final String device_name_tag = "device";
    public static final String device_type_tag = "device_type";
    public static final String version_code_tag = "version_code";
    public static final String device_id_tag = "device_id";
    public static final String time_stamp_tag = "time_stamp";
    public static final String user_id_tag = "user_id";
    public static final String token_tag = "token";

    public Map<String, Object> params = new HashMap<>();

    public Object timeObjec;

    public PubParamNetExecuter() {
        params.put(os_version_tag, AppSystemUtil.getOSVERSION());
        params.put(device_name_tag, AppSystemUtil.getDeviceName());
        params.put(device_type_tag, AppSystemUtil.getDeviceType());
        params.put(version_code_tag, AppSystemUtil.getVersionCode());
        params.put(os_version_tag, AppSystemUtil.getOSVERSION());
        params.put(device_id_tag, AppSystemUtil.getDeviceId());
        params.put(user_id_tag, AppSystemUtil.getAppUserID());
        timeObjec = System.currentTimeMillis();
        params.put(time_stamp_tag, timeObjec);
    }

    public void setTokenTag(String token) {
        params.put(token_tag, DeviceUtil.getMD5Value(timeObjec + token));
    }

    public void setTokenTag(String token, Object value) {
        setTimeObjec(value);
        params.put(token_tag, DeviceUtil.getMD5Value(value + token));
    }

    public void setTimeObjec(Object objec) {
        this.timeObjec = objec;
        params.put(time_stamp_tag, objec);
    }

    public String getTimeStampedToken(String token) {
        return DeviceUtil.getMD5Value(timeObjec + token);
    }

    public Object getTimeStamp() {
        return timeObjec;
    }
}
