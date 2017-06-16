package longimage.bigImageView.view;

/***
 * @author marks.luo
 * @Description: TODO(图片保存回调借口)
 * @date:2017-06-14 11:56
 */
public interface ImageSaveCallback {
    void onSuccess(String uri);

    void onFail(Throwable t);
}
