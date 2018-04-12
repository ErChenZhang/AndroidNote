##View的post()和postDelayed()方法详解

###子线程中不能对UI进行操作，这我们都知道，但是可以通过view.post(Runnable)的方式进行UI操作，这是为什么呢？我们通过分析源码的方式来了解一下


	public boolean post(Runnable action) {
        final AttachInfo attachInfo = mAttachInfo;
        if (attachInfo != null) {
            return attachInfo.mHandler.post(action);
        }

        // Postpone the runnable until we know on which thread it needs to run.
        // Assume that the runnable will be successfully placed after attach.
        getRunQueue().post(action);
        return true;
    }


view的post()将Runnable action传递给attachInfo.Handler,所以其本质都是Handler、Looper、MessageQueue、Message的异步消息处理机制，接着再查看mAttachInfo的来源，源码定位到

 	void dispatchAttachedToWindow(AttachInfo info, int visibility) {
        mAttachInfo = info;
		...
	}

####在view的绘制过程中，第一次会调用DecorView的dispatchAttachedToWindow，所以这也就解释了view.post()可以解决在oncreate()中获取view的宽高为0的问题

` getRunQueue().post(action); `当attachInfo为空时会走此方法，getRunQueue()的源码：


	private HandlerActionQueue getRunQueue() {
        if (mRunQueue == null) {
            mRunQueue = new HandlerActionQueue();
        }
        return mRunQueue;
    }

再看HandlerActionQueue的源码：

	public void post(Runnable action) {
        postDelayed(action, 0);
    }

    public void postDelayed(Runnable action, long delayMillis) {
        final HandlerAction handlerAction = new HandlerAction(action, delayMillis);

        synchronized (this) {
            if (mActions == null) {
                mActions = new HandlerAction[4];
            }
            mActions = GrowingArrayUtils.append(mActions, mCount, handlerAction);
            mCount++;
        }
    }

HandlerActionQueue会将runnable缓存起来，最大缓存数为4，缓存起来的Runnable返回到上面的dispatchAttachedToWindow中会被调用