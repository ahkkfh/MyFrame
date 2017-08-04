package longimage.photodrawable;

import android.view.animation.Interpolator;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-08-04 15:44
 *
 */
public interface ColorPagerCallback {
    int SCROLL_STATE_IDLE = 0;
    int SCROLL_STATE_DRAGGING = 1;
    int SCROLL_STATE_SETTLING = 2;
    Interpolator ANIMATOR_INTERPOLATOR = new Interpolator() {
        public float getInterpolation(float t) {
            --t;
            return t * t * t * t * t + 1.0F;
        }
    };
}
