

#FlowGeek
FlowGeek是基于MVP架构的、遵循Material Design设计规范的开源中国社区客户端。

开源中国社区客户端自面世开源以来，	给很多初学者到来了很多帮助，感谢@火蚁 的工作。现在技术革新很厉害，而我们开源中国的社区app还一直停留在原始的样子，不断的收集大家的意见和issue，改改bug什么的，并没有做改版。其实到这，改版的理由已经很充足了，技术落后、界面难看、代码臃肿...但是各位老司机都应该清楚，改版这种事情是件工作量具大的，理解业务逻辑、UI设计、架构设计、开源组件选择...所以，内部也是停留在想想的阶段。所幸来到开源中国，工作量（暂时）不大，还是比较轻松的，最近业界也津津乐道过MVP、Material Design一阵子，闲来无事，怀着一颗我不入地狱谁入地狱的决心，我就入坑了...

##技术架构

###MVP

<img src="http://git.oschina.net/uploads/images/2016/0310/133601_cf3c9118_116508.png" width="600"/>
![输入图片说明](http://git.oschina.net/uploads/images/2016/0310/133601_cf3c9118_116508.png "在这里输入图片标题")

###类图
Activity
![输入图片说明](http://git.oschina.net/uploads/images/2016/0310/133604_d1087573_116508.png "在这里输入图片标题")

Fragment
![输入图片说明](http://git.oschina.net/uploads/images/2016/0310/133607_6191b5fe_116508.png "在这里输入图片标题")

Presenter
![输入图片说明](http://git.oschina.net/uploads/images/2016/0310/133609_5518d6b3_116508.png "在这里输入图片标题")

我发现我还是很敬业的

##主要开源组件
- [RxJava/RxAndroid](https://github.com/ReactiveX/RxJava)：Java的响应式编程的库，相当强大且相当赞！！推荐大家深入到源码，体会设计的强大。

- [Retrofit](https://github.com/square/retrofit)：优雅的HTTP请求开源库，使用动态代理实现，也很赞！退加你深入源码。

- [Picasso](https://github.com/square/picasso)：强大的图像加载库，Square公司真是业界良心。

- [ButterKnift]()：告别findViewById

- [RxLifecycle](https://github.com/trello/RxLifecycle)：在Acvitity和Fragment中管理订阅者/观察者的生命周期。

- [Nucleus](https://github.com/konmik/nucleus)：MVP框架基础库，我使用的MVP基础类就是来源于他，感谢这位开发者。

- [Gson](https://github.com/google/gson)：Google的json解析库。



##推荐Document
- [RxReactive](http://reactivex.io/)

- [Retrofit](http://square.github.io/retrofit/#contributing)

- [Retrofit技术博文](https://futurestud.io/blog/retrofit-getting-started-and-android-client)

- [MVP](http://www.tuicool.com/articles/uIjEJj7)

- [Material Design中文版](http://wiki.jikexueyuan.com/project/material-design/)
 
- [RxJava技术博文](http://gank.io/post/560e15be2dca930e00da1083)

##Feture
- **2016.03.10更新**：目前功能：登录、资讯（暂且只支持一些分类，其他太过复杂）、资讯评论、动弹列表、我的动弹、发表动弹（文字or有图像）、动弹评论


##效果图一览
#FlowGeek
![输入图片说明](http://git.oschina.net/uploads/images/2016/0310/140315_bbf764cc_116508.png "在这里输入图片标题")

![输入图片说明](http://git.oschina.net/uploads/images/2016/0310/140317_1db676f0_116508.png "在这里输入图片标题")

![输入图片说明](http://git.oschina.net/uploads/images/2016/0310/140319_0b00d52d_116508.png "在这里输入图片标题")

![输入图片说明](http://git.oschina.net/uploads/images/2016/0310/140333_50142400_116508.png "在这里输入图片标题")

![输入图片说明](http://git.oschina.net/uploads/images/2016/0310/140335_041eb74f_116508.png "在这里输入图片标题")

![输入图片说明](http://git.oschina.net/uploads/images/2016/0310/140336_b459238f_116508.png "在这里输入图片标题")

![输入图片说明](http://git.oschina.net/uploads/images/2016/0310/140338_5dee381c_116508.png "在这里输入图片标题")

![输入图片说明](http://git.oschina.net/uploads/images/2016/0310/140340_621e5059_116508.png "在这里输入图片标题")

![输入图片说明](http://git.oschina.net/uploads/images/2016/0310/140342_1c1dd7b1_116508.png "在这里输入图片标题")


![输入图片说明](http://git.oschina.net/uploads/images/2016/0310/140356_9403ffcd_116508.png "在这里输入图片标题")

![输入图片说明](http://git.oschina.net/uploads/images/2016/0310/140427_e8e3fcaf_116508.png "在这里输入图片标题")

![输入图片说明](http://git.oschina.net/uploads/images/2016/0310/140435_c73abe4d_116508.png "在这里输入图片标题")

![输入图片说明](http://git.oschina.net/uploads/images/2016/0310/140437_0effd45f_116508.png "在这里输入图片标题")

![输入图片说明](http://git.oschina.net/uploads/images/2016/0310/140438_f37f37e1_116508.png "在这里输入图片标题")

![输入图片说明](http://git.oschina.net/uploads/images/2016/0310/140440_c5e114a0_116508.png "在这里输入图片标题")

![输入图片说明](http://git.oschina.net/uploads/images/2016/0310/140442_3d2414e2_116508.png "在这里输入图片标题")

![输入图片说明](http://git.oschina.net/uploads/images/2016/0310/140450_6f4611e7_116508.png "在这里输入图片标题")

##Note
- 本人技术和见识都有限，自己认为好的不一定是被认可的，可取的，我希望大家不吝赐教，发挥开源的力量。

- 我一直在自己的小米手机上开发的，版本是5.1，回过头看了下4.4的样子，发现有些地方匪夷所思，暂且Design Support向下兼容得并不好，很多地方不相通，如果你想看最佳的效果，请用Android5.0以上。

- 没有正式完成前改动都会比较大，希望大家定期关注。

- 恩，我可以去当设计师了╭∩╮（︶︿︶）╭∩╮

