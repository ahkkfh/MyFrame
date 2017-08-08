package longimage.matisse;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

/***
 * @author marks.luo
 * @Description: ({@link ImageEngine} implementation using fresco.)
 * @date:2017-08-08 11:57
 *
 */
public class FrescoEngine implements ImageEngine {
    @Override
    public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {

    }

    @Override
    public void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {

    }

    @Override
    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {

    }

    @Override
    public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {

    }

    @Override
    public boolean supportAnimatedGif() {
        return false;
    }
}
