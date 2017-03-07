package cn.mark.utils.longImageView;

import android.graphics.PointF;

import java.io.Serializable;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-03-07 17:19
 * 包裹显示图像的比例，中心和方向，以便在屏幕旋转时轻松恢复。
 */

public class ImageViewState implements Serializable {
    private float scale;

    private float centerX;

    private float centerY;

    private int orientation;

    public ImageViewState(float scale, PointF center, int orientation) {
        this.scale = scale;
        this.centerX = center.x;
        this.centerY = center.y;
        this.orientation = orientation;
    }

    public float getScale() {
        return scale;
    }

    public PointF getCenter() {
        return new PointF(centerX, centerY);
    }

    public int getOrientation() {
        return orientation;
    }

}
