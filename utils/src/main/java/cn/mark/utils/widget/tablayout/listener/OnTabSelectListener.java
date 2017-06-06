package cn.mark.utils.widget.tablayout.listener;

/***
 * @author marks.luo
 * @Description: TODO(tab选择监听器)
 * @date:2017-04-25 14:49
 */
public interface OnTabSelectListener {
    void onTabSelect(int position);

    /***
     * tab重选
     * @param position
     */
    void onTabReselect(int position);
}
