package cn.mark.utils.longImageView;

/***
 * @author marks.luo
 * @Description: TODO(事件侦听器，允许子类和活动通知重要事件。)
 * @date:2017-03-07 17:57
 */
public interface OnImageEventListener {
    /**
     * Called when the dimensions of the image and view are known, and either a preview image,
     * the full size image, or base layer tiles are loaded. This indicates the scale and translate
     * are known and the next draw will display an image. This event can be used to hide a loading
     * graphic, or inform a subclass that it is safe to draw overlays.
     * (当图像和视图的尺寸已知时调用，并且加载预览图像，全尺寸图像或基本图块。
     * 这表示比例和翻译已知，下一个绘制将显示图像。 此事件可用于隐藏加载图形，或通知子类可以安全地绘制叠加。)
     */
    void onReady();

    /**
     * Called when the full size image is ready. When using tiling, this means the lowest resolution
     * base layer of tiles are loaded, and when tiling is disabled, the image bitmap is loaded.
     * This event could be used as a trigger to enable gestures if you wanted interaction disabled
     * while only a preview is displayed, otherwise for most cases {@link #onReady()} is the best
     * event to listen to.
     * (当全尺寸图像准备就绪时调用。 当使用平铺时，这意味着加载了最低分辨率基本图像块，并且禁用平铺时，
     * 将加载图像位图。 如果您希望禁用交互，而仅显示预览，
     * 则此事件可用作触发器以启用手势，否则在大多数情况下{@link #onReady（）}是最好的事件。)
     */
    void onImageLoaded();

    /**
     * Called when a preview image could not be loaded. This method cannot be relied upon; certain
     * encoding types of supported image formats can result in corrupt or blank images being loaded
     * and displayed with no detectable error. The view will continue to load the full size image.
     * (无法加载预览图像时调用。 这种方法不能依赖;
     * 所支持的图像格式的某些编码类型可能导致损坏或空白图像被加载和显示，没有可检测的错误。
     * 视图将继续加载完整大小的图像。)
     * @param e The exception thrown. This error is logged by the view.
     */
    void onPreviewLoadError(Exception e);

    /**
     * Indicates an error initiliasing the decoder when using a tiling, or when loading the full
     * size bitmap when tiling is disabled. This method cannot be relied upon; certain encoding
     * types of supported image formats can result in corrupt or blank images being loaded and
     * displayed with no detectable error.
     * (表示在使用平铺时或在禁用平铺时加载完整大小的位图时导致解码器出错的错误。
     * 这种方法不能依赖; 所支持的图像格式的某些编码类型可能导致损坏或空白图像被加载和显示，没有可检测的错误。)
     * @param e The exception thrown. This error is also logged by the view.
     */
    void onImageLoadError(Exception e);

    /**
     * Called when an image tile could not be loaded. This method cannot be relied upon; certain
     * encoding types of supported image formats can result in corrupt or blank images being loaded
     * and displayed with no detectable error. Most cases where an unsupported file is used will
     * result in an error caught by {@link #onImageLoadError(Exception)}.
     * (无法加载图像平铺时调用。 这种方法不能依赖; 所支持的图像格式的某些编码类型可能导致损坏或空白图像被加载和显示，
     * 没有可检测的错误。 大多数使用不支持的文件的情况会导致{@link #onImageLoadError（Exception）}捕获到错误。)
     * @param e The exception thrown. This error is logged by the view.
     */
    void onTileLoadError(Exception e);

    /**
     * Called when a bitmap set using ImageSource.cachedBitmap is no longer being used by the View.
     * This is useful if you wish to manage the bitmap after the preview is shown
     * (当使用ImageSource.cachedBitmap设置的位图不再由View使用时调用。如果您希望在显示预览后管理位图，这是有用的)
     */
    void onPreviewReleased();
}
