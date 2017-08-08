package cn.mark.frame.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseActivity;
import cn.mark.frame.databinding.ActivityListviewVpBinding;
import cn.mark.frame.ui.adapter.ViewPageAdapter;
import longimage.photodrawable.CustomViewpager;

/***
 * @author marks.luo
 * @Description: (ViewPagerActivity 显示图片)
 * @date:2017-08-04 17:48
 *
 */
public class ViewPagerActivity extends BaseActivity {
    private ActivityListviewVpBinding mBinding;
    private ArrayList<String> mUrlList = new ArrayList<>();
    private String[] arr = new String[]{
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/08/07/15020890643409.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/30/15014102181208.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/30/15014102197958.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/30/15014102194216.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/30/15014102206557.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/30/15014102219713.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/30/15014102226219.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/30/15014102233259.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/30/15014102232654.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/30/15014102237998.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/25/15009790778584.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/25/15009791135455.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/25/15009791466777.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/25/15009791821982.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/20/15005167325963.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/20/15005167357984.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/20/15005167356804.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/20/15005167376918.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/20/15005167387945.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/20/15005167406637.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/20/15005167418872.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/20/15005167428073.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/20/15005167441320.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/13/14999201534476.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/13/14999201538751.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/13/14999201543976.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/13/14999201544693.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/13/14999201547907.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/13/14999201546634.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/13/14999201543103.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/05/14992281246599.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/05/14992281457385.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/05/14992281609429.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/05/14992281802350.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/05/14992281987564.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/05/14992282184273.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/05/14992282378659.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/05/14992282627157.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/05/14992281987564.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/05/14992281802350.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/05/14992282378659.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/05/14992282627157.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/05/14992282879924.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/05/14992283071454.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/05/14992282378659.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/07/05/14992283071454.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/06/27/14985404676868.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/06/27/14985404691871.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/06/27/14985404702443.jpg",
            "http://imgfs.oppo.cn/uploads/thread/attachment/2017/06/27/14985404716211.jpg"
    };
    private ViewPageAdapter mPageAdapter;
    private int mcurrentPosition = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_listview_vp);
        initData();
        initView();
    }

    private void initData() {
        for (int i = 0; i < arr.length; i++) {
            mUrlList.add(arr[i]);
        }
    }

    @Override
    protected void initView() {
        mPageAdapter = new ViewPageAdapter(mUrlList, this);
        mBinding.vpListView.setAdapter(mPageAdapter);
        mBinding.vpListView.setCurrentItem(0);
        mBinding.vpCount.setText(mcurrentPosition + "/" + arr.length);
        mBinding.vpListView.setOnPageChangeListener(new CustomViewpager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBinding.vpCount.setText((position + 1) + "/" + arr.length);
            }

            @Override
            public void onPageScrollStateChanged(int var1) {

            }
        });
//        mBinding.vpListView.setOnTouchSlopListener(new CustomViewpager.OnTouchSlopListener() {
//            @Override
//            public void onTouch(float diff) {
//                mBinding.vpCount.setAlpha(1 - diff / (AppConfig.PhoneInfo.screenheight * 0.13f));
//                mBinding.getRoot().getBackground().mutate().setAlpha((int) (255 * (1 - diff / (AppConfig.PhoneInfo.screenheight * 0.5f))));//设置整个布局
//            }
//
//            @Override
//            public void onSlopExit() {
//                finish();
//            }
//        });
    }
}
