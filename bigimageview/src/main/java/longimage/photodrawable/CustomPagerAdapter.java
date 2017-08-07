package longimage.photodrawable;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

/***
 * @author marks.luo
 * @Description: ()
 * @date:2017-08-04 15:42
 *
 */
public abstract class CustomPagerAdapter {
    private DataSetObservable mObservable = new DataSetObservable();
    public static final int POSITION_UNCHANGED = -1;
    public static final int POSITION_NONE = -2;

    public CustomPagerAdapter() {
    }

    public abstract int getCount();

    public void startUpdate(ViewGroup container) {
        this.startUpdate((View) container);
    }

    public Object instantiateItem(ViewGroup container, int position) {
        return this.instantiateItem((View) container, position);
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        this.destroyItem((View) container, position, object);
    }

    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        this.setPrimaryItem((View) container, position, object);
    }

    public void finishUpdate(ViewGroup container) {
        this.finishUpdate((View) container);
    }

    /**
     * @deprecated
     */
    public void startUpdate(View container) {
    }

    /**
     * @deprecated
     */
    public Object instantiateItem(View container, int position) {
        throw new UnsupportedOperationException("Required method instantiateItem was not overridden");
    }

    /**
     * @deprecated
     */
    public void destroyItem(View container, int position, Object object) {
        throw new UnsupportedOperationException("Required method destroyItem was not overridden");
    }

    /**
     * @deprecated
     */
    public void setPrimaryItem(View container, int position, Object object) {
    }

    /**
     * @deprecated
     */
    public void finishUpdate(View container) {
    }

    public abstract boolean isViewFromObject(View var1, Object var2);

    public Parcelable saveState() {
        return null;
    }

    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    public int getItemPosition(Object object) {
        return -1;
    }

    public void notifyDataSetChanged() {
        this.mObservable.notifyChanged();
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        this.mObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        this.mObservable.unregisterObserver(observer);
    }

    public CharSequence getPageTitle(int position) {
        return null;
    }

    public float getPageWidth(int position) {
        return 1.0F;
    }
}
