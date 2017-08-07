package longimage.photodrawable;

import android.content.Context;
import android.util.DisplayMetrics;

/***
 * @author marks.luo
 * @Description: ()
 * @date:2017-08-04 15:17
 *
 */
public class AppConfig {

    public static void initConfig(Context context) {
        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        PhoneInfo.screenWidth = metric.widthPixels < metric.heightPixels ? metric.widthPixels : metric.heightPixels;  // 屏幕宽度（像素）
        PhoneInfo.screenheight = metric.heightPixels > metric.widthPixels ? metric.heightPixels : metric.widthPixels;  // 屏幕高度（像素）
        PhoneInfo.screen_ration = PhoneInfo.screenheight / (float) PhoneInfo.screenWidth;
        PhoneInfo.density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
    }

    public static class PhoneInfo {
        public static int screenWidth;
        public static int screenheight;//手机屏幕高宽比h/w
        public static float screen_ration;
        public static float density;
    }
}
