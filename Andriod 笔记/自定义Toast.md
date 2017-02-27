##自定义Toast
* 实现和自定义控件的方法类似，重点难点是自定义Toast类中的实现
* 构造方法中的三个关键的参数
	* Windowanager--通过上下文获取
	* mview布局--通过View.inflate() 
	* WindowManager.LayoutParams--参照源码，将TN()构造方法中的mParams相关代码拷贝过来=
* 自定义的Toast的最重要的三点，其他都是万变不离其宗



###步骤：(参考源码)
1. 通过Xml布局实现Toast的长相
	
			<?xml version="1.0" encoding="utf-8"?>
		<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
		    android:layout_width="wrap_content"
		    android:layout_height="wrap_content"
		    android:background="@drawable/toast_normal"	//添加圆边的矩形背景
		    android:orientation="vertical"
		    android:padding="8dp" >
		
		    <TextView
		        android:id="@+id/toast_tv_address"
		        android:layout_width="wrap_content"
		        android:layout_height="wrap_content"
		        android:drawableLeft="@drawable/location"
		        android:gravity="center_vertical"
		        android:text="号码归属地" />
		
		</LinearLayout>

2. 新建AddressToast类（构造方法中的三个参数）

		private WindowManager mWM;
		private View mView;
		private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
		
		private Context mContext;
		
		public AddressToast(Context context) {
			this.mContext = context;
			//得到WindowManager对象
			mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	
			// 加载布局
			mView = View.inflate(context, R.layout.toast_address, null);
	
			// params参数
			final WindowManager.LayoutParams params = mParams;
			params.height = WindowManager.LayoutParams.WRAP_CONTENT;
			params.width = WindowManager.LayoutParams.WRAP_CONTENT;
			params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
					| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
			params.format = PixelFormat.TRANSLUCENT;
			// params.windowAnimations =
			// com.android.internal.R.style.Animation_Toast;
			params.type = WindowManager.LayoutParams.TYPE_TOAST;
			params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;	params.setTitle("Toast");
		}

3. 调用自定义Toast
	* 创建自定义的Toast,直接调用show()

###自定义Toast的优化
* 创建隐藏Toast的方法hide()
	
		public void hide() {
			if (mView != null) {
				if (mView.getParent() != null) {
					mWM.removeView(mView);
				}
				//把view设置为null
				mView = null;
			}
		}
* 调用hide()之后，下次再调用show()方法的时候会报NullPointException,所以也需要自定义show()方法

		public void show(String address) {
			// 加载布局
			if (mView == null) {
				mView = View.inflate(mContext, R.layout.toast_address, null);
			}
			//设置显示文本
			TextView tvAddress = (TextView) mView
					.findViewById(R.id.toast_tv_address);
			tvAddress.setText(address);
		
			mWM.addView(mView, mParams);
		}
	
