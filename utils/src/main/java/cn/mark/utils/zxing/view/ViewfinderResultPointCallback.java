package cn.mark.utils.zxing.view;

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-13 17:40
 */
public class ViewfinderResultPointCallback implements ResultPointCallback {
    private final ViewfinderView viewfinderView;

    public ViewfinderResultPointCallback(ViewfinderView viewfinderView) {
        this.viewfinderView = viewfinderView;
    }

    @Override
    public void foundPossibleResultPoint(ResultPoint point) {
        viewfinderView.addPossibleResultPoint(point);
    }
}
