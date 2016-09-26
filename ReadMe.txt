这是一个集成DataBinding、retrofit2、RxJava、GreenDao、Dagger2的MVP框架。


2016-07-22
    1.配置应用自动打包发送到fir上
    2.使用Retrofit+Dagger2等下载文件（未完成）


2016-07-25
    使用retrofit2+dagger2等完成下载文件功能（因为retrofit请求是抽取了一个公共的地址，所以不好用retrofit来下载apk，
       可以考虑其他的方案）

2016-08-04
    了解AccessibilityService,项目中加入AccessibilityService。(该功能主要是辅助人们去使用android设备，但是它是在后台运行，
    接受系统回调),AccessibilityService可以拦截窗体状态改变，通知栏状态改变，view被点击等等。可以实现的功能有自动化测试
    自动抢红包，自动安装。但是当你开启这中辅助功能时会对你的隐私信息带来一些风险，所以还是要谨慎开启第三方辅助功能。
