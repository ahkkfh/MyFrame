package cn.mark.network.Task.utils;


import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import cn.mark.network.R;
import cn.mark.network.Task.PubParamNetExecuter;
import cn.mark.network.Task.RxJavaNet;
import cn.mark.network.retrofit.RetrofitServer;
import cn.mark.network.retrofit.bean.userjson.DownloadBean;
import cn.mark.utils.Constant;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yaoping on 2016/6/13.
 * 自定下载程序，重写execute方法，下载apk文件
 */
public class ActionDownloadApk extends RxJavaNet<DownloadBean> {
    private String apkUrl;
    private NotificationManager progressManager;
    private NotificationCompat.Builder progressBuilder;
    private Context context;
    private static final String defaultTitle = "LazyCatTravel";
    private static final String defaultContext = "懒猫旅行下载0%";
    private static final String defaultContext2 = "懒猫旅行下载";
    private static final String downloadSuccessText = "懒猫旅行下载完成";
    private int progress;
    private static final int maxProgress = 100;

    public ActionDownloadApk(Context context, String fileUrl) {
        apkUrl = fileUrl;
        this.context = context;
        initProgress();
    }

    private void initProgress() {
        progressManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        progressBuilder.setContentTitle(defaultTitle);
        progressBuilder.setContentText(defaultContext);
        progressBuilder.setSmallIcon(R.drawable.ic_launcher);
    }

    @Override
    public void execute() {
        Observable.just(apkUrl)
                .subscribeOn(Schedulers.io())//设置下面执行代码不在ui线程
                .flatMap(new Func1<String, Observable<DownloadBean>>() {//在这里进行下载操作
                    @Override
                    public Observable<DownloadBean> call(String url) {
                        //调用下载请求
                        Call<ResponseBody> call = RetrofitServer.getInstance().getUserService().downloadApk(url);
                        final DownloadBean bean = new DownloadBean();
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {//判断是否请求成功
                                    InputStream inputStream;
                                    FileOutputStream outputStream;
                                    try {
                                        File file = new File(Constant.saveApkPath);//设置文件保存的位置
                                        if (!file.exists()) {//文件目录不存在创建
                                            file.mkdir();
                                        }
                                        long contentLength = response.body().contentLength();//获取文件长度
                                        File apkFile = new File(Constant.saveApkPath, Constant.downloadFileName);
                                        inputStream = response.body().byteStream();
                                        outputStream = new FileOutputStream(apkFile);
                                        int count = 0;
                                        int len = 0;
                                        byte[] bytes = new byte[1024];
                                        do {
                                            int number = inputStream.read(bytes);
                                            if ((count - len) > (contentLength / 20) || len == 0) {
                                                //计算progress
                                                progress = (int) (((float) count / contentLength) * 100);
                                                progressBuilder.setProgress(maxProgress, progress, false);
                                                progressBuilder.setContentText(defaultContext2 + progress + "%");
                                                progressManager.notify(2, progressBuilder.build());
                                                len = count;
                                            }
                                            if (number <= 0) {
                                                progressBuilder.setProgress(maxProgress, maxProgress, false);
                                                progressBuilder.setContentText(downloadSuccessText);
                                                progressManager.notify(2, progressBuilder.build());
                                                bean.error_code = Constant.requestSuccess;
                                                bean.error_msg = "ok";
                                                bean.app_url = "file://" + apkFile.toString();
                                                clearNotify(2);
                                                break;
                                            }
                                            outputStream.write(bytes, 0, number);
                                        } while (true);
                                        inputStream.close();
                                        outputStream.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        bean.error_code = -1;
                                        bean.error_msg = e.getMessage();
                                        bean.app_url = "";
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                bean.error_code = -1;
                                bean.error_msg = t.getMessage();
                                bean.app_url = "";
                            }
                        });
                        return Observable.just(bean);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//设置在主线程
                .subscribe(this);

    }

    /**
     * 清除当前创建的通知栏
     */
    public void clearNotify(int notifyId) {
        progressManager.cancel(notifyId);//删除一个特定的通知ID对应的通知
//		mNotification.cancel(getResources().getString(R.string.app_name));
    }

    @Override
    public void onNext(DownloadBean data) {
    }
}
