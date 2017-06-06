package cn.mark.frame.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityPpchartBinding;
import cn.mark.frame.ui.wedight.PPChart;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-24 17:07
 */
public class ChartActivity extends BaseActivity {
    private ActivityPpchartBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_ppchart);
        initView();
    }

    @Override
    protected void initView() {
        addData();
    }

    private void addData() {
        List<PPChart.DataObject> dataObjects = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            if (i%2==0){
                dataObjects.add(new PPChart.DataObject("i----" + i, (5 * i)));
            }else{
                dataObjects.add(new PPChart.DataObject("i----" + i, (2*i)));
            }
        }
        mBinding.chart.setData(dataObjects);
    }
}
