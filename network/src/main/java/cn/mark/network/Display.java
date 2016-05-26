package cn.mark.network;


import cn.mark.utils.enmu.MainType;

/**
 * Created by yaoping on 2016/5/19.
 * 指定跳转对应的页面
 */
public interface Display {
    //----------------main page show-------------//

    //跳转到主页指定页面
    void showPage(MainType type);

    void showDefaultPage();

    //跳转到主页指定页
    void showPager();

    //显示主页
    void showHome();

    //显示目的地
    void showDestination();

    //显示指定目的地id
    void showDestination(int destId);

    //显示购物车
    void showCart();

    //显示购物车
    void showCenter();

    //显示购物车列表
    void showCartList();

    //更新购物车数量
    void updateCartSize(int size);

    //显示订单详情
    void showOrderDetail(String id);
}
