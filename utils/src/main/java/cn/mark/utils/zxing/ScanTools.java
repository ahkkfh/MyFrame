package cn.mark.utils.zxing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;


/***
 * @author marks.luo
 * @Description: TODO(扫描帮助类)
 * @date:2017-04-13 09:33
 */
public class ScanTools {

    /***
     * 扫描回调
     */
    public interface ScanCall {
        void getCode(String code);
    }

    /***
     * 扫描当前View上的二维码
     * @param view 显示二维码的view
     * @param scanCall 扫描回调
     */
    public static void scanCode(View view, ScanCall scanCall) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);//创建Bitmap
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.rgb(0xf1, 0xf1, 0xf1));
        view.draw(canvas);
        if (bitmap != null) {
            //扫描
            String code = scanBitmap(bitmap);
            scanCall.getCode(code);
        }
    }

    /***
     * 解析二维码
     * @param bitmap  存放二维码的bitmap
     * @return
     */
    public static String scanBitmap(Bitmap bitmap) {
        Map<DecodeHintType, String> hints = new HashMap<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(bitmap);
        //将图片转换成二进制图片
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
        //初始化解析扫描对象
        QRCodeReader reader = new QRCodeReader();
        //开始解析
        Result result = null;
        try {
            result = reader.decode(binaryBitmap, hints);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null) {
            return result.getText();
        }
        return "";
    }


    private static final int BLACK = 0xff000000;

    /***
     * 生产二维码
     * @param content 二维码中包含内容
     * @param widthAndHeight 宽和高
     * @return
     */
    public static Bitmap createQRCode(String content, int widthAndHeight) throws WriterException {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = BLACK;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /***
     * 创建二维码
     * @param content 二维码中包含内容
     * @param widthPx 二维码宽度(像素)
     * @param heightpx  二维码高度(像素)
     * @return
     */
    public static Bitmap createQRCode(String content, int widthPx, int heightpx, Bitmap logoBitmap) {
        try {
            if (TextUtils.isEmpty(content)) {
                return null;
            }
            //配置参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//设置编码
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);//容错级别
            //图像数据转换，使用了矩阵转换

            BitMatrix matrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPx, heightpx, hints);
            int[] pixels = new int[widthPx * heightpx];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < heightpx; y++) {
                for (int x = 0; x < widthPx; x++) {
                    if (matrix.get(x, y)) {//设置不同颜色区分内容信息
                        pixels[y * widthPx + x] = 0xff000000;
                    } else {
                        pixels[y * widthPx + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(widthPx, heightpx, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, widthPx, 0, 0, widthPx, heightpx);
            if (logoBitmap != null) {
                bitmap = addLogo(bitmap, logoBitmap);
            }
            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 在二维码中间添加Logo图案
     * @param bitmap 二维码图片
     * @param logoBitmap logo图片
     * @return 添加好的图片
     */
    private static Bitmap addLogo(Bitmap bitmap, Bitmap logoBitmap) {
        if (null == bitmap) {
            return null;
        }
        if (null == logoBitmap) {
            return bitmap;
        }
        //获取图片宽高
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int logoBitwidth = logoBitmap.getWidth();
        int logoBitheight = logoBitmap.getHeight();
        if (bitmapWidth == 0 || bitmapHeight == 0) {
            return null;
        }
        if (logoBitwidth == 0 || logoBitheight == 0) {
            return bitmap;
        }
        //logo大小为二维码整体大小的1/5
        float scaleFactor = bitmapWidth * 1.0f / 5 / logoBitwidth;
        Bitmap newBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(bitmap, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, bitmapWidth / 2, bitmapHeight / 2);
            canvas.drawBitmap(logoBitmap, (bitmapWidth - logoBitwidth) / 2, (bitmapHeight - logoBitheight) / 2, null);
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            newBitmap = null;
            e.getStackTrace();
        }
        return newBitmap;
    }
}
