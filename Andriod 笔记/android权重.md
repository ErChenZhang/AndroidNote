##Android	线性布局权重

**权重（layout_weight）**：即为当前线性布局指定方向(水平、竖直)上剩余空间的一个分配规则。

注：以下皆以水平方向上剩余空间为案例分析，当前测试手机分辨率为480*320，屏幕像素密度为mdpi，即1dp = 1px；

####案例一布局：

	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:orientation="horizontal"
	    tools:context=".MainActivity" >
	   <!--内部控件水平排列-->
	   <TextView
	        android:layout_width="0dp"
	        android:layout_height="120dp"
	        android:layout_weight="3"
	        android:background="@android:color/black"/>
	    <TextView
	    	android:layout_width="0dp"
		    android:layout_height="120dp"
		    android:layout_weight="1"
		    android:background="@android:color/holo_green_dark"/>
	</LinearLayout>

当前布局显示效果

![](http://i.imgur.com/Rrlc1pN.jpg)

由上图可见当前黑色部分占用屏幕的四分之三，绿色部分占屏幕的四分之一

黑色区域宽度，绿色区域宽度计算方式：

	当前屏幕横屏宽度：320dp
	第一个子控件未分配权重前所占宽度：0dp 
	第二个子控件未分配权重前所占宽度：0dp 
	当前屏幕剩余空间总数：320dp-0dp-0dp = 320dp，将当前320dp按权重分配给两个子控件，子控件一分配到四分之三，子控件二分配到四分之一
	
	第一个子控件分配权重后宽度：0dp+（（320dp-0dp-0dp）*3）/4 = 240dp
	第二个子控件分配权重后宽度：0dp+（320dp-0dp-0dp）/4 = 80dp


####案例二布局：

	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:orientation="horizontal"
	    tools:context=".MainActivity" >
	   <TextView
	        android:layout_width="60dp"
	        android:layout_height="120dp"
	        android:layout_weight="3"
	        android:background="@android:color/black"/>
	    <TextView
	    	android:layout_width="60dp"
		    android:layout_height="120dp"
		    android:layout_weight="1"
		    android:background="@android:color/holo_green_dark"/>
	</LinearLayout>
	
当前布局显示效果

![](http://i.imgur.com/6PwNgOH.jpg)


黑色区域宽度，绿色区域宽度计算方式：

	当前屏幕横屏宽度：320dp
	第一个子控件未分配权重前所占宽度：60dp 
	第二个子控件未分配权重前所占宽度：60dp 
	当前屏幕剩余空间总数：320dp-60dp-60dp = 200dp，将当前200dp按权重分配给两个子控件，子控件一分配到四分之三，子控件二分配到四分之一
	
	第一个子控件分配权重后宽度：60dp+（（320dp-60dp-60dp）*3）/4 = 210dp
	第二个子控件分配权重后宽度：60dp+（320dp-60dp-60dp）/4 = 110dp

####案例三布局：

	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:orientation="horizontal"
	    tools:context=".MainActivity" >
	   <TextView
	        android:layout_width="260dp"
	        android:layout_height="120dp"
	        android:layout_weight="3"
	        android:background="@android:color/black"/>
	    <TextView
	    	android:layout_width="260dp"
		    android:layout_height="120dp"
		    android:layout_weight="1"
		    android:background="@android:color/holo_green_dark"/>
	</LinearLayout>
当前布局显示效果

![](http://i.imgur.com/Ldq0yf1.jpg)


黑色区域宽度，绿色区域宽度计算方式：

	当前屏幕横屏宽度：320dp
	第一个子控件未分配权重前所占宽度：260dp 
	第二个子控件未分配权重前所占宽度：260dp 
	当前屏幕剩余空间总数：320dp-260dp-260dp = -200dp，将当前-200dp按权重分配给两个子控件，子控件一分配到四分之三，子控件二分配到四分之一
	
	第一个子控件分配权重后宽度：260dp+（（320dp-260dp-260dp）*3）/4 = 110dp
	第二个子控件分配权重后宽度：260dp+（320dp-260dp-260dp）/4 = 210dp


####案例四布局：

	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:orientation="horizontal"
	    tools:context=".MainActivity" >
	   <TextView
	        android:layout_width="fill_parent"
	        android:layout_height="120dp"
	        android:layout_weight="3"
	        android:background="@android:color/black"/>
	    <TextView
	    	android:layout_width="fill_parent"
		    android:layout_height="120dp"
		    android:layout_weight="1"
		    android:background="@android:color/holo_green_dark"/>
	</LinearLayout>
当前布局显示效果

![](http://i.imgur.com/FuTljJV.jpg)

	当前屏幕横屏宽度：320dp
	第一个子控件未分配权重前所占宽度：fill_parent 即为充满横屏(320dp)
	第二个子控件未分配权重前所占宽度：fill_parent 即为充满横屏(320dp)
	当前屏幕剩余空间总数：320dp-320dp-320dp = -320dp，将当前-320dp按权重分配给两个子控件，子控件一分配到四分之三，子控件二分配到四分之一
	
	第一个子控件分配权重后宽度：320dp+（（320dp-320dp-320dp）*3）/4 = 80dp
	第二个子控件分配权重后宽度：320dp+（320dp-320dp-320dp）/4 = 240dp

由以上四个案例总结：
	
	例:如水平方向上的线性布局LinearLayout控件L中，包含两个水平占用空间的控件A,B。
	   L控件：L控件宽度layout_width = width_l
	   A控件：a控件宽度layout_width = width_a    a控件权重layout_weight = weight_a
	   B控件：b控件宽度layout_width = width_b    b控件权重layout_weight = weight_b

	L中子控件最终占用宽度 = 原有宽度(width_a)+剩余空间分配量
	
	A所占宽度 = width_a + (width_l-width_a-width_b)*weight_a/(weight_a+weight_b)
	B所占宽度 = width_b + (width_l-width_a-width_b)*weight_b/(weight_a+weight_b)
	
	由以上案例可以得出推断：

	情况一：当L中内部子控件(A,B)的宽度之和大于L的总宽度时，即(width_l-width_a-width_b)<0时，weight_a/(weight_a+weight_b)比例的值越大，当前控件所占空间越小。

	情况二：当L中内部子控件(A,B)的宽度之和小于L的总宽度时，即(width_l-width_a-width_b)>0时，weight_a/(weight_a+weight_b)比例的值越大，当前控件所占空间越大。
	 
