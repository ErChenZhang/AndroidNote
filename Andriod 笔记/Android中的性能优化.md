##性能优化（一个微小的优化可能看不出什么变化，但是当执行的次数和优化的项目的增多之后这些优化就显而易见了，用C++做了一个性能测试器）
 

###编码方式
* 使用增强for循环代替正常的for循环
* 使用线程池优化代码
	* new thread会有弊端：新建对象性能差；线程缺乏统一的管理；
	* 线程池的好处：重用存在的线程，减少对象的创建；可以有效的控制最大并发线程数，提高系统资源的使用率；
* 该注销的，该销毁的都要适时的销毁，防止内存泄漏
	* WebView需要在activity或者Fragment销毁的时候销毁
	* Broadcast动态注册的时候，要在调用者生命周期结束的时候去注销这个广播接收者unregisterReceiver
	* Cursor回收，数据库查询的一个类，，在使用结束后，应该保证Cursor占用的内存被及时手动释放掉，否则等到垃圾回收期来回收的时候，就会给用户错误提示
	* Stream/File(流/文件)回收 
	
* listView的优化
	* Item布局，层级越少越好，使用hierarchyview工具查看优化
	* 复用convertView
	* 使用ViewHolder
	* item中有图片时要选择异步加载，应对图片进行适当压缩
	* 快速滑动的时候，不加载图片
	* 实现数据的分页加载

###内存
* 如何检测内存泄漏
	* 使用AS中的Memory窗口
	* DDMS-Heap内存监测工具窗口
	* leakcanary，一个内存泄露自动检测工具
	* Android应用内存泄露MAT工具定位分析--Eclipse

* 如何解决内存泄漏的问题 -- 导致内存泄漏的原因无非就是垃圾回收器没有及时回收不再使用的对象的引用
	* 使用单例模式的时候，单例对象持有该Activity的引用，因此当该Activity退出的时候它的内存并不会被回收，也会导致内存泄漏，解决办法：传入Application的Context，让单例对象的生命周期和Application一样长
	* 非静态内部类中创建静态实例，例如在handler中持有其他Activity引用，activity退出，但是handler依然在做耗时操作，会导致内存泄漏。解决办法：handler设置为static，并且弱引用activity
	* Cursor和IO流要及时关闭
	* 及时回收内存和有效利用已用对象，例如Bitmap对象使用结束后，及时调用recycle(),Adapter时要复用convertView
	
###UI
##,##UI启动卡顿，动画不流畅等性能分析 
#### -- HierarchyViewer分析UI性能 
#### -- 使用Lint进行资源及冗余UI布局等优化
#### -- 使用Memory监测及GC打印与Allocation Tracker进行UI卡顿分析
* ANR
* 人为在UI线程中进行轻微耗时操作
* 布局Layout过于复杂，无法再16ms内完成渲染
* 同一时间执行动画的次数过多，导致CPU或者GPU负载过重
* View过度绘制，导致某一些像素在同一帧时间内被绘制多次
* view频繁的触发measure、layout，导致measure、layout耗时过多
* 内存频繁触发GC，导致暂时阻塞渲染操作

* 减少视图层级
	* ViewStub标签，直观效果类似于设置View不可见，更重大的意义在于该标签包裹的View在默认状态下不会占用任何内存空间
	* 采用<merge>优化布局层数
	* 需要重复使用的样式，抽取出来，使用<include>标签


###网络

###图片
* 图片处理
	* 等比缩放，使用采样率inSampleSize ,只是改变图片大小并不能彻底解决内存溢出
	* 对图片采用软引用，及时进行recycle()操作
	* 设置图片解码格式 
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_4444;
	* 加载部分图片

* 图片缓存-三级缓存（内存缓存、磁盘缓存、网络缓存）
* 使用加载图片框架处理图片（如专业的imageLoader、glide、Picasso）