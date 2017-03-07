package cn.mark.utils.longImageView;

import android.graphics.PointF;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-03-07 18:04
 *事件侦听器，允许通知平移和缩放事件的活动。
 *  您的代码的初始化和调用不会触发事件; 触摸事件和动画做。
 *  此监听器中的方法将在UI线程上调用，并且可能会频繁调用 - 您的实现应该很快返回。
 */
public interface OnStateChangedListener {
    /**
     * The scale has changed. Use with {@link #getMaxScale()} and {@link #getMinScale()} to determine
     * whether the image is fully zoomed in or out.
     * @param newScale The new scale.
     * @param origin Where the event originated from - one of {@link #ORIGIN_ANIM}, {@link #ORIGIN_TOUCH}.
     */
    void onScaleChanged(float newScale, int origin);

    /**
     * The source center has been changed. This can be a result of panning or zooming.
     * @param newCenter The new source center point.
     * @param origin Where the event originated from - one of {@link #ORIGIN_ANIM}, {@link #ORIGIN_TOUCH}.
     */
    void onCenterChanged(PointF newCenter, int origin);
}
