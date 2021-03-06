package longimage.progresspie;


import android.view.LayoutInflater;
import android.view.View;

import com.filippudak.ProgressPieView.ProgressPieView;

import java.util.Locale;

import longimage.R;
import longimage.bigImageView.indicator.ProgressIndicator;
import longimage.bigImageView.view.BigImageView;

/***
 * @author marks.luo
 * @Description: TODO(自定义 progress)
 * @date:2017-06-14 11:56
 */
public class ProgressPieIndicator implements ProgressIndicator {
    private ProgressPieView mProgressPieView;

    @Override
    public View getView(BigImageView parent) {
        mProgressPieView = (ProgressPieView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ui_progress_pie_indicator, parent, false);
        return mProgressPieView;
    }

    @Override
    public void onStart() {
        // not interested
    }

    @Override
    public void onProgress(int progress) {
        if (progress < 0 || progress > 100 || mProgressPieView == null) {
            return;
        }
        mProgressPieView.setProgress(progress);
        mProgressPieView.setText(String.format(Locale.getDefault(), "%d%%", progress));
    }

    @Override
    public void onFinish() {
        // not interested
    }
}