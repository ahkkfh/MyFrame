package cn.mark.utils.empty;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import cn.mark.utils.R;

/**
 * Created by yaoping on 2016/7/7.
 *
 * @author yaoping.luo
 *         该view是用于listview、gridview、recycleView没有数据显示
 */
public class EmptyView extends FrameLayout {
    private static EmptyViewUtils.EmptyViewBuilder mConfig = null;

    private float mTextSize;//text size
    private int mTextColor;//hint text color
    private String mNoDataText;
    private int iconSrc;
    private OnClickListener mOnClickListener;//button click listeren
    private String actionText;//button show text


    private boolean mShowIcon = true;
    private boolean mShowText = true;
    private boolean mShowButton = false;

    private ImageView mImageView;
    private TextView mTextView;
    private Button mButton;

    public static void init(EmptyViewUtils.EmptyViewBuilder defaultConfig) {
        EmptyView.mConfig = defaultConfig;
    }

    public static boolean hasDefaultConfig() {
        return EmptyView.mConfig != null;
    }

    public static EmptyViewUtils.EmptyViewBuilder getConfig() {
        return mConfig;
    }

    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.no_data_view, null);

        mImageView = (ImageView) findViewById(R.id.no_data_img);
        mTextView = (TextView) findViewById(R.id.no_data_text);
        mButton = (Button) findViewById(R.id.no_data_btn);
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
        mTextView.setTextSize(mTextSize);
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
        mTextView.setTextColor(mTextColor);
    }

    public void setNoDataText(String mNoDataText) {
        this.mNoDataText = mNoDataText;
        mTextView.setText(mNoDataText);
    }

    public int getIconSrc() {
        return iconSrc;
    }

    public void setIconSrc(int iconSrc) {
        this.iconSrc = iconSrc;
        mImageView.setImageResource(iconSrc);
    }

    public void setIconSrc(Drawable drawable) {
        this.iconSrc = iconSrc;
        mImageView.setImageDrawable(drawable);
    }

    public OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
        mButton.setOnClickListener(mOnClickListener);
    }


    public void setActionText(String actionText) {
        this.actionText = actionText;
        mButton.setText(actionText);
    }


    public void setShowIcon(boolean mShowIcon) {
        this.mShowIcon = mShowIcon;
        mImageView.setVisibility(mShowIcon ? VISIBLE : GONE);
    }

    public void setShowText(boolean mShowText) {
        this.mShowText = mShowText;
        mTextView.setVisibility(mShowText ? VISIBLE : GONE);
    }

    public void setShowButton(boolean mShowButton) {
        this.mShowButton = mShowButton;
        mButton.setVisibility(mShowButton ? VISIBLE : GONE);
    }
}
