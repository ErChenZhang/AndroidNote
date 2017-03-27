##网络框架Volley
* Google推出的网络请求的框架，它在网络请求上更加快速方便，另外在网络图片加载方面也提供了一些支持

###volley的工作方式：
* 创建不同的request，然后将它们添加到requestqueue中，一个项目只需要一个queue就足够了,可以将这个请求队列设成全局，在application类中初始化
* 将不同的格式的请求封装成不同的方法,通过网络监听的onResponse()返回对应的response
	* StringRequest
	
			StringRequest stringRequest = new StringRequest(Method.POST, url,  listener, errorListener) {  
			    @Override  
			    protected Map<String, String> getParams() throws AuthFailureError {  
			        Map<String, String> map = new HashMap<String, String>();  
			        map.put("params1", "value1");  
			        map.put("params2", "value2");  
			        return map;  
			    }  
			}; 
	* JsonObjectRequest
	* 网络图片加载
	* ImageRequest
		
			 public ImageRequest(java.lang.String url, 
								com.android.volley.Response.Listener<android.graphics.Bitmap> listener, //响应成功的回调
								int maxWidth,  //图片的最大宽度
								int maxHeight, //图片的最大高度
								android.graphics.Bitmap.Config decodeConfig, //图片的颜色属性，Bitmap.config 
								com.android.volley.Response.ErrorListener errorListener) { //响应失败的回调 
			 }
	
	
	* NetworkImageView
	
			mNetworkImageView.setImageUrl(IMAGE_URL, mImageLoader);
	
	* ImageLoader
		
			new ImageLoader(sRequestQueue, new SmartBJImageCache(MAX_CACHE_SIZE));//需要设置缓存

			SmartBJImageCache -- 需要extends LruCache<String, Bitmap> implements ImageLoader.ImageCache
								构造方法中参数设置缓存的最大空间
								sizeOf() 返回缓存图片的大小
								getBitmap()  返回LruCache中的图片
								putBitmap()  将图片加到缓存中		


###源码解析：整个工作流程
	
	Volley.newRequestQueue(Context context)

	方法中返回
	RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), network);
	最后调用的RequestQueue方法中中的 new ExecutorDelivery(new Handler(Looper.getMainLooper()))参数绑定了主线程，实际通过handler将子线程中的响应传递给主线程
		new DiskBasedCache(cacheDir)--磁盘缓存地址
		network--new BasicNetwork(stack)，通过HttpStack接口获取，HttpStack通过不同的SDK版本执行不同的网络库，
		SDK大于9选择HTTPUrlConnection,否则选择HttpClient，当然，通过改变HttpStack可以改变网络库，可以包括OKHttp等
	
	然后就是queue.start();
		创建CacheDispatcher缓存分发器和networkDispatcher网络分发器，networkDispatcher会根据线程池的的数量（默认为4个）创建个数
		Volley可以频繁进行网络请求也就是因为有几个网络分发器
		
		CacheDispatcher缓存分发器start()调用run(),内部有一个死循环，死循环内部会进行一系列的判断，其中包括isCanceled()，entry == null，entry.isExpired()（缓存是否过期）
		满足条件的就将request添加到netWorkQueue中，有缓存的request.parseNetworkResponse()解析得到网络response,最后通过postResponse()传递给主线程
		
		另一个NetworkDispatcher网络分发器 ，run()内部有mNetwork.performRequest(request)调用得到response，再parseNetworkResponse()解析响应，最后依旧是通过postResponse(request, response)方法将响应传递给主线程
		