##OKHttp --所有的网络通信的核心任务就是Client端和Server端之间进行数据的交互和操作
1. 有什么用
2. 怎么用
3. 为什么要用

###流程图
![](img/12.jpg)

---
###OkHttpClinet 流程的总控制者
![](img/13.jpg)

###简单使用

- 一般的get请求
- 一般的post请求
- 基于Http的文件上传
- 文件下载
- 加载图片
- 支持请求回调，直接返回对象、对象集合
- 支持session的保持

1.get请求的步骤
	
	1、发送一个get请求的步骤，首先新建一个OkHttpClient对象，再构造一个Request对象，参数最起码有个url，当然你可以通过Request.Builder设置更多的参数比如：header、method等。

    2、然后通过request的对象去构造得到一个Call对象，类似于将你的请求封装成了任务，既然是任务，就会有execute()（同步的方式执行）和cancel()等方法。

  	3、最后，我们希望以异步的方式去执行请求，所以我们调用的是call.enqueue，将call加入调度队列，然后等待任务执行完成，我们在Callback中即可得到结果。

		//创建okHttpClient对象
		OkHttpClient mOkHttpClient = new OkHttpClient();
		//创建一个Request
		final Request request = new Request.Builder()
		                .url("https://github.com/hongyangAndroid")
		                .build();
		//new call
		Call call = mOkHttpClient.newCall(request); 
		//请求加入调度
		call.enqueue(new Callback()
        {
            @Override
            public void onFailure(Request request, IOException e)
            {
            }

            @Override
            public void onResponse(final Response response) throws IOException
            {
                    //String htmlStr =  response.body().string();
            }
        });

2.post携带参数的请求 通过FormEncodingBuilder添加多个键值对，构造一个RequestBody，再通过request的post(出入请求体)
	
		Request request = buildMultipartFormRequest(
		        url, new File[]{file}, new String[]{fileKey}, null);
		FormEncodingBuilder builder = new FormEncodingBuilder();   
		builder.add("username","张鸿洋");
		
		Request request = new Request.Builder()
		                   .url(url)
		                .post(builder.build())
		                .build();
		 mOkHttpClient.newCall(request).enqueue(new Callback(){});
	
3.基于Http文件的上传 -- MultipartBuilder 
	
		File file = new File(Environment.getExternalStorageDirectory(), "balabala.mp4");
		
		RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
		
		RequestBody requestBody = new MultipartBuilder()
		     .type(MultipartBuilder.FORM)
		     .addPart(Headers.of(
		          "Content-Disposition", 
		              "form-data; name=\"username\""), 
		          RequestBody.create(null, "张鸿洋"))
		     .addPart(Headers.of(
		         "Content-Disposition", 
		         "form-data; name=\"mFile\"; 
		         filename=\"wjd.mp4\""), fileBody)
		     .build();
		
		Request request = new Request.Builder()
		    .url("http://192.168.1.103:8080/okHttpServer/fileUpload")
		    .post(requestBody)
		    .build();
		
		Call call = mOkHttpClient.newCall(request);
		call.enqueue(new Callback()
		{
		    //...
		});	

4.文件下载和图片的加载

	一般都在onResponse()回调中通过获取response的bytes[]或者inputstream，再通过decode方法去解码获取想要的数据


##源码解析

## 同步请求
	* newCall--实际是调用realCall
	
			protected RealCall(OkHttpClient client, Request originalRequest) {  
			    this.client = client;    
			    this.originalRequest = originalRequest;    
			    this.retryAndFollowUpInterceptor = new RetryAndFollowUpInterceptor(client);	
			}
	* excuate()--先判断这个请求是否执行了，然后通过getResponseWithInterceptorChain()获取响应
			
				@Override
		   	 public Response execute() throws IOException {
		        synchronized (this) {
		            if (executed) throw new IllegalStateException("Already Executed"); //(1)
		            executed = true;
		        }
		        try {
		            client.dispatcher.executed(this);//(2)
		            Response result = getResponseWithInterceptorChain();//(3)
		            if (result == null) throw new IOException("Canceled");
		            return result;
		        }finally {
		            client.dispatcher.finished(this);//(4)
		        }
		    }

	* getResponseWithInterceptorChain()--使用拦截器的责任链获取响应，然后proceed()开始执行链式拦截器
		
				//将拦截器链式加入，通过链式调用
				//拦截器的作用：拦截器是OkHttp框架提供的对http的请求和响应进行同意处理的机制
				//多个拦截器还可以连接形成一个链再使用，拦截器会按照链式上的顺序依次执行
				//拦截器会先对请求进行修改，然后获取响应之后也会对响应进行修改
			 private Response getResponseWithInterceptorChain() throws IOException {
			     // Build a full stack of interceptors.
			     List<Interceptor> interceptors = new ArrayList<>();
			     interceptors.addAll(client.interceptors());     //(1)
			     interceptors.add(retryAndFollowUpInterceptor);    //(2)
			     interceptors.add(new BridgeInterceptor(client.cookieJar()));    //(3)
			     interceptors.add(new CacheInterceptor(client.internalCache()));    //(4)
			     interceptors.add(new ConnectInterceptor(client));    //(5)
			     if (!retryAndFollowUpInterceptor.isForWebSocket()) {
			         interceptors.addAll(client.networkInterceptors());    //(6)
			     }
			     interceptors.add(new CallServerInterceptor(
			             retryAndFollowUpInterceptor.isForWebSocket()));     //(7)
			
			     Interceptor.Chain chain = new RealInterceptorChain(
			             interceptors, null, null, null, 0, originalRequest);
			     return chain.proceed(originalRequest); //  <<=========开始链式调用
			 }
	
	* proceed()--实例化一个RealIterceptorChain对象，在一个存放Interceptor的ArrayList中获取当前的Interceptor,
		当前的Interceptor.intercept()方法调用RealIterceptorChain，并传递
			
			   public Response proceed(Request request, StreamAllocation streamAllocation, HttpCodec httpCodec,
			     Connection connection) throws IOException {
			    if (index >= interceptors.size()) throw new AssertionError();
			
			    calls++;
			
			    // If we already have a stream, confirm that the incoming request will use it.
			    //如果我们已经有一个stream。确定即将到来的request会使用它
			    if (this.httpCodec != null && !sameConnection(request.url())) {
			      throw new IllegalStateException("network interceptor " + interceptors.get(index - 1)
			          + " must retain the same host and port");
			    }
			
			    // If we already have a stream, confirm that this is the only call to chain.proceed().
			    //如果我们已经有一个stream， 确定chain.proceed()唯一的call
			    if (this.httpCodec != null && calls > 1) {
			      throw new IllegalStateException("network interceptor " + interceptors.get(index - 1)
			          + " must call proceed() exactly once");
			    }
			
			    // Call the next interceptor in the chain.
			    //调用链的下一个拦截器
			    RealInterceptorChain next = new RealInterceptorChain(
			        interceptors, streamAllocation, httpCodec, connection, index + 1, request);
			    Interceptor interceptor = interceptors.get(index);
			    Response response = interceptor.intercept(next);
			
			    // Confirm that the next interceptor made its required call to chain.proceed().
			    if (httpCodec != null && index + 1 < interceptors.size() && next.calls != 1) {
			      throw new IllegalStateException("network interceptor " + interceptor
			          + " must call proceed() exactly once");
			    }
			
			    // Confirm that the intercepted response isn't null.
			    if (response == null) {
			      throw new NullPointerException("interceptor " + interceptor + " returned null");
			    }
			
			    return response;
			  }

###OkHttp中一些常见的Interceptor
* 拦截器使用了 责任链设计模式，网络请求Request会在拦截器链中进行一层层的传递并对请求进行处理，如果哪个拦截器得到响应，就停止传递，响应再根据拦截器链往上一层层传递并处理，最后传递到newCall的execute得到response

1、RetryAndFollowUpInterceptor--负责失败重试和重定向
	
		@Override 
		public Response intercept(Chain chain) throws IOException {
	        Request request = chain.request();
	        streamAllocation = new StreamAllocation(
	                client.connectionPool(), createAddress(request.url()));
	        int followUpCount = 0;
	        Response priorResponse = null;
	        while (true) {
	            if (canceled) {
	                streamAllocation.release();
	                throw new IOException("Canceled");
	            }
	
	            Response response = null;
	            boolean releaseConnection = true;
	            try {
	                response = ((RealInterceptorChain) chain).proceed(request, streamAllocation, null, null); //(1)
	                releaseConnection = false;
	            } catch (RouteException e) {
	                // The attempt to connect via a route failed. The request will not have been sent.
	                //通过路线连接失败，请求将不会再发送
	                if (!recover(e.getLastConnectException(), true, request)) throw e.getLastConnectException();
	                releaseConnection = false;
	                continue;
	            } catch (IOException e) {
	                // An attempt to communicate with a server failed. The request may have been sent.
	                // 与服务器尝试通信失败，请求不会再发送。
	                if (!recover(e, false, request)) throw e;
	                releaseConnection = false;
	                continue;
	            } finally {
	                // We're throwing an unchecked exception. Release any resources.
	                //抛出未检查的异常，释放资源
	                if (releaseConnection) {
	                    streamAllocation.streamFailed(null);
	                    streamAllocation.release();
	                }
	            }
	
	            // Attach the prior response if it exists. Such responses never have a body.
	            // 附加上先前存在的response。这样的response从来没有body
	            // TODO: 2016/8/23 这里没赋值，岂不是一直为空？
	            if (priorResponse != null) { //  (2)
	                response = response.newBuilder()
	                        .priorResponse(priorResponse.newBuilder()
	                                .body(null)
	                                .build())
	                        .build();
	            }
	
	            Request followUp = followUpRequest(response); //判断状态码 (3)
	            if (followUp == null){
	                if (!forWebSocket) {
	                    streamAllocation.release();
	                }
	                return response;
	            }
	
	            closeQuietly(response.body());
	
	            if (++followUpCount > MAX_FOLLOW_UPS) {
	                streamAllocation.release();
	                throw new ProtocolException("Too many follow-up requests: " + followUpCount);
	            }
	
	            if (followUp.body() instanceof UnrepeatableRequestBody) {
	                throw new HttpRetryException("Cannot retry streamed HTTP body", response.code());
	            }
	
	            if (!sameConnection(response, followUp.url())) {
	                streamAllocation.release();
	                streamAllocation = new StreamAllocation(
	                        client.connectionPool(), createAddress(followUp.url()));
	            } else if (streamAllocation.codec() != null) {
	                throw new IllegalStateException("Closing the body of " + response
	                        + " didn't close its backing stream. Bad interceptor?");
	            }
	
	            request = followUp;
	            priorResponse = response;
	        }
	    }

2、BridgeInterceptor--通过将用户构建的请求转换成发送到服务器的请求，以及将服务器返回的响应转换成用户友好的响应

3、ConnectInterceptor--建立连接

4、NetworkInterceptors--配置OkHttpClient时的设置

5、CallServerInterceptor--发送和接受数据 


##异步请求
* 异步get请求方式--区别于同步请求放在一个请求队列中
	
		 private final OkHttpClient client = new OkHttpClient();
		
		  public void run() throws Exception {
		    Request request = new Request.Builder()
		        .url("http://publicobject.com/helloworld.txt")
		        .build();
		
		    client.newCall(request).enqueue(new Callback() {
		      @Override 
		      public void onFailure(Call call, IOException e) {
		        e.printStackTrace();
		      }
		
		      @Override 
		      public void onResponse(Call call, Response response) throws IOException {
		        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
		
		        Headers responseHeaders = response.headers();
		        for (int i = 0, size = responseHeaders.size(); i < size; i++) {
		          System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
		        }
		
		        System.out.println(response.body().string());
		      }
		    });
		  }

* enqueue(回调) --判断是否执行，没有执行就进入请求队列中

		@Override 
	    public void enqueue(Callback responseCallback) {
	        synchronized (this) {
	            if (executed) throw new IllegalStateException("Already Executed");
	            executed = true;
	        }
	        client.dispatcher().enqueue(new AsyncCall(responseCallback));
	    }

* dispatcher类中的enqueue()--首先判断runningAsynCalls是否已经满了，不满就将call添加到runningAsynCalls中,并利用线程池去执行call,
  如果runningAsynCalls已经满了，就将call添加到readyAsyncCalls

		synchronized void enqueue(AsyncCall call) {
	        if (runningAsyncCalls.size() < maxRequests && runningCallsForHost(call) < maxRequestsPerHost) {
	            runningAsyncCalls.add(call);
	            executorService().execute(call);
	        } else {
	            readyAsyncCalls.add(call);
	        }
	    }

* runningAsyncCalls和readyAsyncCalls--都是双端队列（支持两端插入和移除的队列）
	
		/** Ready async calls in the order they'll be run. */
		private final Deque<AsyncCall> readyAsyncCalls = new ArrayDeque<>(); //正在准备中的异步请求队列
		
		/** Running asynchronous calls. Includes canceled calls that haven't finished yet. */
		private final Deque<AsyncCall> runningAsyncCalls = new ArrayDeque<>(); //运行中的异步请求
		
		/** Running synchronous calls. Includes canceled calls that haven't finished yet. */
		private final Deque<RealCall> runningSyncCalls = new ArrayDeque<>(); //同步请求
		
* 内部类AsynCall--执行execute(),实际调用Response response = getResponseWithInterceptorChain()获得response,都是使用拦截器，和同步请求的方式一样

		final class AsyncCall extends NamedRunnable {
		    private final Callback responseCallback;
		
		    private AsyncCall(Callback responseCallback) {
		        super("OkHttp %s", redactedUrl());
		        this.responseCallback = responseCallback;
		    }
		
		    String host() {
		        return originalRequest.url().host();
		    }
		
		    Request request() {
		        return originalRequest;
		    }
		
		    RealCall get() {
		        return RealCall.this;
		    }
		
		    @Override protected void execute() {
		        boolean signalledCallback = false;
		        try {
		            Response response = getResponseWithInterceptorChain();
		            if (retryAndFollowUpInterceptor.isCanceled()) {
		                signalledCallback = true;
		                responseCallback.onFailure(RealCall.this, new IOException("Canceled"));
		            } else {
		                signalledCallback = true;
		                responseCallback.onResponse(RealCall.this, response);
		            }
		        } catch (IOException e) {
		            if (signalledCallback) {
		                // Do not signal the callback twice!
		                Platform.get().log(INFO, "Callback failure for " + toLoggableString(), e);
		            } else {
		                responseCallback.onFailure(RealCall.this, e);
		            }
		        } finally {
		            client.dispatcher().finished(this);
		        }
		    }
		}
