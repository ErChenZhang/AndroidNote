##Android提供Handler是用来满足线程间的通信,其内部实现一般涉及到这几个类：
* Handler：负责消息的发送和接受
* Looper： 一个线程可以产生一个Looper对象，由它来管理线程里的MessageQueue
* MessageQueue：存放线程放入的信息
* Thread：这里是之UIThread，就是主线程，Android启动程序的时会替它创建一个MessageQueue


###源码解析：
####Looper
* Handler的构造方法中，获取当前线程的Looper
* Looper的构造方法是私有的，创建一个Looper是调用Looper.prepare(),该方法会判断当前线程中有没有一个looper，有就报异常
* Looper创建之后，会调用Looper.loop()来启动Looper的循环,内部死循环通过queue.next()从MessageQueue中获取Message对象
* 再通过msg.target.dispatchMessage(msg),msg.target就是Handler对象，这里把Message发送给Handler进行处理
####MessageQueue
* Looper中的queue.next()就是不断从MessageQueue维护的链表中获取Message对象
* Handler中无论使用sendMessage还是post都调用sendMessageAtTime(),这个方法就调用enqueueMessage(),其中也是一个死循环，不断地将Message追加到MessageQueue链表中


##Asynctask
* 异步任务执行的顺序

1. execute(Params... params) 调用此方法，触发异步任务的执行 
2. onPreExecute() 在execute(Params... params)被调用之后立即执行，一般做一些准备工作
3. doInBackground(Params... params) 在onPreExecute()完成后立即执行，一般执行较为费时的操作，在执行过程中可以调用publishProgress(Progress... values)来更新进度信息。
4. onProgressUpdate(Progress... values) 在调用publishProgress(Progress... values)时，此方法被执行，直接将进度信息更新到UI组件上。
5. onPostExecute(Result result) 当后台操作结束时，此方法将会被调用，计算结果将做为参数传递到此方法中，直接将结果显示到UI组件上。

###源码解析
1. 首先从execute()开始，这里会判断当前Asynctask的状态，正在运行和已经完成的状态都会报IllegalStateException，保证了Asynctask只能执行一次，如果没有执行过，当前的Asynctask的状态会设置为RUNNING，接着会调用onRreExecute(),将AsyncTask的第一个参数赋值给mWorker.mParams，最后返回的还是AsyncTask
2. 接着看AsyncTask的构造方法，这里new了一个WorkRunnable的实现类，实现call方法，call方法中设置mTaskInvoked=true，Task设置为调用，最终再调用doInBcakgroud(mParams)方法，将其返回值作为参数传递给postRequest()
3. postResult()有一个异步消息加载机制，传递一个消息message，message.what为MESSAGE_POST_RESULT,message.object = new AsyncTaskResult(this,result),这个AsyncTaskResult就是一个简单的携带参数的对象
4. 有一个InternalHandler继承Handler，复写handleMessage等待消息传入并且对消息进行处理，接受消息后执行了AsyncTask.this.finish(result)
5. 再看finish()方法，中间判断isCancelled(),cancel()执行onCancelled()回调，正常情况下调用onPostExecute(result),主要这里调用是在handler的handleMessage中，所以在UI线程中，最后将状态置为FINISHED