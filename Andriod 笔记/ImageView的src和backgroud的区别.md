##src和background的区别
* 在imageVIew的XML属性中有两个属性src和background
* src存放的是图片资源，background是背景

* 但是两者还是有一些区别的
* 首先两者在代码中设置图片的方法不同

		//background
		mImageView.setBackground();
		mImageView.setBackgroundResource();
		mImageView.setBackgroundColor();
		mImageView.setBackgroundDrawable();
		
		//src
		mImageView.setImageResource();
		mImageView.setImageDrawable();
		mImageView.setImageURI();

* 其次，是src和background之间的一些区别
>
1.src存放的是原图大小， background会根据组件的大小进行拉伸。
>
2.src是图片的内容（画），background是图片的背景（画框），可以同时存在。
>
3.background可以设置透明度，src不可以。
>
4.scaleType只对src起作用。scaleType见最下

>
5.自定义ImageView中画图的时候使用setBackgroundDrawable。如果使用setImageDrawable，需要设置setIntrinsicHeight和setIntrinsicWidth，不然不会显示的，具体原因去看看源码吧。


##scaleType
	CENTER /center 在视图中心显示图片，并且不缩放图片

	CENTER_CROP / centerCrop 按比例缩放图片，使得图片长 (宽)的大于等于视图的相应维度
	
	CENTER_INSIDE / centerInside 按比例缩放图片，使得图片长 (宽)的小于等于视图的相应维度
	
	FIT_CENTER / fitCenter 按比例缩放图片到视图的最小边，居中显示
	
	FIT_END / fitEnd 按比例缩放图片到视图的最小边，显示在视图的下部分位置
	
	FIT_START / fitStart 把图片按比例扩大/缩小到视图的最小边，显示在视图的上部分位置
	
	FIT_XY / fitXY 把图片不按比例缩放到视图的大小显示
	
	MATRIX / matrix 用矩阵来绘制