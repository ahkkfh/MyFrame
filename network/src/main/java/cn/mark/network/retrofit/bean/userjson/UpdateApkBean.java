package cn.mark.network.retrofit.bean.userjson;

import cn.mark.network.retrofit.bean.InfoBean;

/**
 * Created by yaoping on 2016/6/7.
 */
public class UpdateApkBean extends InfoBean {
    public String upgrade_type;//	string	"0":有新版本但不提示，"1":弹窗提示更新，"2":强制更新，"10"或其他则已是最新版本
    public String url;    //string	安装包下载或其他地址
    public String version;    //string	新版客户端的版本号
    public String title;    //string	提示标题(可选择使用，如果后台不设置，一般默认为空)
    public String msg;//string	提示信息(可选择使用，如果后台不设置，一般默认为空)
    public String content;//	string	升级日志或其他内容(可选择使用，如果后台不设置，一般默认为空)


}
