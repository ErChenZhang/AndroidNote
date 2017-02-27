##android项目下的目录结构
* activity：应用被打开时显示的界面
* src：项目代码
* R.java：存放项目中所有资源的资源ID
* Android jar:存放的是jar包
* libs：存放的是第三方jar包
* assets：存放资源文件，比方说MP3，视频文件
* bin：存放编译打包后的文件
* res：存放资源文件，所有放在此文件夹中的资源都会在R.java中产生资源ID
* drawable：存放的是图片文件
* layout：存放的是布局文件，把布局文件通过指定的资源id传给activity
* menu：定义菜单样式
* String.xml:存放字符串资源，每个资源都有一个指定ID

###Android的配置文件（清单文件）
* Android的四大组件在使用前全部都需要在清单文件中配置
* <Application/>的配置作用在整个应用上的
* <activity/>的配置对给activity生效

###常用的adb指令
* Android debug bridge 安卓调试桥
* adb start-server：启动adb进程
* adb kill-server：杀死进程
* adb devices：查看与当前开发环境连接的设备，此指令也可以开启进程
* adb install XXX.apk：往虚拟机上安装apk
* adb uninstall 包名：删除模拟器中的应用
* adb shell:进入Linux的命令行
* ps：查看运行进程
* ls：查看当前文件目录下的文件结构
* netstat -ano：查看占用端口的进程

##电话拨号器
1. 添加一个按钮
2. 对按钮添加一个监听事件

> 	 通过id获取button
	 View button =  getViewById("R.id.bt_call");//也可以直接强转Button button = (Button)getViewById("R.id.bt_call")
>    给按钮设计点击
	 button.setOnclickListener(new Listener());

3.对button添加监听事件(见下面四种方法)


##点击事件的四种方法
* 创建一个类实现接口，重写OnClick()

>     Public void myListener implements OnClickListener{
>     	@Override
		public void onClick(View v) {
			//按钮被点击的事件
			System.out.println("给110打电话");
			//松耦合
			//机器代码-->c语言-->c++--->java(c+++)--->c#-->自然语言
			//意图。Intent
			//泡茶 泡妞 打人 打酱油
			Intent intent = new Intent();
			//设置动作 拨打电话的动作
			intent.setAction(Intent.ACTION_CALL);
			//设置数据Uri 统一资源标识符  URL统一资源定位符 网络路径 http ftp rstp
			//itheima://56
			intent.setData(Uri.parse("tel://110"));
			//激活动作
			startActivity(intent);
		}
>     }

* 匿名内部类

>		button.setOnClickListener(new OnClickListener(){
		onClick(){
			//设置意图		
		}
	})

* 让当前的activity实现接口
		 extends Activity implements OnClickListener
		如果按钮非常多，建议使用这种方法

* 在布局文件中配置点击事件的方法