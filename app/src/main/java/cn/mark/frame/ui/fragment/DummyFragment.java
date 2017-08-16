package cn.mark.frame.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.mark.frame.R;
import cn.mark.frame.base.BaseFragment;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-08-16 15:26
 *
 */
public class DummyFragment extends BaseFragment {
    public DummyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dummy, container, false);
    }
}
