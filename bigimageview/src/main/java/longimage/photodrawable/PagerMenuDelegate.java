package longimage.photodrawable;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-08-04 15:43
 *
 */
public class PagerMenuDelegate {
    private static final String TAG = "ColorPagerMenuDelegate";
    private static final boolean DBG = true;
    private static final float MENU_SCROLL_OFFSET = 0.3F;
    private static final float MENU_SCROLL_OFFSET_LOW = 0.3F;
    private static final float MENU_SCROLL_OFFSET_HIGH = 0.7F;
    private static final int MENU_SCROLL_STATE_IDLE = 0;
    private static final int MENU_SCROLL_STATE_DOWN = 1;
    private static final int MENU_SCROLL_STATE_UP = 2;
    private static final int MENU_SCROLL_STATE_OUT = 3;
    private float mLastMenuOffset = -1.0F;
    private boolean mLastDirection = true;
    private boolean mNextDirection = true;
    private boolean mIsBeingSettled = false;
    private int mLastItem = -1;
    private int mNextItem = -1;
    private int mMenuScrollState = 0;
    private CustomViewpager.OnPageMenuChangeListener mOnPageMenuChangeListener = null;
    private ColorBottomMenuCallback mCallback = null;
    private CustomViewpager mPager = null;

    public PagerMenuDelegate(CustomViewpager pager) {
        this.mPager = pager;
    }

    void setOnPageMenuChangeListener(CustomViewpager.OnPageMenuChangeListener listener) {
        this.mOnPageMenuChangeListener = listener;
    }

    void bindSplitMenuCallback(ColorBottomMenuCallback callback) {
        this.mCallback = callback;
    }

    void setSettleState() {
        this.mIsBeingSettled = true;
    }

    void onPageMenuSelected(int position) {
        this.mLastItem = this.mPager.getCurrentItem();
        this.mNextItem = position;
        if(this.mPager.getDragState() || this.mIsBeingSettled) {
            this.setMenuUpdateMode(2);
        }

        if(this.mOnPageMenuChangeListener != null) {
            this.mOnPageMenuChangeListener.onPageMenuSelected(position);
        }

    }

    void onPageMenuScrollStateChanged(int state) {
        if(this.mPager.getScrollState() == 0) {
            this.mIsBeingSettled = false;
            this.setMenuUpdateMode(1);
        }

        if(this.mOnPageMenuChangeListener != null) {
            this.mOnPageMenuChangeListener.onPageMenuScrollStateChanged(state);
        }

    }

    void pageMenuScrolled(int currentPage, float pageOffset) {
        float menuOffset = this.getMenuOffset(currentPage, pageOffset);
        if(this.mLastMenuOffset != menuOffset) {
            if(menuOffset == 1.0F || menuOffset < this.mLastMenuOffset) {
                this.onPageMenuScrollDataChanged();
            }

            this.mLastMenuOffset = menuOffset;
        }

        this.onPageMenuScrolled(-1, menuOffset);
    }

    void updateNextItem(float deltaX) {
        CustomViewpager.ItemInfo ii = this.mPager.infoForCurrentScrollPosition();
        int currentPage = ii.position;
        this.updateDirection(deltaX > 0.0F);
        if(this.mNextDirection) {
            this.mLastItem = currentPage;
            this.mNextItem = Math.min(currentPage + 1, this.mPager.getAdapter().getCount() - 1);
        } else {
            this.mLastItem = currentPage;
            this.mNextItem = currentPage;
        }

    }

    void updateDirection(boolean direction) {
        this.mLastDirection = this.mNextDirection;
        this.mNextDirection = direction;
    }

    private void setMenuUpdateMode(int updateMode) {
        if(this.mCallback != null) {
            this.mCallback.setMenuUpdateMode(updateMode);
        }

    }

    private float getMenuOffset(int currentPage, float pageOffset) {
        float totalOffset;
        if(this.mNextItem == this.mLastItem) {
            totalOffset = pageOffset;
        } else {
            int menuOffset = Math.min(this.mNextItem, this.mLastItem);
            int itemCount = Math.abs(this.mNextItem - this.mLastItem);
            totalOffset = ((float)currentPage + pageOffset - (float)menuOffset) / (float)itemCount;
        }

        float menuOffset1;
        if(totalOffset > 0.0F && totalOffset <= 0.3F) {
            menuOffset1 = totalOffset / 0.3F;
        } else if(totalOffset > 0.3F && totalOffset < 0.7F) {
            menuOffset1 = 1.0F;
        } else if(totalOffset >= 0.7F) {
            menuOffset1 = (1.0F - totalOffset) / 0.3F;
        } else {
            menuOffset1 = 0.0F;
        }

        return menuOffset1;
    }

    private void onPageMenuScrolled(int index, float offset) {
        if(this.mOnPageMenuChangeListener != null) {
            this.mOnPageMenuChangeListener.onPageMenuScrolled(index, offset);
        }

    }

    private void onPageMenuScrollDataChanged() {
        if(this.mOnPageMenuChangeListener != null) {
            this.mOnPageMenuChangeListener.onPageMenuScrollDataChanged();
        }

    }
}
