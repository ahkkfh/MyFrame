package cn.mark.utils.widget.tablayout.listener;

import android.support.annotation.DrawableRes;

/***
 * @author marks.luo
 * @Description: TODO(自定义tab的实体类)
 * @date:2017-04-25 14:51
 */
public interface CustomTabEntity {
    /**
     * 获取tab的标题
     *
     * @return
     */
    String getTabTitle();

    /**
     * 获取tab的icon
     *
     * @return
     */
    @DrawableRes
    int getTabSelectedIcon();

    /***
     * 获取未选中tab的icon
     * @return
     */
    @DrawableRes
    int getTabUnselectedIcon();
}
