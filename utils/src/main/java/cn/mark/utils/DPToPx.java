package cn.mark.utils;

import android.content.Context;

/***
 * @author marks.luo
 * @Description:(dp，sp px 之间的装换)
 * @date:2017-04-25 14:40
 */
public class DPToPx {

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int sp2px(Context context, float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }
}
