###Glide和Picasso的区别
* Glide和Picasso都是都是图片加载和缓存的库，两者在使用以及代码上都极其相似，不同的是两者的缓存策略
* Picasso使用的是将图片下载后不经压缩直接将图片整个缓存到磁盘中，当需要使用图片的时候，直接返回完整大小的图片，再根据ImageView的大小resize
* Glide首先会根据ImageView的大小适配图片，再将适配后的图片存储到磁盘中，如果有不同大小的ImageView加载同一张图片，Glide会以不同的分辨率缓存这张图片的两份不同的拷贝
* Glide可以加载GIF图，而Picasso不可以

###Glide的源码详解

	使用方法
	Glide  
	.with(context) .load("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg")  
	.into(ivImg);

* Activity/Fragment作为with()参数，图片加载会和Activity/Fragment的生命周期保持一致，比如onPaused状态的时停止加载，onResumed的时候自动重新加载，使用activit作为参数，当activity销毁的时候自动取消等待中的请求，但是使用其他context，可能就失去这种优化效果
	* 在listView中，当item已经滚出屏幕的范围，Glide会自动取消列表中的图片请求，因为Glide通过在ImageView上设置了一个tag，在加载另一张图片之前检查者个tag，如果存在就取消第一次请求

###Glide的总体设计
* RequestManager：请求管理，每个Activity都会创建一个RequestManager,根据对应activtiy的生命周期管理该Activity上所有的图片请求
* Engine：加载图片的引擎，根据这个Request创建EngineJob、DecodeJob
* EngineJob：图片加载
* DecodeJob：图片处理

####总体流程
* 请求加载图片的时候，创建RequestManager管理Request，会绑定Activity/Fragment,通过其生命周期管理Request，具体包含两个成员变量Lifecycle，用于监听avtivity/fragment的生命周期，RequestTracker，用于保存当前RequestManager所有的请求和待处=处理的请求。
	* 主要方法有：DrawableRequestBuilder通过DrawableRequestBuilder创建一个Request,这里主要配置加载图片的url、大小、动画、imageView对象、自定义图片处理接口
	* Request，主要是操作请求
	* Engine，请求的初始化
	* load，获取MemoryCache中缓存，首先创建当前Request的缓存key，通过key值从MemoryCache中获取缓存，判断缓存是否存在
	* EngineRunnable，主要功能请求资源，处理资源，缓存资源
	* DecodeJob
	* Transformation，处理资源，这里有BitmapPool，达到Bitmap复用
	* ResourceDecoder，用于将文件、IO流转化为Resource
