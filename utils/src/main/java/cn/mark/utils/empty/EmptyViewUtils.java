package cn.mark.utils.empty;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by yaoping on 2016/7/7.
 */
public class EmptyViewUtils {

    public static void setEmptyView(AdapterView listView) {
        if (EmptyView.hasDefaultConfig()) {//判断是否配置信息
            EmptyView.getConfig().bindView(listView);
        } else {
            EmptyView view = genSimpleEmptyView(listView);//创建简单的view
            listView.setEmptyView(view);//设置空白view
        }
    }

    @Nullable
    private static EmptyView genSimpleEmptyView(View view) {
        EmptyView emptyView = new EmptyView(view.getContext(), null);//创建空白view
        ViewParent parent = view.getParent();//获取他的父类
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).addView(emptyView);//在父类中添加没有数据的view
            //判断父布局是否为linearLayout、RelativeLayout、FrameLayout，设置参数
            if (parent instanceof LinearLayout) {
                //设置中间显示
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) emptyView.getLayoutParams();
                lp.height = -1;
                lp.gravity = Gravity.CENTER;
                emptyView.setLayoutParams(lp);
            } else if (parent instanceof RelativeLayout) {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) emptyView.getLayoutParams();
                lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);//添加规则
                emptyView.setLayoutParams(lp);
            } else if (parent instanceof FrameLayout) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) emptyView.getLayoutParams();
                lp.height = -1;
                lp.gravity = Gravity.CENTER;
                emptyView.setLayoutParams(lp);
            }
        }
        return emptyView;
    }

    public static final class EmptyViewBuilder {
        private Context context;
        private ViewGroup.LayoutParams layoutParams;
        private String emptyText;
        private int iconSrc;
        private Drawable iconDrawable;
        private int emptyTextColor = -1;
        private int emptyTextSize;
        private ShowType mShowIcon = ShowType.DEFAULT;
        private ShowType mShowText = ShowType.DEFAULT;
        private ShowType mShowButton = ShowType.DEFAULT;
        private View.OnClickListener listener;
        private String mActionText;

        private enum ShowType {
            DEFAULT, SHOW, HIDE
        }

        private EmptyViewBuilder(Context context) {
            this.context = context;
        }

        public static EmptyViewBuilder getInstance(Context context) {
            return new EmptyViewBuilder(context);
        }

        public void bindView(final AdapterView listView) {
            EmptyView emptyView = genSimpleEmptyView(listView);
            removeExistEmptyView(listView);
            listView.setEmptyView(emptyView);
            setEmptyViewStyle(emptyView);
        }

        public void bindView() {

        }

        /*  public void bindView(final RecyclerView recyclerView) {
              final EmptyView emptyView = genSimpleEmptyView(recyclerView);
              final RecyclerView.Adapter adapter = recyclerView.getAdapter();
              if (adapter != null) {
                  RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
                      @Override
                      public void onChanged() {
                          super.onChanged();
                          recyclerView.setVisibility(adapter.getItemCount() > 0 ? View.VISIBLE : View.GONE);
                          emptyView.setVisibility(adapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
                      }
                  };
                  adapter.registerAdapterDataObserver(observer);
                  observer.onChanged();
              } else {
                  throw new RuntimeException("This RecyclerView has no adapter, you must call setAdapter first!");
              }
              setEmptyViewStyle(emptyView);
          }
  */
        private void setEmptyViewStyle(EmptyView emptyView) {
            //设置icon是否显示
            boolean canShowIcon = (mShowIcon == ShowType.SHOW ||
                    (mShowIcon == ShowType.DEFAULT && EmptyView.hasDefaultConfig() && EmptyView.getConfig().mShowIcon == ShowType.SHOW));
            emptyView.setShowIcon(canShowIcon);
            if (canShowIcon) {
                if (iconSrc != 0) {
                    emptyView.setIconSrc(iconSrc);
                } else if (EmptyView.hasDefaultConfig() && EmptyView.getConfig().iconSrc != 0) {
                    emptyView.setIconSrc(EmptyView.getConfig().iconSrc);
                }

                if (iconDrawable != null) {
                    emptyView.setIconSrc(iconDrawable);
                } else if (EmptyView.hasDefaultConfig() && EmptyView.getConfig().iconDrawable != null) {
                    emptyView.setIconSrc(EmptyView.getConfig().iconDrawable);
                }
            }
            //设置text是否显示
            boolean canShowText = (mShowText == ShowType.SHOW ||
                    (mShowText == ShowType.DEFAULT && EmptyView.hasDefaultConfig() && EmptyView.getConfig().mShowText == ShowType.SHOW));
            emptyView.setShowText(canShowText);
            if (canShowIcon) {
                if (emptyTextColor != -1) {
                    emptyView.setTextColor(emptyTextColor);
                } else if (EmptyView.hasDefaultConfig() && EmptyView.getConfig().emptyTextColor != -1) {
                    emptyView.setTextColor(EmptyView.getConfig().emptyTextColor);
                }

                if (emptyTextSize != 0) {
                    emptyView.setTextSize(emptyTextSize);
                } else if (EmptyView.hasDefaultConfig() && EmptyView.getConfig().emptyTextSize != 0) {
                    emptyView.setTextSize(EmptyView.getConfig().emptyTextSize);
                }
                if (!TextUtils.isEmpty(emptyText)) {
                    emptyView.setNoDataText(emptyText);
                } else if (EmptyView.hasDefaultConfig() && !TextUtils.isEmpty(EmptyView.getConfig().emptyText)) {
                    emptyView.setNoDataText(EmptyView.getConfig().emptyText);
                }
            }
            //设置Button是否显示
            boolean canShowButton = (mShowButton == ShowType.SHOW || (mShowButton == ShowType.DEFAULT && EmptyView.hasDefaultConfig() &&
                    EmptyView.getConfig().mShowButton == ShowType.SHOW));
            emptyView.setShowButton(canShowButton);
            if (canShowButton) {
                if (!TextUtils.isEmpty(mActionText)) {
                    emptyView.setActionText(mActionText);
                } else if (EmptyView.hasDefaultConfig() && !TextUtils.isEmpty(EmptyView.getConfig().mActionText)) {
                    emptyView.setActionText(EmptyView.getConfig().mActionText);
                }

                if (listener != null) {
                    emptyView.setOnClickListener(listener);
                } else if (EmptyView.hasDefaultConfig() && EmptyView.getConfig().listener != null) {
                    emptyView.setOnClickListener(EmptyView.getConfig().listener);
                }
            }

            if (layoutParams != null) {
                emptyView.setLayoutParams(layoutParams);
            } else if (EmptyView.hasDefaultConfig() && EmptyView.getConfig().layoutParams != null) {
                emptyView.setLayoutParams(EmptyView.getConfig().layoutParams);
            }
        }

        public EmptyViewBuilder setEmptyText(String emptyText) {
            this.emptyText = emptyText;
            return this;
        }

        public EmptyViewBuilder setEmptyText(int strId) {
            this.emptyText = context.getString(strId);
            return this;
        }

        public EmptyViewBuilder setIconSrc(int iconSrc) {
            this.iconSrc = iconSrc;
            this.iconDrawable = null;
            return this;
        }


        public EmptyViewBuilder setIconDrawable(Drawable iconDrawable) {
            this.iconDrawable = iconDrawable;
            this.iconSrc = 0;
            return this;
        }


        public EmptyViewBuilder setEmptyTextColor(int emptyTextColor) {
            this.emptyTextColor = emptyTextColor;
            return this;
        }

        public EmptyViewBuilder setLayoutParams(ViewGroup.LayoutParams layoutParams) {
            this.layoutParams = layoutParams;
            return this;
        }

        public EmptyViewBuilder setEmptyTextSize(int emptyTextSize) {
            this.emptyTextSize = emptyTextSize;
            return this;
        }

        public EmptyViewBuilder setEmptyTextSizePx(int emptyTextSize) {
            this.emptyTextSize = px2sp(context, emptyTextSize);
            return this;
        }


        public EmptyViewBuilder setShowIcon(boolean mShowIcon) {
            this.mShowIcon = mShowIcon ? ShowType.SHOW : ShowType.HIDE;
            return this;
        }


        public EmptyViewBuilder setShowText(boolean mShowText) {
            this.mShowText = mShowText ? ShowType.SHOW : ShowType.HIDE;
            return this;
        }


        public EmptyViewBuilder setShowButton(boolean mShowButton) {
            this.mShowButton = mShowButton ? ShowType.SHOW : ShowType.HIDE;
            return this;
        }


        public EmptyViewBuilder setListener(View.OnClickListener listener) {
            this.listener = listener;
            return this;
        }


        public EmptyViewBuilder setActionText(String mActionText) {
            this.mActionText = mActionText;
            return this;
        }
    }


    private static void removeExistEmptyView(AdapterView listView) {
        if (listView.getEmptyView() != null) {
            ViewParent rootView = listView.getParent();
            if (rootView instanceof ViewGroup) {
                ((ViewGroup) rootView).removeView(listView.getEmptyView());
            }
        }
    }

    public static void setEmptyView(AdapterView listView, EmptyViewBuilder builder) {
        builder.bindView(listView);
    }

    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spValue, context.getResources().getDisplayMetrics());
    }


    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}
