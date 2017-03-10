* 当我们退出Activity的时候，AsyncTask可能还在执行
* 我们想在activity退出，关闭AsyncTask，想到了它的cancel()方法
* 参数为true的时候，AsyncTask中的线程正在运行也要被关闭；false等到线程执行完再取消
* 但是事实证明，调用这个cancel方法并没有关闭Asynctask

###那么问题就来了，既然叫cancel为什么不能取消Asynctask呢

* 查看源码可以看到
* 调用cancel(boolean)就会间接调用iscancelled(),返回true;

		 public final boolean cancel(boolean mayInterruptIfRunning) {
	        mCancelled.set(true);
	        return mFuture.cancel(mayInterruptIfRunning);
	    }

* 其实这个方法是给Asynctask设置了"canceled"状态，也就是说让线程池中的某个控制状态的的标志位变为CANCEL状态
* 然后在onProgressUpdate()、doInBackground()这两个方法中，刚开始的时候会判断线程中是否取消状态，true的话return

* 这也就是为什么我们调用cancel()，AsyncTask没有取消的原因
 