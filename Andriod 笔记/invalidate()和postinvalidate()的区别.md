##invalidate、postInvalidate
* 两者都是刷新界面的方法
* 原理：将UI线程中之前旧的view删掉，重新创建一个view

###区别
* invalidate()只能在UI线程中调用，也符合主线程更新UI的原则
* postInvalidate()可以直接在子线程中刷新UI，源码如下:

		
			/**
			*This method can be invoked from outside of the UI thread
 			* only when this View is attached to a window
			*/
		  public void postInvalidate() {
       		 postInvalidateDelayed(0);
   		  }
	
	   
	     public void postInvalidateDelayed(long delayMilliseconds) {
	        final AttachInfo attachInfo = mAttachInfo;
	        if (attachInfo != null) {
	            attachInfo.mViewRootImpl.dispatchInvalidateDelayed(this, delayMilliseconds);
	        }
    	 }
	
* postInvalidate()是当View加载到window中的时候可以在UI线程外更新界面，再往下的dispatchInvalidateDelayed方法就是封装不可见得了
