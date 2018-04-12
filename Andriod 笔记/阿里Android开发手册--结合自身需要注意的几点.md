##阿里Android开发手册--结合自身需要注意的几点

###Android资源文件命名和使用
在Android开发过程，有时候头痛的不是需求实现不了，往往就是命名，阿里给出的这要命名规则，大大地减少了我们开发过程不必要的浪费时间

1. 所有资源文件带上模块前缀，都小写
2. layout的命名规则（**模块名_样式名称**）

		Activity 的 layout 以 module_activity 开头
  	 	Fragment 的 layout 以 module_fragment 开头
    	Dialog 的 layout 以 module_dialog 开头
     	include 的 layout 以 module_include 开头
     	ListView 的行 layout 以 module_list_item 开头
     	RecyclerView 的 item layout 以 module_recycle_item 开头
     	GridView 的行 layout 以 module_grid_item 开头
3. drawable资源名称（**模块名\_业务功能描述\_控件描述\_控件状态限定词**）

		module_login_btn_pressed
		module_tabs_icon_home_normal
4. anim资源命名（**模块名\_逻辑名称\_方向[序号**]）

		module_fade_in
		module_fade_out
		module_push_down_in	
5. 颜色资源名称(**模块名\_逻辑名称\_颜色**)

		<color name="module_btn_bg_color">#33b5e5e5</color>
6. dimen资源（**模块名\_描述信息**）

		<dimen name="module_horizontal_line_height">1dp</dimen>
7. style资源名称(**父 style 名称.当前 style 名称**)

		<style name="ParentTheme.ThisActivityTheme">
		…
		</style>
8. string资源文件名称(**模块名_逻辑名称**)

		moudule_login_tips
		module_homepage_notice_desc

###Android四大组件

1. 定义全局类 Toast和log定义成工具类  Toast避免了连续弹出时不能取消上一次的现象 log可以统一关闭

2. 如果广播只用于应用内 最好使用**LocalBroadcastManager**.getInstance(context).sendBroadcast(intent)发送广播，避免了广播泄露以及被拦截，同时相对全局广播本地广播更加高效
3. list的adapter中的item无论是否有内容都要设置（例如文本为空都要设置setText("")），避免在滑动过程中出现出现内容的混乱
4. 尽量避免嵌套的Fragment

	1.  onActivityResult()方法的处理错乱，内嵌的 Fragment 可能收不到该方法的回调，需要由宿主 Fragment 进行转发处理；
	2.  突变动画效果；
	3.  被继承的 setRetainInstance()，导致在 Fragment 重建时多次触发不必要的逻
辑

5. 避免在 Service#onStartCommand()/onBind()方法中执行耗时操作，如果确实有需求，应改用 IntentService 或采用其他异步机制完成,因为IntentService实际是Service的子类，对其方法进行了重写，其实IntentService在执行onCreate的方法的时候，其实开了一个线程HandlerThread


###UI和布局
1. 尽量使用RelativeLayout替代LinearLayout，减少嵌套的层级，可以使用AS上的Hierarachy Viewer 查看View的嵌套层级

2. Activity中的对话框尽量使用DialogFragment，而不是Dialog/AlertDialog,好处是可以随着Activity生命周期管理对话框的生命周期

3. 禁止设置子View和父View为同样的背景导致的过度绘制

4. 帧动画尽量避免使用AnimationDrawable，图片会加载到内存中，如果图片过多会导致OOM
5. ScrollView不要嵌套LsitView/GridView/ExpandableListview，处理不好会导致滑动的冲突，这样也会把所有的Item加载到内存中，可以使用NestedScrollView处理

###进程、线程与消息通信
1. 新建线程时，必须通过线程池提供（AsyncTask或者ThreadPoolexecutor或者其他形式自定义的线程池）

2. 	网络操作不要放在主线程中调用

3. 	不要在非UI线程中初始化ViewStub，不然会报null，ViewStub是个好东西啊，可以延迟View加载，渲染时间会减少

4. 	不要硬编码文件路径，使用api访问
				android.os.Environment#getExternalStorageDirectory()
	android.os.Environment#getExternalStoragePublicDirectory()
	android.content.Context#getFilesDir()
	android.content.Context#getCacheDir

5. 	使用到外部存储的时候，必须检查外部存储的可用性（Environment.getExternalStorageState()）

6. SharedPreference只能存储简单数据类型，复杂的数据类型应该使用文件、数据库等其他方式存储（切记不要讲复杂的数据转成String）

###文件和数据库
1. 执行SQL语句的时候，应该使用SQLiteDatabase的iinsert()、 update()、 delete()
方法，尽量避免使用SQL语句

###Bitmap、Drawable与动画
1. 操作图片时，要放在异步线程中进行，可能会导致卡顿

2. 对一些资源图片用tinypng进行压缩

3. 图片使用完成后，要释放资源

4. 页面退出时要清理动画资源
5. 