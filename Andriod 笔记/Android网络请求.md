###网络图片下载
1. 确定网址
	>String path = "http://192.168.13.13:8080/dd.jpg";
	> File file = new File(getCacheDir(), getFileName(path));
2. 将网址封装成一个url对象
	> Url url = new URL(path);
3. 获取一个客户端与服务端之间的连接对象，但是这时候还没有建立连接
    > HttpURLConnction conn = (HttpURLConnection)url.openConnection();
4. 对连接对象进行初始化
	* 设置请求方法，要大写（"GET"、"POST"）
	> conn.setReuquestMethod("GET")
	* 设置连接超时
	> conn.setConnectTimeOut(5000)
	* 设置读取超时
	> conn.setReadTimeOut(5000)
5. 发送请求，与服务器建立连接
	> conn.connection();
6. 判断连接是否成功
	* 如果响应码为200，说明请求成功
	> if(conn.getResponseCode()==200){
	* 获取服务器响应头中的流，流中的数据就是客户端请求的数据
	> InputStream is = conn.getInputStream();
	* 读取服务器中返回的流里的数据，把数据写到本地文件，缓存起来
	>     FileOutputStream fos = new FileOutputStream(file)
    	 byte[] b = new byte[1024];
    	 int len = 0;
    	 while((len=is.read(b)!=0)){
    	 fos.write(b)
    	 }
    	 fos.close;
	* 读取流中的数据，并构成位图对象
	> Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath())

#网络编程
##主线程阻塞（用户体验十分的差）
* 体现：停止UI刷新，应用无法响应用户操作
* 要求：一些耗时操作不应放在主线程上
* ANR：Answer not responding
	* 应用无响应异常
	* 主线程阻塞时间过长，就会抛出ANR
*主线程也叫UI线程，只有在主线程中才能刷新UI

## 消息队列机制
* 主线程创建的时候，系统会同时创建消息队列对象（MessageQueue）和消息轮询器对象（Looper），两者都是系统自动创建的，不需要用户手动创建

