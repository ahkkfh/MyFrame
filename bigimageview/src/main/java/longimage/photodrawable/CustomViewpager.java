package longimage.photodrawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityRecord;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.Scroller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import longimage.ViewHelper;

/***
 * @author marks.luo
 * @Description: (自定义viewpager)
 * @date:2017-08-04 15:40
 *
 */
public class CustomViewpager extends ViewGroup {

    private static final String TAG = "CustomViewpager";
    private static final boolean DEBUG = false;
    private static final boolean USE_CACHE = false;
    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
    private static final int MAX_SETTLE_DURATION = 600;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    private static final int DEFAULT_GUTTER_SIZE = 16;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final int[] LAYOUT_ATTRS = new int[]{16842931};
    private int mExpectedAdapterCount;
    private static final Comparator<ItemInfo> COMPARATOR = new Comparator<CustomViewpager.ItemInfo>() {
        public int compare(CustomViewpager.ItemInfo lhs, CustomViewpager.ItemInfo rhs) {
            return lhs.position - rhs.position;
        }
    };
    private static final Interpolator sInterpolator;
    private final ArrayList<ItemInfo> mItems = new ArrayList();
    private final CustomViewpager.ItemInfo mTempItem = new CustomViewpager.ItemInfo();
    private final Rect mTempRect = new Rect();
    private CustomPagerAdapter mAdapter;
    private int mCurItem;
    private int mRestoredCurItem = -1;
    private Parcelable mRestoredAdapterState = null;
    private ClassLoader mRestoredClassLoader = null;
    private Scroller mScroller;
    private CustomViewpager.PagerObserver mObserver;
    private int mPageMargin;
    private Drawable mMarginDrawable;
    private int mTopPageBounds;
    private int mBottomPageBounds;
    private float mFirstOffset = -3.4028235E38F;
    private float mLastOffset = 3.4028235E38F;
    private int mChildWidthMeasureSpec;
    private int mChildHeightMeasureSpec;
    private boolean mInLayout;
    private boolean mScrollingCacheEnabled;
    private boolean mPopulatePending;
    private int mOffscreenPageLimit = 1;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;
    private boolean mIgnoreGutter;
    private int mDefaultGutterSize;
    private int mGutterSize;
    private int mTouchSlop;
    private float mLastMotionX;
    private float mLastMotionY;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private int mActivePointerId = -1;
    private static final int INVALID_POINTER = -1;
    private VelocityTracker mVelocityTracker;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int mFlingDistance;
    private int mCloseEnough;
    private static final int CLOSE_ENOUGH = 2;
    private boolean mFakeDragging;
    private long mFakeDragBeginTime;
    private EdgeEffect mLeftEdge;
    private EdgeEffect mRightEdge;
    private boolean mFirstLayout = true;
    private boolean mNeedCalculatePageOffsets = false;
    private boolean mCalledSuper;
    private int mDecorChildCount;
    private CustomViewpager.OnPageChangeListener mOnPageChangeListener;
    private CustomViewpager.OnPageChangeListener mInternalPageChangeListener;
    private CustomViewpager.OnAdapterChangeListener mAdapterChangeListener;
    private CustomViewpager.PageTransformer mPageTransformer;
    private Method mSetChildrenDrawingOrderEnabled;
    private static final int DRAW_ORDER_DEFAULT = 0;
    private static final int DRAW_ORDER_FORWARD = 1;
    private static final int DRAW_ORDER_REVERSE = 2;
    private int mDrawingOrder;
    private ArrayList<View> mDrawingOrderedChildren;
    private static final CustomViewpager.ViewPositionComparator sPositionComparator;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_SETTLING = 2;
    private final Runnable mEndScrollRunnable = new Runnable() {
        public void run() {
            CustomViewpager.this.setScrollState(0);
            CustomViewpager.this.populate();
        }
    };
    private int mScrollState = 0;
    private static final float DURATION_SCALE = 1.0F;
    private PagerMenuDelegate mMenuDelegate = new PagerMenuDelegate(this);
    private boolean mDisableTouch = false;
    private int validYSliding = (int) (AppConfig.PhoneInfo.screenheight * 0.15);

    /**
     * 最小可切换Page的偏移量
     **/
    private float minPageOffset = 0.09f;

    public void setMinPageOffset(float pageOffset) {
        minPageOffset = pageOffset;
    }

    public CustomViewpager(Context context) {
        super(context);
        this.initViewPager();
    }

    public CustomViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initViewPager();
    }

    void initViewPager() {
        this.setWillNotDraw(false);
        this.setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        this.setFocusable(true);
        Context context = this.getContext();
        this.mScroller = new Scroller(context, sInterpolator);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        float density = context.getResources().getDisplayMetrics().density;
        this.mTouchSlop = configuration.getScaledPagingTouchSlop();
        this.mMinimumVelocity = (int) (400.0F * density);
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffect(context);
        this.mRightEdge = new EdgeEffect(context);
        this.mFlingDistance = (int) (25.0F * density);
        this.mCloseEnough = (int) (2.0F * density);
        this.mDefaultGutterSize = (int) (16.0F * density);
        this.setAccessibilityDelegate(new CustomViewpager.MyAccessibilityDelegate());
        if (ViewCompat.getImportantForAccessibility(this) == 0) {
            ViewCompat.setImportantForAccessibility(this, 1);
        }

    }

    protected void onDetachedFromWindow() {
        this.removeCallbacks(this.mEndScrollRunnable);
        super.onDetachedFromWindow();
    }

    private void setScrollState(int newState) {
        if (this.mScrollState != newState) {
            this.mScrollState = newState;
            if (this.mPageTransformer != null) {
                this.enableLayers(newState != 0);
            }

            if (this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageScrollStateChanged(newState);
            }

            this.mMenuDelegate.onPageMenuScrollStateChanged(this.mScrollState);
        }
    }

    public void setAdapter(CustomPagerAdapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mObserver);
            this.mAdapter.startUpdate(this);

            for (int oldAdapter = 0; oldAdapter < this.mItems.size(); ++oldAdapter) {
                CustomViewpager.ItemInfo wasFirstLayout = this.mItems.get(oldAdapter);
                this.mAdapter.destroyItem(this, wasFirstLayout.position, wasFirstLayout.object);
            }

            this.mAdapter.finishUpdate(this);
            this.mItems.clear();
            this.removeNonDecorViews();
            this.mCurItem = 0;
            this.scrollTo(0, 0);
        }

        CustomPagerAdapter var4 = this.mAdapter;
        this.mAdapter = adapter;
        this.mExpectedAdapterCount = 0;
        if (this.mAdapter != null) {
            if (this.mObserver == null) {
                this.mObserver = new CustomViewpager.PagerObserver();
            }

            this.mAdapter.registerDataSetObserver(this.mObserver);
            this.mPopulatePending = false;
            boolean var5 = this.mFirstLayout;
            this.mFirstLayout = true;
            this.mExpectedAdapterCount = this.mAdapter.getCount();
            if (this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                this.setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            } else if (!var5) {
                this.populate();
            } else {
                this.requestLayout();
            }
        }

        if (this.mAdapterChangeListener != null && var4 != adapter) {
            this.mAdapterChangeListener.onAdapterChanged(var4, adapter);
        }

    }

    private void removeNonDecorViews() {
        for (int i = 0; i < this.getChildCount(); ++i) {
            View child = this.getChildAt(i);
            CustomViewpager.LayoutParams lp = (CustomViewpager.LayoutParams) child.getLayoutParams();
            if (!lp.isDecor) {
                this.removeViewAt(i);
                --i;
            }
        }

    }

    public CustomPagerAdapter getAdapter() {
        return this.mAdapter;
    }

    void setOnAdapterChangeListener(CustomViewpager.OnAdapterChangeListener listener) {
        this.mAdapterChangeListener = listener;
    }

    private int getClientWidth() {
        return this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight();
    }

    public void setCurrentItem(int item) {
        this.mPopulatePending = false;
        this.setCurrentItemInternal(item, !this.mFirstLayout, false);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        this.mPopulatePending = false;
        this.setCurrentItemInternal(item, smoothScroll, false);
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always) {
        this.setCurrentItemInternal(item, smoothScroll, always, 0);
    }

    void setCurrentItemInternal(int item, boolean smoothScroll, boolean always, int velocity) {
        if (this.mAdapter != null && this.mAdapter.getCount() > 0) {
            if (!always && this.mCurItem == item && this.mItems.size() != 0) {
                this.setScrollingCacheEnabled(false);
            } else {
                this.mMenuDelegate.setSettleState();
                if (item < 0) {
                    item = 0;
                } else if (item >= this.mAdapter.getCount()) {
                    item = this.mAdapter.getCount() - 1;
                }

                int pageLimit = this.mOffscreenPageLimit;
                if (item > this.mCurItem + pageLimit || item < this.mCurItem - pageLimit) {
                    for (int dispatchSelected = 0; dispatchSelected < this.mItems.size(); ++dispatchSelected) {
                        ((CustomViewpager.ItemInfo) this.mItems.get(dispatchSelected)).scrolling = true;
                    }
                }

                boolean var7 = this.mCurItem != item;
                if (this.mFirstLayout) {
                    this.mCurItem = item;
                    this.mMenuDelegate.onPageMenuSelected(this.mCurItem);
                    if (var7 && this.mOnPageChangeListener != null) {
                        this.mOnPageChangeListener.onPageSelected(item);
                    }

                    if (var7 && this.mInternalPageChangeListener != null) {
                        this.mInternalPageChangeListener.onPageSelected(item);
                    }

                    this.requestLayout();
                } else {
                    this.populate(item);
                    this.scrollToItem(item, smoothScroll, velocity, var7);
                }

            }
        } else {
            this.setScrollingCacheEnabled(false);
        }
    }

    private void scrollToItem(int item, boolean smoothScroll, int velocity, boolean dispatchSelected) {
        CustomViewpager.ItemInfo curInfo = this.infoForPosition(item);
        int destX = 0;
        if (curInfo != null) {
            int width = this.getClientWidth();
            destX = (int) ((float) width * Math.max(this.mFirstOffset, Math.min(curInfo.offset, this.mLastOffset)));
        }

        if (smoothScroll) {
            this.smoothScrollTo(destX, 0, velocity);
            if (dispatchSelected && this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageSelected(item);
            }

            if (dispatchSelected && this.mInternalPageChangeListener != null) {
                this.mInternalPageChangeListener.onPageSelected(item);
            }
        } else {
            if (dispatchSelected && this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageSelected(item);
            }

            if (dispatchSelected && this.mInternalPageChangeListener != null) {
                this.mInternalPageChangeListener.onPageSelected(item);
            }

            this.completeScroll(false);
            this.scrollTo(destX, 0);
            this.pageScrolled(destX);
        }

    }

    public void setOnPageChangeListener(CustomViewpager.OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    public void setPageTransformer(boolean reverseDrawingOrder, CustomViewpager.PageTransformer transformer) {
        if (Build.VERSION.SDK_INT >= 11) {
            boolean hasTransformer = transformer != null;
            boolean needsPopulate = hasTransformer != (this.mPageTransformer != null);
            this.mPageTransformer = transformer;
            this.setChildrenDrawingOrderEnabled(hasTransformer);
            if (hasTransformer) {
                this.mDrawingOrder = reverseDrawingOrder ? 2 : 1;
            } else {
                this.mDrawingOrder = 0;
            }

            if (needsPopulate) {
                this.populate();
            }
        }

    }

    protected void setChildrenDrawingOrderEnabled(boolean enable) {
        if (Build.VERSION.SDK_INT >= 7) {
            if (this.mSetChildrenDrawingOrderEnabled == null) {
                try {
                    this.mSetChildrenDrawingOrderEnabled = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", new Class[]{Boolean.TYPE});
                } catch (NoSuchMethodException var4) {
                    Log.e("CustomViewpager", "Can\'t find setChildrenDrawingOrderEnabled", var4);
                }
            }

            try {
                this.mSetChildrenDrawingOrderEnabled.invoke(this, new Object[]{Boolean.valueOf(enable)});
            } catch (Exception var3) {
                Log.e("CustomViewpager", "Error changing children drawing order", var3);
            }
        }

    }

    protected int getChildDrawingOrder(int childCount, int i) {
        int index = this.mDrawingOrder == 2 ? childCount - 1 - i : i;
        int result = ((CustomViewpager.LayoutParams) ((View) this.mDrawingOrderedChildren.get(index)).getLayoutParams()).childIndex;
        return result;
    }

    CustomViewpager.OnPageChangeListener setInternalPageChangeListener(CustomViewpager.OnPageChangeListener listener) {
        CustomViewpager.OnPageChangeListener oldListener = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = listener;
        return oldListener;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public void setOffscreenPageLimit(int limit) {
        if (limit < 1) {
            Log.w("CustomViewpager", "Requested offscreen page limit " + limit + " too small; defaulting to " + 1);
            limit = 1;
        }

        if (limit != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = limit;
            this.populate();
        }

    }

    public void setPageMargin(int marginPixels) {
        int oldMargin = this.mPageMargin;
        this.mPageMargin = marginPixels;
        int width = this.getWidth();
        this.recomputeScrollPosition(width, width, marginPixels, oldMargin);
        this.requestLayout();
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    public void setPageMarginDrawable(Drawable d) {
        this.mMarginDrawable = d;
        if (d != null) {
            this.refreshDrawableState();
        }

        this.setWillNotDraw(d == null);
        this.invalidate();
    }

    public void setPageMarginDrawable(int resId) {
        this.setPageMarginDrawable(this.getContext().getResources().getDrawable(resId));
    }

    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.mMarginDrawable;
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable d = this.mMarginDrawable;
        if (d != null && d.isStateful()) {
            d.setState(this.getDrawableState());
        }

    }

    float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5F;
        f = (float) ((double) f * 0.4712389167638204D);
        return (float) Math.sin((double) f);
    }

    void smoothScrollTo(int x, int y) {
        this.smoothScrollTo(x, y, 0);
    }

    void smoothScrollTo(int x, int y, int velocity) {
        if (this.getChildCount() == 0) {
            this.setScrollingCacheEnabled(false);
        } else {
            int sx = this.getScrollX();
            int sy = this.getScrollY();
            int dx = x - sx;
            int dy = y - sy;
            if (dx == 0 && dy == 0) {
                this.completeScroll(false);
                this.populate();
                this.setScrollState(0);
            } else {
                this.setScrollingCacheEnabled(true);
                this.setScrollState(2);
                int width = this.getClientWidth();
                int halfWidth = width / 2;
                float distanceRatio = Math.min(1.0F, 1.0F * (float) Math.abs(dx) / (float) width);
                float distance = (float) halfWidth + (float) halfWidth * this.distanceInfluenceForSnapDuration(distanceRatio);
                boolean duration = false;
                velocity = Math.abs(velocity);
                int duration1;
                if (velocity > 0) {
                    duration1 = 4 * Math.round(1000.0F * Math.abs(distance / (float) velocity));
                } else {
                    float pageWidth = (float) width * this.mAdapter.getPageWidth(this.mCurItem);
                    float pageDelta = (float) Math.abs(dx) / (pageWidth + (float) this.mPageMargin);
                    duration1 = (int) ((pageDelta + 1.0F) * 100.0F);
                }

                duration1 = (int) ((float) Math.min(duration1, 600) * 1.0F);
                this.mScroller.startScroll(sx, sy, dx, dy, duration1);
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }
    }

    CustomViewpager.ItemInfo addNewItem(int position, int index) {
        CustomViewpager.ItemInfo ii = new CustomViewpager.ItemInfo();
        ii.position = position;
        ii.object = this.mAdapter.instantiateItem(this, position);
        ii.widthFactor = this.mAdapter.getPageWidth(position);
        if (index >= 0 && index < this.mItems.size()) {
            this.mItems.add(index, ii);
        } else {
            this.mItems.add(ii);
        }

        return ii;
    }

    void dataSetChanged() {
        int adapterCount = this.mAdapter.getCount();
        this.mExpectedAdapterCount = adapterCount;
        boolean needPopulate = this.mItems.size() < this.mOffscreenPageLimit * 2 + 1 && this.mItems.size() < adapterCount;
        int newCurrItem = this.mCurItem;
        boolean isUpdating = false;

        int childCount;
        for (childCount = 0; childCount < this.mItems.size(); ++childCount) {
            CustomViewpager.ItemInfo i = (CustomViewpager.ItemInfo) this.mItems.get(childCount);
            int child = this.mAdapter.getItemPosition(i.object);
            if (child != -1) {
                if (child == -2) {
                    this.mItems.remove(childCount);
                    --childCount;
                    if (!isUpdating) {
                        this.mAdapter.startUpdate(this);
                        isUpdating = true;
                    }

                    this.mAdapter.destroyItem(this, i.position, i.object);
                    needPopulate = true;
                    if (this.mCurItem == i.position) {
                        newCurrItem = Math.max(0, Math.min(this.mCurItem, adapterCount - 1));
                        needPopulate = true;
                    }
                } else if (i.position != child) {
                    if (i.position == this.mCurItem) {
                        newCurrItem = child;
                    }

                    i.position = child;
                    needPopulate = true;
                }
            }
        }

        if (isUpdating) {
            this.mAdapter.finishUpdate(this);
        }

        Collections.sort(this.mItems, COMPARATOR);
        if (needPopulate) {
            childCount = this.getChildCount();

            for (int var9 = 0; var9 < childCount; ++var9) {
                View var10 = this.getChildAt(var9);
                CustomViewpager.LayoutParams lp = (CustomViewpager.LayoutParams) var10.getLayoutParams();
                if (!lp.isDecor) {
                    lp.widthFactor = 0.0F;
                }
            }

            this.setCurrentItemInternal(newCurrItem, false, true);
            this.requestLayout();
        }

    }

    void populate() {
        this.populate(this.mCurItem);
    }

    void populate(int newCurrentItem) {
        CustomViewpager.ItemInfo oldCurInfo = null;
        int focusDirection = 2;
        if (this.mCurItem != newCurrentItem) {
            focusDirection = this.mCurItem < newCurrentItem ? 66 : 17;
            oldCurInfo = this.infoForPosition(this.mCurItem);
            this.mMenuDelegate.updateDirection(newCurrentItem > this.mCurItem);
            this.mMenuDelegate.onPageMenuSelected(newCurrentItem);
            this.mCurItem = newCurrentItem;
        }

        if (this.mAdapter == null) {
            this.sortChildDrawingOrder();
        } else if (this.mPopulatePending) {
            this.sortChildDrawingOrder();
        } else if (this.getWindowToken() != null) {
            this.mAdapter.startUpdate(this);
            int pageLimit = this.mOffscreenPageLimit;
            int startPos = Math.max(0, this.mCurItem - pageLimit);
            int N = this.mAdapter.getCount();
            int endPos = Math.min(N - 1, this.mCurItem + pageLimit);
            if (N != this.mExpectedAdapterCount) {
                String var20;
                try {
                    var20 = this.getResources().getResourceName(this.getId());
                } catch (Resources.NotFoundException var18) {
                    var20 = Integer.toHexString(this.getId());
                }

                throw new IllegalStateException("The application\'s ColorPagerAdapter changed the adapter\'s contents without calling ColorPagerAdapter#notifyDataSetChanged! Expected adapter item count: " + this.mExpectedAdapterCount + ", found: " + N + " Pager id: " + var20 + " Pager class: " + this.getClass() + " Problematic adapter: " + this.mAdapter.getClass());
            } else {
                boolean curIndex = true;
                CustomViewpager.ItemInfo curItem = null;

                int var19;
                for (var19 = 0; var19 < this.mItems.size(); ++var19) {
                    CustomViewpager.ItemInfo childCount = (CustomViewpager.ItemInfo) this.mItems.get(var19);
                    if (childCount.position >= this.mCurItem) {
                        if (childCount.position == this.mCurItem) {
                            curItem = childCount;
                        }
                        break;
                    }
                }

                if (curItem == null && N > 0) {
                    curItem = this.addNewItem(this.mCurItem, var19);
                }

                int currentFocused;
                CustomViewpager.ItemInfo ii;
                int i;
                if (curItem != null) {
                    float var21 = 0.0F;
                    currentFocused = var19 - 1;
                    ii = currentFocused >= 0 ? (CustomViewpager.ItemInfo) this.mItems.get(currentFocused) : null;
                    i = this.getClientWidth();
                    float child = i <= 0 ? 0.0F : 2.0F - curItem.widthFactor + (float) this.getPaddingLeft() / (float) i;

                    for (int extraWidthRight = this.mCurItem - 1; extraWidthRight >= 0; --extraWidthRight) {
                        if (var21 >= child && extraWidthRight < startPos) {
                            if (ii == null) {
                                break;
                            }

                            if (extraWidthRight == ii.position && !ii.scrolling) {
                                this.mItems.remove(currentFocused);
                                this.mAdapter.destroyItem(this, extraWidthRight, ii.object);
                                --currentFocused;
                                --var19;
                                ii = currentFocused >= 0 ? (CustomViewpager.ItemInfo) this.mItems.get(currentFocused) : null;
                            }
                        } else if (ii != null && extraWidthRight == ii.position) {
                            var21 += ii.widthFactor;
                            --currentFocused;
                            ii = currentFocused >= 0 ? (CustomViewpager.ItemInfo) this.mItems.get(currentFocused) : null;
                        } else {
                            ii = this.addNewItem(extraWidthRight, currentFocused + 1);
                            var21 += ii.widthFactor;
                            ++var19;
                            ii = currentFocused >= 0 ? (CustomViewpager.ItemInfo) this.mItems.get(currentFocused) : null;
                        }
                    }

                    float var28 = curItem.widthFactor;
                    currentFocused = var19 + 1;
                    if (var28 < 2.0F) {
                        ii = currentFocused < this.mItems.size() ? (CustomViewpager.ItemInfo) this.mItems.get(currentFocused) : null;
                        float rightWidthNeeded = i <= 0 ? 0.0F : (float) this.getPaddingRight() / (float) i + 2.0F;

                        for (int pos = this.mCurItem + 1; pos < N; ++pos) {
                            if (var28 >= rightWidthNeeded && pos > endPos) {
                                if (ii == null) {
                                    break;
                                }

                                if (pos == ii.position && !ii.scrolling) {
                                    this.mItems.remove(currentFocused);
                                    this.mAdapter.destroyItem(this, pos, ii.object);
                                    ii = currentFocused < this.mItems.size() ? (CustomViewpager.ItemInfo) this.mItems.get(currentFocused) : null;
                                }
                            } else if (ii != null && pos == ii.position) {
                                var28 += ii.widthFactor;
                                ++currentFocused;
                                ii = currentFocused < this.mItems.size() ? (CustomViewpager.ItemInfo) this.mItems.get(currentFocused) : null;
                            } else {
                                ii = this.addNewItem(pos, currentFocused);
                                ++currentFocused;
                                var28 += ii.widthFactor;
                                ii = currentFocused < this.mItems.size() ? (CustomViewpager.ItemInfo) this.mItems.get(currentFocused) : null;
                            }
                        }
                    }

                    this.calculatePageOffsets(curItem, var19, oldCurInfo);
                }

                this.mAdapter.setPrimaryItem(this, this.mCurItem, curItem != null ? curItem.object : null);
                this.mAdapter.finishUpdate(this);
                int var22 = this.getChildCount();

                for (currentFocused = 0; currentFocused < var22; ++currentFocused) {
                    View var27 = this.getChildAt(currentFocused);
                    CustomViewpager.LayoutParams var23 = (CustomViewpager.LayoutParams) var27.getLayoutParams();
                    var23.childIndex = currentFocused;
                    if (!var23.isDecor && var23.widthFactor == 0.0F) {
                        CustomViewpager.ItemInfo var25 = this.infoForChild(var27);
                        if (var25 != null) {
                            var23.widthFactor = var25.widthFactor;
                            var23.position = var25.position;
                        }
                    }
                }

                this.sortChildDrawingOrder();
                if (this.hasFocus()) {
                    View var24 = this.findFocus();
                    ii = var24 != null ? this.infoForAnyChild(var24) : null;
                    if (ii == null || ii.position != this.mCurItem) {
                        for (i = 0; i < this.getChildCount(); ++i) {
                            View var26 = this.getChildAt(i);
                            ii = this.infoForChild(var26);
                            if (ii != null && ii.position == this.mCurItem && var26.requestFocus(focusDirection)) {
                                break;
                            }
                        }
                    }
                }

            }
        }
    }

    private void sortChildDrawingOrder() {
        if (this.mDrawingOrder != 0) {
            if (this.mDrawingOrderedChildren == null) {
                this.mDrawingOrderedChildren = new ArrayList();
            } else {
                this.mDrawingOrderedChildren.clear();
            }

            int childCount = this.getChildCount();

            for (int i = 0; i < childCount; ++i) {
                View child = this.getChildAt(i);
                this.mDrawingOrderedChildren.add(child);
            }

            Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
        }

    }

    private void calculatePageOffsets(CustomViewpager.ItemInfo curItem, int curIndex, CustomViewpager.ItemInfo oldCurInfo) {
        int N = this.mAdapter.getCount();
        int width = this.getClientWidth();
        float marginOffset = width > 0 ? (float) this.mPageMargin / (float) width : 0.0F;
        int itemCount;
        if (oldCurInfo != null) {
            itemCount = oldCurInfo.position;
            int offset;
            CustomViewpager.ItemInfo pos;
            float i;
            int ii;
            if (itemCount < curItem.position) {
                offset = 0;
                pos = null;
                i = oldCurInfo.offset + oldCurInfo.widthFactor + marginOffset;

                for (ii = itemCount + 1; ii <= curItem.position && offset < this.mItems.size(); ++ii) {
                    for (pos = (CustomViewpager.ItemInfo) this.mItems.get(offset); ii > pos.position && offset < this.mItems.size() - 1; pos = (CustomViewpager.ItemInfo) this.mItems.get(offset)) {
                        ++offset;
                    }

                    while (ii < pos.position) {
                        i += this.mAdapter.getPageWidth(ii) + marginOffset;
                        ++ii;
                    }

                    pos.offset = i;
                    i += pos.widthFactor + marginOffset;
                }
            } else if (itemCount > curItem.position) {
                offset = this.mItems.size() - 1;
                pos = null;
                i = oldCurInfo.offset;

                for (ii = itemCount - 1; ii >= curItem.position && offset >= 0; --ii) {
                    for (pos = (CustomViewpager.ItemInfo) this.mItems.get(offset); ii < pos.position && offset > 0; pos = (CustomViewpager.ItemInfo) this.mItems.get(offset)) {
                        --offset;
                    }

                    while (ii > pos.position) {
                        i -= this.mAdapter.getPageWidth(ii) + marginOffset;
                        --ii;
                    }

                    i -= pos.widthFactor + marginOffset;
                    pos.offset = i;
                }
            }
        }

        itemCount = this.mItems.size();
        float var12 = curItem.offset;
        int var13 = curItem.position - 1;
        this.mFirstOffset = curItem.position == 0 ? curItem.offset : -3.4028235E38F;
        this.mLastOffset = curItem.position == N - 1 ? curItem.offset + curItem.widthFactor - 1.0F : 3.4028235E38F;

        int var14;
        CustomViewpager.ItemInfo var15;
        for (var14 = curIndex - 1; var14 >= 0; --var13) {
            for (var15 = (CustomViewpager.ItemInfo) this.mItems.get(var14); var13 > var15.position; var12 -= this.mAdapter.getPageWidth(var13--) + marginOffset) {
                ;
            }

            var12 -= var15.widthFactor + marginOffset;
            var15.offset = var12;
            if (var15.position == 0) {
                this.mFirstOffset = var12;
            }

            --var14;
        }

        var12 = curItem.offset + curItem.widthFactor + marginOffset;
        var13 = curItem.position + 1;

        for (var14 = curIndex + 1; var14 < itemCount; ++var13) {
            for (var15 = (CustomViewpager.ItemInfo) this.mItems.get(var14); var13 < var15.position; var12 += this.mAdapter.getPageWidth(var13++) + marginOffset) {
                ;
            }

            if (var15.position == N - 1) {
                this.mLastOffset = var12 + var15.widthFactor - 1.0F;
            }

            var15.offset = var12;
            var12 += var15.widthFactor + marginOffset;
            ++var14;
        }

        this.mNeedCalculatePageOffsets = false;
    }

    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        CustomViewpager.SavedState ss = new CustomViewpager.SavedState(superState);
        ss.position = this.mCurItem;
        if (this.mAdapter != null) {
            ss.adapterState = this.mAdapter.saveState();
        }

        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof CustomViewpager.SavedState)) {
            super.onRestoreInstanceState(state);
        } else {
            CustomViewpager.SavedState ss = (CustomViewpager.SavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            if (this.mAdapter != null) {
                this.mAdapter.restoreState(ss.adapterState, ss.loader);
                this.setCurrentItemInternal(ss.position, false, true);
            } else {
                this.mRestoredCurItem = ss.position;
                this.mRestoredAdapterState = ss.adapterState;
                this.mRestoredClassLoader = ss.loader;
            }

        }
    }

    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        if (!this.checkLayoutParams(params)) {
            params = this.generateLayoutParams(params);
        }

        CustomViewpager.LayoutParams lp = (CustomViewpager.LayoutParams) params;
        lp.isDecor |= child instanceof CustomViewpager.Decor;
        if (this.mInLayout) {
            if (lp != null && lp.isDecor) {
                throw new IllegalStateException("Cannot add pager decor view during layout");
            }

            lp.needsMeasure = true;
            this.addViewInLayout(child, index, params);
        } else {
            super.addView(child, index, params);
        }

    }

    public void removeView(View view) {
        if (this.mInLayout) {
            this.removeViewInLayout(view);
        } else {
            super.removeView(view);
        }

    }

    CustomViewpager.ItemInfo infoForChild(View child) {
        for (int i = 0; i < this.mItems.size(); ++i) {
            CustomViewpager.ItemInfo ii = (CustomViewpager.ItemInfo) this.mItems.get(i);
            if (this.mAdapter.isViewFromObject(child, ii.object)) {
                return ii;
            }
        }

        return null;
    }

    CustomViewpager.ItemInfo infoForAnyChild(View child) {
        while (true) {
            ViewParent parent;
            if ((parent = child.getParent()) != this) {
                if (parent != null && parent instanceof View) {
                    child = (View) parent;
                    continue;
                }

                return null;
            }

            return this.infoForChild(child);
        }
    }

    CustomViewpager.ItemInfo infoForPosition(int position) {
        for (int i = 0; i < this.mItems.size(); ++i) {
            CustomViewpager.ItemInfo ii = (CustomViewpager.ItemInfo) this.mItems.get(i);
            if (ii.position == position) {
                return ii;
            }
        }

        return null;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int measuredWidth = this.getMeasuredWidth();
        int maxGutterSize = measuredWidth / 10;
        this.mGutterSize = Math.min(maxGutterSize, this.mDefaultGutterSize);
        int childWidthSize = measuredWidth - this.getPaddingLeft() - this.getPaddingRight();
        int childHeightSize = this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom();
        int size = this.getChildCount();

        int i;
        View child;
        CustomViewpager.LayoutParams lp;
        int widthSpec;
        for (i = 0; i < size; ++i) {
            child = this.getChildAt(i);
            if (child.getVisibility() != GONE) {
                lp = (CustomViewpager.LayoutParams) child.getLayoutParams();
                if (lp != null && lp.isDecor) {
                    widthSpec = lp.gravity & 7;
                    int vgrav = lp.gravity & 112;
                    int widthMode = MeasureSpec.AT_MOST;
                    int heightMode = MeasureSpec.AT_MOST;
                    boolean consumeVertical = vgrav == 48 || vgrav == 80;
                    boolean consumeHorizontal = widthSpec == 3 || widthSpec == 5;
                    if (consumeVertical) {
                        widthMode = MeasureSpec.EXACTLY;
                    } else if (consumeHorizontal) {
                        heightMode = MeasureSpec.EXACTLY;
                    }

                    int widthSize = childWidthSize;
                    int heightSize = childHeightSize;
                    if (lp.width != -2) {
                        widthMode = MeasureSpec.EXACTLY;
                        if (lp.width != -1) {
                            widthSize = lp.width;
                        }
                    }

                    if (lp.height != -2) {
                        heightMode = MeasureSpec.EXACTLY;
                        if (lp.height != -1) {
                            heightSize = lp.height;
                        }
                    }

                    int widthSpec1 = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
                    int heightSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
                    child.measure(widthSpec1, heightSpec);
                    if (consumeVertical) {
                        childHeightSize -= child.getMeasuredHeight();
                    } else if (consumeHorizontal) {
                        childWidthSize -= child.getMeasuredWidth();
                    }
                }
            }
        }

        this.mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        this.mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);
        this.mInLayout = true;
        this.populate();
        this.mInLayout = false;
        size = this.getChildCount();

        for (i = 0; i < size; ++i) {
            child = this.getChildAt(i);
            if (child.getVisibility() != GONE) {
                lp = (CustomViewpager.LayoutParams) child.getLayoutParams();
                if (lp == null || !lp.isDecor) {
                    widthSpec = MeasureSpec.makeMeasureSpec((int) ((float) childWidthSize * lp.widthFactor), MeasureSpec.EXACTLY);
                    child.measure(widthSpec, this.mChildHeightMeasureSpec);
                }
            }
        }

    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw) {
            this.recomputeScrollPosition(w, oldw, this.mPageMargin, this.mPageMargin);
        }

    }

    private void recomputeScrollPosition(int width, int oldWidth, int margin, int oldMargin) {
        int scrollPos;
        if (oldWidth > 0 && !this.mItems.isEmpty()) {
            int ii1 = width - this.getPaddingLeft() - this.getPaddingRight() + margin;
            int scrollOffset1 = oldWidth - this.getPaddingLeft() - this.getPaddingRight() + oldMargin;
            scrollPos = this.getScrollX();
            float pageOffset = (float) scrollPos / (float) scrollOffset1;
            int newOffsetPixels = (int) (pageOffset * (float) ii1);
            this.scrollTo(newOffsetPixels, this.getScrollY());
            if (!this.mScroller.isFinished()) {
                int newDuration = (int) ((float) (this.mScroller.getDuration() - this.mScroller.timePassed()) * 1.0F);
                CustomViewpager.ItemInfo targetInfo = this.infoForPosition(this.mCurItem);
                this.mScroller.startScroll(newOffsetPixels, 0, (int) (targetInfo.offset * (float) width), 0, newDuration);
            }
        } else {
            CustomViewpager.ItemInfo ii = this.infoForPosition(this.mCurItem);
            float scrollOffset = ii != null ? Math.min(ii.offset, this.mLastOffset) : 0.0F;
            scrollPos = (int) (scrollOffset * (float) (width - this.getPaddingLeft() - this.getPaddingRight()));
            if (scrollPos != this.getScrollX()) {
                this.completeScroll(false);
                this.scrollTo(scrollPos, this.getScrollY());
            }
        }

    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = this.getChildCount();
        int width = r - l;
        int height = b - t;
        int paddingLeft = this.getPaddingLeft();
        int paddingTop = this.getPaddingTop();
        int paddingRight = this.getPaddingRight();
        int paddingBottom = this.getPaddingBottom();
        int scrollX = this.getScrollX();
        int decorCount = 0;

        int childWidth;
        int loff;
        int childLeft;
        for (childWidth = 0; childWidth < count; ++childWidth) {
            View i = this.getChildAt(childWidth);
            if (i.getVisibility() != GONE) {
                CustomViewpager.LayoutParams child = (CustomViewpager.LayoutParams) i.getLayoutParams();
                boolean lp = false;
                boolean ii = false;
                if (child.isDecor) {
                    loff = child.gravity & 7;
                    childLeft = child.gravity & 112;
                    int var27;
                    switch (loff) {
                        case 1:
                            var27 = Math.max((width - i.getMeasuredWidth()) / 2, paddingLeft);
                            break;
                        case 2:
                        case 4:
                        default:
                            var27 = paddingLeft;
                            break;
                        case 3:
                            var27 = paddingLeft;
                            paddingLeft += i.getMeasuredWidth();
                            break;
                        case 5:
                            var27 = width - paddingRight - i.getMeasuredWidth();
                            paddingRight += i.getMeasuredWidth();
                    }

                    int var28;
                    switch (childLeft) {
                        case 16:
                            var28 = Math.max((height - i.getMeasuredHeight()) / 2, paddingTop);
                            break;
                        case 48:
                            var28 = paddingTop;
                            paddingTop += i.getMeasuredHeight();
                            break;
                        case 80:
                            var28 = height - paddingBottom - i.getMeasuredHeight();
                            paddingBottom += i.getMeasuredHeight();
                            break;
                        default:
                            var28 = paddingTop;
                    }

                    var27 += scrollX;
                    i.layout(var27, var28, var27 + i.getMeasuredWidth(), var28 + i.getMeasuredHeight());
                    ++decorCount;
                }
            }
        }

        childWidth = width - paddingLeft - paddingRight;

        for (int var25 = 0; var25 < count; ++var25) {
            View var26 = this.getChildAt(var25);
            if (var26.getVisibility() != GONE) {
                CustomViewpager.LayoutParams var29 = (CustomViewpager.LayoutParams) var26.getLayoutParams();
                CustomViewpager.ItemInfo var30;
                if (!var29.isDecor && (var30 = this.infoForChild(var26)) != null) {
                    loff = (int) ((float) childWidth * var30.offset);
                    childLeft = paddingLeft + loff;
                    if (var29.needsMeasure) {
                        var29.needsMeasure = false;
                        int widthSpec = MeasureSpec.makeMeasureSpec((int) ((float) childWidth * var29.widthFactor), MeasureSpec.EXACTLY);
                        int heightSpec = MeasureSpec.makeMeasureSpec(height - paddingTop - paddingBottom, MeasureSpec.EXACTLY);
                        var26.measure(widthSpec, heightSpec);
                    }

                    var26.layout(childLeft, paddingTop, childLeft + var26.getMeasuredWidth(), paddingTop + var26.getMeasuredHeight());
                }
            }
        }

        this.mTopPageBounds = paddingTop;
        this.mBottomPageBounds = height - paddingBottom;
        this.mDecorChildCount = decorCount;
        if (this.mFirstLayout) {
            this.scrollToItem(this.mCurItem, false, 0, false);
        }

        this.mFirstLayout = false;
    }

    public void computeScroll() {
        if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            int oldX = this.getScrollX();
            int oldY = this.getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if (oldX != x || oldY != y) {
                this.scrollTo(x, y);
                if (!this.pageScrolled(x)) {
                    this.mScroller.abortAnimation();
                    this.scrollTo(0, y);
                }
            }

            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            this.completeScroll(true);
        }
    }

    private boolean pageScrolled(int xpos) {
        if (this.mItems.size() == 0) {
            this.mCalledSuper = false;
            this.onPageScrolled(0, 0.0F, 0);
            if (!this.mCalledSuper) {
                throw new IllegalStateException("onPageScrolled did not call superclass implementation");
            } else {
                return false;
            }
        } else {
            CustomViewpager.ItemInfo ii = this.infoForCurrentScrollPosition();
            int width = this.getClientWidth();
            int widthWithMargin = width + this.mPageMargin;
            float marginOffset = (float) this.mPageMargin / (float) width;
            int currentPage = ii.position;
            float pageOffset = ((float) xpos / (float) width - ii.offset) / (ii.widthFactor + marginOffset);
            int offsetPixels = (int) (pageOffset * (float) widthWithMargin);
            this.mCalledSuper = false;
            this.onPageScrolled(currentPage, pageOffset, offsetPixels);
            this.mMenuDelegate.pageMenuScrolled(currentPage, pageOffset);
            if (!this.mCalledSuper) {
                throw new IllegalStateException("onPageScrolled did not call superclass implementation");
            } else {
                return true;
            }
        }
    }

    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        int scrollX;
        int childCount;
        int i;
        if (this.mDecorChildCount > 0) {
            scrollX = this.getScrollX();
            childCount = this.getPaddingLeft();
            i = this.getPaddingRight();
            int child = this.getWidth();
            int lp = this.getChildCount();

            for (int transformPos = 0; transformPos < lp; ++transformPos) {
                View child1 = this.getChildAt(transformPos);
                CustomViewpager.LayoutParams lp1 = (CustomViewpager.LayoutParams) child1.getLayoutParams();
                if (lp1.isDecor) {
                    int hgrav = lp1.gravity & 7;
                    boolean childLeft = false;
                    int var18;
                    switch (hgrav) {
                        case 1:
                            var18 = Math.max((child - child1.getMeasuredWidth()) / 2, childCount);
                            break;
                        case 2:
                        case 4:
                        default:
                            var18 = childCount;
                            break;
                        case 3:
                            var18 = childCount;
                            childCount += child1.getWidth();
                            break;
                        case 5:
                            var18 = child - i - child1.getMeasuredWidth();
                            i += child1.getMeasuredWidth();
                    }

                    var18 += scrollX;
                    int childOffset = var18 - child1.getLeft();
                    if (childOffset != 0) {
                        child1.offsetLeftAndRight(childOffset);
                    }
                }
            }
        }

        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }

        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }

        if (this.mPageTransformer != null) {
            scrollX = this.getScrollX();
            childCount = this.getChildCount();

            for (i = 0; i < childCount; ++i) {
                View var15 = this.getChildAt(i);
                CustomViewpager.LayoutParams var16 = (CustomViewpager.LayoutParams) var15.getLayoutParams();
                if (!var16.isDecor) {
                    float var17 = (float) (var15.getLeft() - scrollX) / (float) this.getClientWidth();
                    this.mPageTransformer.transformPage(var15, var17);
                }
            }
        }

        this.mCalledSuper = true;
    }

    private void completeScroll(boolean postEvents) {
        boolean needPopulate = this.mScrollState == 2;
        int i;
        if (needPopulate) {
            this.setScrollingCacheEnabled(false);
            this.mScroller.abortAnimation();
            i = this.getScrollX();
            int ii = this.getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if (i != x || ii != y) {
                this.scrollTo(x, y);
            }
        }

        this.mPopulatePending = false;

        for (i = 0; i < this.mItems.size(); ++i) {
            CustomViewpager.ItemInfo var7 = (CustomViewpager.ItemInfo) this.mItems.get(i);
            if (var7.scrolling) {
                needPopulate = true;
                var7.scrolling = false;
            }
        }

        if (needPopulate) {
            if (postEvents) {
                ViewCompat.postOnAnimation(this, this.mEndScrollRunnable);
            } else {
                this.mEndScrollRunnable.run();
            }
        }

    }

    private boolean isGutterDrag(float x, float dx) {
        return x < (float) this.mGutterSize && dx > 0.0F || x > (float) (this.getWidth() - this.mGutterSize) && dx < 0.0F;
    }

    private void enableLayers(boolean enable) {
        int childCount = this.getChildCount();

        for (int i = 0; i < childCount; ++i) {
            int layerType = enable ? 2 : 0;
            this.getChildAt(i).setLayerType(layerType, (Paint) null);
        }

    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            if (this.mDisableTouch) {
                return false;
            } else {
                int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
                if (action != MotionEvent.ACTION_CANCEL && action != MotionEvent.ACTION_UP) {
                    if (action != MotionEvent.ACTION_DOWN) {
                        if (this.mIsBeingDragged) {
                            return true;
                        }

                        if (this.mIsUnableToDrag) {
                            return false;
                        }
                    }

                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            this.mLastMotionX = this.mInitialMotionX = ev.getX();
                            this.mLastMotionY = this.mInitialMotionY = ev.getY();
                            this.mActivePointerId = ev.getPointerId(0);
                            this.mIsUnableToDrag = false;
                            this.mScroller.computeScrollOffset();
                            if (this.mScrollState == 2 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
                                this.mScroller.abortAnimation();
                                this.mPopulatePending = false;
                                this.populate();
                                this.mIsBeingDragged = true;
                                this.requestParentDisallowInterceptTouchEvent(true);
                                this.setScrollState(1);
                            } else {
                                this.completeScroll(false);
                                this.mIsBeingDragged = false;
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            int activePointerId = this.mActivePointerId;
                            if (activePointerId != -1) {
                                int pointerIndex = ev.findPointerIndex(activePointerId);
                                float x = ev.getX(pointerIndex);
                                float dx = x - this.mLastMotionX;
                                float xDiff = Math.abs(dx);
                                float y = ev.getY(pointerIndex);
                                float yDiff = Math.abs(y - this.mInitialMotionY);
                                if (dx != 0.0F && !this.isGutterDrag(this.mLastMotionX, dx) && this.canScroll(this, false, (int) dx, (int) x, (int) y)) {
                                    this.mLastMotionX = x;
                                    this.mLastMotionY = y;
                                    this.mIsUnableToDrag = true;
                                    return false;
                                }


                                if (xDiff > (float) this.mTouchSlop && xDiff * 0.5F > yDiff && !mIsUnableToDrag) {
                                    this.mIsBeingDragged = true;
                                    this.requestParentDisallowInterceptTouchEvent(true);
                                    this.setScrollState(1);
                                    this.mLastMotionX = dx > 0.0F ? this.mInitialMotionX + (float) this.mTouchSlop : this.mInitialMotionX - (float) this.mTouchSlop;
                                    this.mLastMotionY = y;
                                    this.setScrollingCacheEnabled(true);
                                } else if (yDiff > (float) this.mTouchSlop) {
//                                    scrollTo(0, (int) (this.mInitialMotionY - y));
                                    this.mIsUnableToDrag = true;
                                }

                                if (this.mIsBeingDragged && this.performDrag(x)) {
                                    ViewCompat.postInvalidateOnAnimation(this);
                                }
                            }
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            this.onSecondaryPointerUp(ev);
                    }

                    if (this.mVelocityTracker == null) {
                        this.mVelocityTracker = VelocityTracker.obtain();
                    }

                    this.mVelocityTracker.addMovement(ev);
//                    return this.mIsBeingDragged;
                    return mOnTouchSlopListener == null ? mIsBeingDragged : this.mIsUnableToDrag;
                } else {
                    this.mIsBeingDragged = false;
                    this.mIsUnableToDrag = false;
                    this.mActivePointerId = -1;
                    if (this.mVelocityTracker != null) {
                        this.mVelocityTracker.recycle();
                        this.mVelocityTracker = null;
                    }

                    return false;
                }
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mDisableTouch) {
            return false;
        } else if (this.mFakeDragging) {
            return true;
        } else if (ev.getAction() == MotionEvent.ACTION_DOWN && ev.getEdgeFlags() != 0) {
            return false;
        } else if (this.mAdapter != null && this.mAdapter.getCount() != 0) {
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }

            this.mVelocityTracker.addMovement(ev);
            int action = ev.getAction();
            boolean needsInvalidate = false;
            int index;
            float x;
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    this.mScroller.abortAnimation();
                    this.mPopulatePending = false;
                    this.populate();
                    this.mLastMotionX = this.mInitialMotionX = ev.getX();
                    this.mLastMotionY = this.mInitialMotionY = ev.getY();
                    this.mActivePointerId = ev.getPointerId(0);
                    break;
                case MotionEvent.ACTION_UP:
                    if (this.mIsBeingDragged) {
                        VelocityTracker index1 = this.mVelocityTracker;
                        index1.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                        int x2 = (int) index1.getXVelocity(this.mActivePointerId);
                        this.mPopulatePending = true;
                        int width1 = this.getClientWidth();
                        int scrollX1 = this.getScrollX();
                        CustomViewpager.ItemInfo ii1 = this.infoForCurrentScrollPosition();
                        int currentPage1 = ii1.position;
                        float pageOffset = ((float) scrollX1 / (float) width1 - ii1.offset) / ii1.widthFactor;
                        int activePointerIndex = ev.findPointerIndex(this.mActivePointerId);
                        float x1 = ev.getX(activePointerIndex);
                        int totalDelta = (int) (x1 - this.mInitialMotionX);
                        int nextPage = this.determineTargetPage(currentPage1, pageOffset, x2, totalDelta);
                        this.setCurrentItemInternal(nextPage, true, true, x2);
                        this.mActivePointerId = -1;
                        this.endDrag();
                        this.mLeftEdge.onRelease();
                        this.mRightEdge.onRelease();
                        needsInvalidate = !this.mLeftEdge.isFinished() || !this.mRightEdge.isFinished();
                    }
                    if (mOnTouchSlopListener != null) {
                        View view = getCutrrentChildView();
                        if (view != null && view.getTranslationY() != 0) {
                            if (Math.abs(view.getTranslationY()) < validYSliding) {
                                AnimatorSet set = new AnimatorSet();
                                set.playTogether(
                                        ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), 0)
                                );
                                set.setDuration(150).start();
                                mOnTouchSlopListener.onTouch(0);
                            } else {
                                AnimatorSet set = new AnimatorSet();
                                set.playTogether(
                                        ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), view.getTranslationY() > 0 ? AppConfig.PhoneInfo.screenheight : -AppConfig.PhoneInfo.screenheight)
                                );
                                set.setDuration(300)
                                        .addListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                mOnTouchSlopListener.onSlopExit();
                                            }
                                        });
                                set.start();
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    float translationY = 0;
                    if (this.mIsUnableToDrag && mOnTouchSlopListener != null) {
                        View view = getCutrrentChildView();
                        if (view != null) {
                            float yDiff = ev.getY() - this.mInitialMotionY;
                            ViewHelper.setTranslationY(view, yDiff);
                            translationY = view.getTranslationY();
                            mOnTouchSlopListener.onTouch(Math.abs(yDiff));
                        }
                    }
                    if (!this.mIsBeingDragged) {
                        index = ev.findPointerIndex(this.mActivePointerId);
                        x = ev.getX(index);
                        float width = Math.abs(x - this.mLastMotionX);
                        float scrollX = ev.getY(index);
                        float ii = Math.abs(scrollX - this.mLastMotionY);
                        if (width > (float) this.mTouchSlop && width > ii) {
                            this.mIsBeingDragged = true;
                            this.requestParentDisallowInterceptTouchEvent(true);
                            this.mLastMotionX = x - this.mInitialMotionX > 0.0F ? this.mInitialMotionX + (float) this.mTouchSlop : this.mInitialMotionX - (float) this.mTouchSlop;
                            this.mLastMotionY = scrollX;
                            this.setScrollState(1);
                            this.setScrollingCacheEnabled(true);
                            ViewParent currentPage = this.getParent();
                            if (currentPage != null) {
                                currentPage.requestDisallowInterceptTouchEvent(true);
                            }
                        }
                    }

                    if (this.mIsBeingDragged) {
                        index = ev.findPointerIndex(this.mActivePointerId);
                        x = ev.getX(index);
                        needsInvalidate |= this.performDrag(x);
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    if (this.mIsBeingDragged) {
                        this.scrollToItem(this.mCurItem, true, 0, false);
                        this.mActivePointerId = -1;
                        this.endDrag();
                        this.mLeftEdge.onRelease();
                        this.mRightEdge.onRelease();
                        needsInvalidate = !this.mLeftEdge.isFinished() || !this.mRightEdge.isFinished();
                    }
                case 4:
                default:
                    break;
                case MotionEventCompat.ACTION_POINTER_DOWN:
                    index = ev.getActionIndex();
                    x = ev.getX(index);
                    this.mLastMotionX = x;
                    this.mActivePointerId = ev.getPointerId(index);
                    break;
                case MotionEventCompat.ACTION_POINTER_UP:
                    this.onSecondaryPointerUp(ev);
                    this.mLastMotionX = ev.getX(ev.findPointerIndex(this.mActivePointerId));
            }

            if (needsInvalidate) {
                ViewCompat.postInvalidateOnAnimation(this);
            }

            return true;
        } else {
            return false;
        }
    }

    private View getCutrrentChildView() {
        int var19;
        for (var19 = 0; var19 < this.mItems.size(); ++var19) {
            CustomViewpager.ItemInfo childCount = this.mItems.get(var19);
            if (childCount.position >= this.mCurItem) {
                if (childCount.position == this.mCurItem) {
                    return (View) childCount.object;
                }
            }
        }
        return null;
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        ViewParent parent = this.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }

    }

    private boolean performDrag(float x) {
        boolean needsInvalidate = false;
        float deltaX = this.mLastMotionX - x;
        this.mLastMotionX = x;
        float oldScrollX = (float) this.getScrollX();
        float scrollX = oldScrollX + deltaX;
        int width = this.getClientWidth();
        float leftBound = (float) width * this.mFirstOffset;
        float rightBound = (float) width * this.mLastOffset;
        boolean leftAbsolute = true;
        boolean rightAbsolute = true;
        CustomViewpager.ItemInfo firstItem = (CustomViewpager.ItemInfo) this.mItems.get(0);
        CustomViewpager.ItemInfo lastItem = (CustomViewpager.ItemInfo) this.mItems.get(this.mItems.size() - 1);
        if (firstItem.position != 0) {
            leftAbsolute = false;
            leftBound = firstItem.offset * (float) width;
        }

        if (lastItem.position != this.mAdapter.getCount() - 1) {
            rightAbsolute = false;
            rightBound = lastItem.offset * (float) width;
        }

        float over;
        if (scrollX < leftBound) {
            if (leftAbsolute) {
                over = leftBound - scrollX;
                this.mLeftEdge.onPull(Math.abs(over) / (float) width);
                needsInvalidate = !this.mLeftEdge.isFinished();
            }

            scrollX = leftBound;
        } else if (scrollX > rightBound) {
            if (rightAbsolute) {
                over = scrollX - rightBound;
                this.mRightEdge.onPull(Math.abs(over) / (float) width);
                needsInvalidate = !this.mRightEdge.isFinished();
            }

            scrollX = rightBound;
        }

        this.mLastMotionX += scrollX - (float) ((int) scrollX);
        this.scrollTo((int) scrollX, this.getScrollY());
        this.mMenuDelegate.updateNextItem(deltaX);
        this.pageScrolled((int) scrollX);
        return needsInvalidate;
    }

    CustomViewpager.ItemInfo infoForCurrentScrollPosition() {
        int width = this.getClientWidth();
        float scrollOffset = width > 0 ? (float) this.getScrollX() / (float) width : 0.0F;
        float marginOffset = width > 0 ? (float) this.mPageMargin / (float) width : 0.0F;
        int lastPos = -1;
        float lastOffset = 0.0F;
        float lastWidth = 0.0F;
        boolean first = true;
        CustomViewpager.ItemInfo lastItem = null;

        for (int i = 0; i < this.mItems.size(); ++i) {
            CustomViewpager.ItemInfo ii = (CustomViewpager.ItemInfo) this.mItems.get(i);
            if (!first && ii.position != lastPos + 1) {
                ii = this.mTempItem;
                ii.offset = lastOffset + lastWidth + marginOffset;
                ii.position = lastPos + 1;
                ii.widthFactor = this.mAdapter.getPageWidth(ii.position);
                --i;
            }

            float offset = ii.offset;
            float rightBound = offset + ii.widthFactor + marginOffset;
            if (!first && scrollOffset < offset) {
                return lastItem;
            }

            if (scrollOffset < rightBound || i == this.mItems.size() - 1) {
                return ii;
            }

            first = false;
            lastPos = ii.position;
            lastOffset = offset;
            lastWidth = ii.widthFactor;
            lastItem = ii;
        }

        return lastItem;
    }

    private int determineTargetPage(int currentPage, float pageOffset, int velocity, int deltaX) {
        int targetPage;
        if (Math.abs(deltaX) > this.mFlingDistance && Math.abs(velocity) > this.mMinimumVelocity) {
            targetPage = velocity > 0 ? currentPage : currentPage + 1;
        } else {
            float firstItem = currentPage >= this.mCurItem ? 1.0f - minPageOffset : minPageOffset;
            targetPage = (int) ((float) currentPage + pageOffset + firstItem);
        }

        if (this.mItems.size() > 0) {
            CustomViewpager.ItemInfo firstItem1 = (CustomViewpager.ItemInfo) this.mItems.get(0);
            CustomViewpager.ItemInfo lastItem = (CustomViewpager.ItemInfo) this.mItems.get(this.mItems.size() - 1);
            targetPage = Math.max(firstItem1.position, Math.min(targetPage, lastItem.position));
        }

        return targetPage;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        boolean needsInvalidate = false;
        int overScrollMode = this.getOverScrollMode();
        if (overScrollMode != 0 && (overScrollMode != 1 || this.mAdapter == null || this.mAdapter.getCount() <= 1)) {
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
        } else {
            int restoreCount;
            int width;
            int height;
            if (!this.mLeftEdge.isFinished()) {
                restoreCount = canvas.save();
                width = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                height = this.getWidth();
                canvas.rotate(270.0F);
                canvas.translate((float) (-width + this.getPaddingTop()), this.mFirstOffset * (float) height);
                this.mLeftEdge.setSize(width, height);
                needsInvalidate |= this.mLeftEdge.draw(canvas);
                canvas.restoreToCount(restoreCount);
            }

            if (!this.mRightEdge.isFinished()) {
                restoreCount = canvas.save();
                width = this.getWidth();
                height = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                canvas.rotate(90.0F);
                canvas.translate((float) (-this.getPaddingTop()), -(this.mLastOffset + 1.0F) * (float) width);
                this.mRightEdge.setSize(height, width);
                needsInvalidate |= this.mRightEdge.draw(canvas);
                canvas.restoreToCount(restoreCount);
            }
        }

        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }

    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
            int scrollX = this.getScrollX();
            int width = this.getWidth();
            float marginOffset = (float) this.mPageMargin / (float) width;
            int itemIndex = 0;
            CustomViewpager.ItemInfo ii = (CustomViewpager.ItemInfo) this.mItems.get(0);
            float offset = ii.offset;
            int itemCount = this.mItems.size();
            int firstPos = ii.position;
            int lastPos = ((CustomViewpager.ItemInfo) this.mItems.get(itemCount - 1)).position;

            for (int pos = firstPos; pos < lastPos; ++pos) {
                while (pos > ii.position && itemIndex < itemCount) {
                    ++itemIndex;
                    ii = (CustomViewpager.ItemInfo) this.mItems.get(itemIndex);
                }

                float drawAt;
                if (pos == ii.position) {
                    drawAt = (ii.offset + ii.widthFactor) * (float) width;
                    offset = ii.offset + ii.widthFactor + marginOffset;
                } else {
                    float widthFactor = this.mAdapter.getPageWidth(pos);
                    drawAt = (offset + widthFactor) * (float) width;
                    offset += widthFactor + marginOffset;
                }

                if (drawAt + (float) this.mPageMargin > (float) scrollX) {
                    this.mMarginDrawable.setBounds((int) drawAt, this.mTopPageBounds, (int) (drawAt + (float) this.mPageMargin + 0.5F), this.mBottomPageBounds);
                    this.mMarginDrawable.draw(canvas);
                }

                if (drawAt > (float) (scrollX + width)) {
                    break;
                }
            }
        }

    }

    public boolean beginFakeDrag() {
        if (this.mIsBeingDragged) {
            return false;
        } else {
            this.mFakeDragging = true;
            this.setScrollState(1);
            this.mInitialMotionX = this.mLastMotionX = 0.0F;
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            } else {
                this.mVelocityTracker.clear();
            }

            long time = SystemClock.uptimeMillis();
            MotionEvent ev = MotionEvent.obtain(time, time, 0, 0.0F, 0.0F, 0);
            this.mVelocityTracker.addMovement(ev);
            ev.recycle();
            this.mFakeDragBeginTime = time;
            return true;
        }
    }

    public void endFakeDrag() {
        if (!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        } else {
            VelocityTracker velocityTracker = this.mVelocityTracker;
            velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
            int initialVelocity = (int) velocityTracker.getXVelocity(this.mActivePointerId);
            this.mPopulatePending = true;
            int width = this.getClientWidth();
            int scrollX = this.getScrollX();
            CustomViewpager.ItemInfo ii = this.infoForCurrentScrollPosition();
            int currentPage = ii.position;
            float pageOffset = ((float) scrollX / (float) width - ii.offset) / ii.widthFactor;
            int totalDelta = (int) (this.mLastMotionX - this.mInitialMotionX);
            int nextPage = this.determineTargetPage(currentPage, pageOffset, initialVelocity, totalDelta);
            this.setCurrentItemInternal(nextPage, true, true, initialVelocity);
            this.endDrag();
            this.mFakeDragging = false;
        }
    }

    public void fakeDragBy(float xOffset) {
        if (!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        } else {
            this.mLastMotionX += xOffset;
            float oldScrollX = (float) this.getScrollX();
            float scrollX = oldScrollX - xOffset;
            int width = this.getClientWidth();
            float leftBound = (float) width * this.mFirstOffset;
            float rightBound = (float) width * this.mLastOffset;
            CustomViewpager.ItemInfo firstItem = (CustomViewpager.ItemInfo) this.mItems.get(0);
            CustomViewpager.ItemInfo lastItem = (CustomViewpager.ItemInfo) this.mItems.get(this.mItems.size() - 1);
            if (firstItem.position != 0) {
                leftBound = firstItem.offset * (float) width;
            }

            if (lastItem.position != this.mAdapter.getCount() - 1) {
                rightBound = lastItem.offset * (float) width;
            }

            if (scrollX < leftBound) {
                scrollX = leftBound;
            } else if (scrollX > rightBound) {
                scrollX = rightBound;
            }

            this.mLastMotionX += scrollX - (float) ((int) scrollX);
            this.scrollTo((int) scrollX, this.getScrollY());
            this.pageScrolled((int) scrollX);
            long time = SystemClock.uptimeMillis();
            MotionEvent ev = MotionEvent.obtain(this.mFakeDragBeginTime, time, 2, this.mLastMotionX, 0.0F, 0);
            this.mVelocityTracker.addMovement(ev);
            ev.recycle();
        }
    }

    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = ev.getActionIndex();
        int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mLastMotionX = ev.getX(newPointerIndex);
            this.mActivePointerId = ev.getPointerId(newPointerIndex);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }

    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }

    }

    private void setScrollingCacheEnabled(boolean enabled) {
        if (this.mScrollingCacheEnabled != enabled) {
            this.mScrollingCacheEnabled = enabled;
        }

    }

    public boolean canScrollHorizontally(int direction) {
        if (this.mAdapter == null) {
            return false;
        } else {
            int width = this.getClientWidth();
            int scrollX = this.getScrollX();
            return direction < 0 ? scrollX > (int) ((float) width * this.mFirstOffset) : (direction > 0 ? scrollX < (int) ((float) width * this.mLastOffset) : false);
        }
    }

    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) v;
            int scrollX = v.getScrollX();
            int scrollY = v.getScrollY();
            int count = group.getChildCount();

            for (int i = count - 1; i >= 0; --i) {
                View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() && y + scrollY >= child.getTop() && y + scrollY < child.getBottom() && this.canScroll(child, true, dx, x + scrollX - child.getLeft(), y + scrollY - child.getTop())) {
                    return true;
                }
            }
        }

        return checkV && v.canScrollHorizontally(-dx);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event) || this.executeKeyEvent(event);
    }

    public boolean executeKeyEvent(KeyEvent event) {
        boolean handled = false;
        if (event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case 21:
                    handled = this.arrowScroll(17);
                    break;
                case 22:
                    handled = this.arrowScroll(66);
                    break;
                case 61:
                    if (Build.VERSION.SDK_INT >= 11) {
                        if (event.hasNoModifiers()) {
                            handled = this.arrowScroll(2);
                        } else if (event.hasModifiers(1)) {
                            handled = this.arrowScroll(1);
                        }
                    }
            }
        }

        return handled;
    }

    public boolean arrowScroll(int direction) {
        View currentFocused = this.findFocus();
        boolean handled;
        if (currentFocused == this) {
            currentFocused = null;
        } else if (currentFocused != null) {
            handled = false;

            for (ViewParent nextFocused = currentFocused.getParent(); nextFocused instanceof ViewGroup; nextFocused = nextFocused.getParent()) {
                if (nextFocused == this) {
                    handled = true;
                    break;
                }
            }

            if (!handled) {
                StringBuilder nextFocused1 = new StringBuilder();
                nextFocused1.append(currentFocused.getClass().getSimpleName());

                for (ViewParent nextLeft = currentFocused.getParent(); nextLeft instanceof ViewGroup; nextLeft = nextLeft.getParent()) {
                    nextFocused1.append(" => ").append(nextLeft.getClass().getSimpleName());
                }

                Log.e("CustomViewpager", "arrowScroll tried to find focus based on non-child current focused view " + nextFocused1.toString());
                currentFocused = null;
            }
        }

        handled = false;
        View nextFocused2 = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
        if (nextFocused2 != null && nextFocused2 != currentFocused) {
            int currLeft;
            int nextLeft1;
            if (direction == 17) {
                nextLeft1 = this.getChildRectInPagerCoordinates(this.mTempRect, nextFocused2).left;
                currLeft = this.getChildRectInPagerCoordinates(this.mTempRect, currentFocused).left;
                if (currentFocused != null && nextLeft1 >= currLeft) {
                    handled = this.pageLeft();
                } else {
                    handled = nextFocused2.requestFocus();
                }
            } else if (direction == 66) {
                nextLeft1 = this.getChildRectInPagerCoordinates(this.mTempRect, nextFocused2).left;
                currLeft = this.getChildRectInPagerCoordinates(this.mTempRect, currentFocused).left;
                if (currentFocused != null && nextLeft1 <= currLeft) {
                    handled = this.pageRight();
                } else {
                    handled = nextFocused2.requestFocus();
                }
            }
        } else if (direction != 17 && direction != 1) {
            if (direction == 66 || direction == 2) {
                handled = this.pageRight();
            }
        } else {
            handled = this.pageLeft();
        }

        if (handled) {
            this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
        }

        return handled;
    }

    private Rect getChildRectInPagerCoordinates(Rect outRect, View child) {
        if (outRect == null) {
            outRect = new Rect();
        }

        if (child == null) {
            outRect.set(0, 0, 0, 0);
            return outRect;
        } else {
            outRect.left = child.getLeft();
            outRect.right = child.getRight();
            outRect.top = child.getTop();
            outRect.bottom = child.getBottom();

            ViewGroup group;
            for (ViewParent parent = child.getParent(); parent instanceof ViewGroup && parent != this; parent = group.getParent()) {
                group = (ViewGroup) parent;
                outRect.left += group.getLeft();
                outRect.right += group.getRight();
                outRect.top += group.getTop();
                outRect.bottom += group.getBottom();
            }

            return outRect;
        }
    }

    boolean pageLeft() {
        if (this.mCurItem > 0) {
            this.setCurrentItem(this.mCurItem - 1, true);
            return true;
        } else {
            return false;
        }
    }

    boolean pageRight() {
        if (this.mAdapter != null && this.mCurItem < this.mAdapter.getCount() - 1) {
            this.setCurrentItem(this.mCurItem + 1, true);
            return true;
        } else {
            return false;
        }
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        int focusableCount = views.size();
        int descendantFocusability = this.getDescendantFocusability();
        if (descendantFocusability != 393216) {
            for (int i = 0; i < this.getChildCount(); ++i) {
                View child = this.getChildAt(i);
                if (child.getVisibility() == VISIBLE) {
                    CustomViewpager.ItemInfo ii = this.infoForChild(child);
                    if (ii != null && ii.position == this.mCurItem) {
                        child.addFocusables(views, direction, focusableMode);
                    }
                }
            }
        }

        if (descendantFocusability != 262144 || focusableCount == views.size()) {
            if (!this.isFocusable()) {
                return;
            }

            if ((focusableMode & 1) == 1 && this.isInTouchMode() && !this.isFocusableInTouchMode()) {
                return;
            }

            if (views != null) {
                views.add(this);
            }
        }

    }

    public void addTouchables(ArrayList<View> views) {
        for (int i = 0; i < this.getChildCount(); ++i) {
            View child = this.getChildAt(i);
            if (child.getVisibility() == VISIBLE) {
                CustomViewpager.ItemInfo ii = this.infoForChild(child);
                if (ii != null && ii.position == this.mCurItem) {
                    child.addTouchables(views);
                }
            }
        }

    }

    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        int count = this.getChildCount();
        int index;
        byte increment;
        int end;
        if ((direction & 2) != 0) {
            index = 0;
            increment = 1;
            end = count;
        } else {
            index = count - 1;
            increment = -1;
            end = -1;
        }

        for (int i = index; i != end; i += increment) {
            View child = this.getChildAt(i);
            if (child.getVisibility() == VISIBLE) {
                CustomViewpager.ItemInfo ii = this.infoForChild(child);
                if (ii != null && ii.position == this.mCurItem && child.requestFocus(direction, previouslyFocusedRect)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == 4096) {
            return super.dispatchPopulateAccessibilityEvent(event);
        } else {
            int childCount = this.getChildCount();

            for (int i = 0; i < childCount; ++i) {
                View child = this.getChildAt(i);
                if (child.getVisibility() == VISIBLE) {
                    CustomViewpager.ItemInfo ii = this.infoForChild(child);
                    if (ii != null && ii.position == this.mCurItem && child.dispatchPopulateAccessibilityEvent(event)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new CustomViewpager.LayoutParams();
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return this.generateDefaultLayoutParams();
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof CustomViewpager.LayoutParams && super.checkLayoutParams(p);
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new CustomViewpager.LayoutParams(this.getContext(), attrs);
    }

    public void setOnPageMenuChangeListener(CustomViewpager.OnPageMenuChangeListener listener) {
        this.mMenuDelegate.setOnPageMenuChangeListener(listener);
    }

    public void setDisableTouchEvent(boolean disable) {
        this.mDisableTouch = disable;
    }

    public void bindSplitMenuCallback(ColorBottomMenuCallback callback) {
        this.mMenuDelegate.bindSplitMenuCallback(callback);
    }

    int getScrollState() {
        return this.mScrollState;
    }

    boolean getDragState() {
        return this.mIsBeingDragged;
    }

    static {
        sInterpolator = ColorBottomMenuCallback.ANIMATOR_INTERPOLATOR;
        sPositionComparator = new CustomViewpager.ViewPositionComparator();
    }

    public interface OnPageMenuChangeListener {
        void onPageMenuScrolled(int var1, float var2);

        void onPageMenuSelected(int var1);

        void onPageMenuScrollStateChanged(int var1);

        void onPageMenuScrollDataChanged();
    }

    static class ViewPositionComparator implements Comparator<View> {
        ViewPositionComparator() {
        }

        public int compare(View lhs, View rhs) {
            CustomViewpager.LayoutParams llp = (CustomViewpager.LayoutParams) lhs.getLayoutParams();
            CustomViewpager.LayoutParams rlp = (CustomViewpager.LayoutParams) rhs.getLayoutParams();
            return llp.isDecor != rlp.isDecor ? (llp.isDecor ? 1 : -1) : llp.position - rlp.position;
        }
    }

    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        public boolean isDecor;
        public int gravity;
        float widthFactor = 0.0F;
        boolean needsMeasure;
        int position;
        int childIndex;

        public LayoutParams() {
            super(-1, -1);
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, CustomViewpager.LAYOUT_ATTRS);
            this.gravity = a.getInteger(0, 48);
            a.recycle();
        }
    }

    private class PagerObserver extends DataSetObserver {


        public void onChanged() {
            CustomViewpager.this.dataSetChanged();
        }

        public void onInvalidated() {
            CustomViewpager.this.dataSetChanged();
        }
    }

    class MyAccessibilityDelegate extends AccessibilityDelegate {
        MyAccessibilityDelegate() {
        }

        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setClassName(CustomViewpager.class.getName());
            AccessibilityRecord record = AccessibilityRecord.obtain();
            record.setScrollable(this.canScroll());
            if (event.getEventType() == 4096 && CustomViewpager.this.mAdapter != null) {
                record.setItemCount(CustomViewpager.this.mAdapter.getCount());
                record.setFromIndex(CustomViewpager.this.mCurItem);
                record.setToIndex(CustomViewpager.this.mCurItem);
            }

        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            info.setClassName(CustomViewpager.class.getName());
            info.setScrollable(this.canScroll());
            if (CustomViewpager.this.canScrollHorizontally(1)) {
                info.addAction(4096);
            }

            if (CustomViewpager.this.canScrollHorizontally(-1)) {
                info.addAction(8192);
            }

        }

        public boolean performAccessibilityAction(View host, int action, Bundle args) {
            if (super.performAccessibilityAction(host, action, args)) {
                return true;
            } else {
                switch (action) {
                    case 4096:
                        if (CustomViewpager.this.canScrollHorizontally(1)) {
                            CustomViewpager.this.setCurrentItem(CustomViewpager.this.mCurItem + 1);
                            return true;
                        }

                        return false;
                    case 8192:
                        if (CustomViewpager.this.canScrollHorizontally(-1)) {
                            CustomViewpager.this.setCurrentItem(CustomViewpager.this.mCurItem - 1);
                            return true;
                        }

                        return false;
                    default:
                        return false;
                }
            }
        }

        private boolean canScroll() {
            return CustomViewpager.this.mAdapter != null && CustomViewpager.this.mAdapter.getCount() > 1;
        }
    }

    public static class SavedState extends View.BaseSavedState {
        int position;
        Parcelable adapterState;
        ClassLoader loader;
        public static final ClassLoaderCreator<CustomViewpager.SavedState> CREATOR = new ClassLoaderCreator() {
            public CustomViewpager.SavedState createFromParcel(Parcel in) {
                return new CustomViewpager.SavedState(in, (ClassLoader) null);
            }

            public CustomViewpager.SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new CustomViewpager.SavedState(in, loader);
            }

            public CustomViewpager.SavedState[] newArray(int size) {
                return new CustomViewpager.SavedState[size];
            }
        };

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.position);
            out.writeParcelable(this.adapterState, flags);
        }

        public String toString() {
            return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.position + "}";
        }

        SavedState(Parcel in, ClassLoader loader) {
            super(in);
            if (loader == null) {
                loader = this.getClass().getClassLoader();
            }

            this.position = in.readInt();
            this.adapterState = in.readParcelable(loader);
            this.loader = loader;
        }
    }

    interface Decor {
    }

    interface OnAdapterChangeListener {
        void onAdapterChanged(CustomPagerAdapter var1, CustomPagerAdapter var2);
    }

    public interface PageTransformer {
        void transformPage(View var1, float var2);
    }

    public static class SimpleOnPageChangeListener implements CustomViewpager.OnPageChangeListener {
        public SimpleOnPageChangeListener() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    public interface OnPageChangeListener {
        void onPageScrolled(int var1, float var2, int var3);

        void onPageSelected(int var1);

        void onPageScrollStateChanged(int var1);
    }

    static class ItemInfo {
        Object object;
        int position;
        boolean scrolling;
        float widthFactor;
        float offset;

        ItemInfo() {
        }
    }

    private CustomViewpager.OnTouchSlopListener mOnTouchSlopListener;

    public void setOnTouchSlopListener(OnTouchSlopListener onTouchSlopListener) {
        mOnTouchSlopListener = onTouchSlopListener;
    }

    public interface OnTouchSlopListener {
        void onTouch(float diff);

        void onSlopExit();
    }
}
