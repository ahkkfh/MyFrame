package cn.mark.frame.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import cn.mark.frame.R;
import cn.mark.utils.StatusBarUtil;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-04-07 17:42
 */
public class BaseFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.colorPrimary));
    }
}
