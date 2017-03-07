# **项目中框架搭建**
    这个项目是现实中公司项目开发，使用所有的新技术搭建的框架，主要是为了更好的些协同开发。项目中使用MVP模式很好的解决了MVC模式中model层直接和view进行通信，从而两层耦合性和高，
    每层都是专注于对应的事情，如view层是专注于页面展示，用户数据接收展示等，中间Presenter处理一些业务逻辑，分别提供接口给view层和model层。model层专注于数据处理，比如数据是从
    网络拉取还是从数据库中读取。
***
1.该项目框架是使用MVP模式搭建整体框架，其中运用的技术有Retrofit，okhttp，RxJava，Dagger2,GreeDao等技术
***
2.其中分为三层，App层为view的层，network为数据持久层和中间控制层结合，dbCreate为数据创建Module，Utils为公共的module
