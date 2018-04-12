##Java调用JavaScript
* 直接使用WebView的loadUrl(url)方法，可能还需要进行一些设置，getSetting()获得WebSettings，然后用setJavaScriptEnabled(true)使WebView上能显示JavaScript


##JavaScript调用Java，归为两类：
* WebView api本身就支持的方式addJavaScriptInterface
	* 要在js调用的方法上面加入注解@JavascriptInterface
	* addJavaScriptInterface()方法将参数中提供的Java对象(object)注入到WebView中
* 通过伪协议拦截页面的去“请求”，即需要JavaScript与Java端事先约定，方法有shouldOverrideUrlLoading()、window.prompt、Console.log和alert,通常称这种方式叫做JSBridge