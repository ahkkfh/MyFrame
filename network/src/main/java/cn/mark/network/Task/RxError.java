package cn.mark.network.Task;

import cn.mark.network.retrofit.bean.InfoBean;

/**
 * Created by yaoping on 2016/5/26.
 */

public class RxError extends Throwable {
    private String error_code;
    private String error_messag;

    public RxError(InfoBean infoBean) {
        error_code = infoBean.error_code;
        error_messag = infoBean.error_msg;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_messag() {
        return error_messag;
    }

    public void setError_messag(String error_messag) {
        this.error_messag = error_messag;
    }
}
