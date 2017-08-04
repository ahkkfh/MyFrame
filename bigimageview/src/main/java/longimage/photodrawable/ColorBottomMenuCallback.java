package longimage.photodrawable;

import android.animation.Animator;

/***
 * @author marks.luo
 * @Description: ()
 * @date:2017-08-04 15:45
 *
 */
public interface ColorBottomMenuCallback extends ColorPagerCallback {
    int INVALID_POSITION = -1;
    int UPDATE_MODE_DIRECT = 0;
    int UPDATE_MODE_ANIMATE = 1;
    int UPDATE_MODE_OUTER = 2;

    void updateMenuScrollPosition(int var1, float var2);

    void updateMenuScrollState(int var1);

    void updateMenuScrollData();

    void setMenuUpdateMode(int var1);

    void lockMenuUpdate();

    void unlockMenuUpdate();

    public interface Updater {
        Animator getUpdater(int var1, int var2);

        boolean visibleFirst();
    }
}
