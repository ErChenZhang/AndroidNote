###打包的流程概述-8个步骤
1. 通过aapt.exe生成R.java文件
2. 处理aidl文件，生成相应java文件
3. javac编译工程源代码，生成相应class文件
4. 解压第三方jar包
5. 通过dx.bat转换所有class文件，生成classes.dex文件
6. aapt生成资源文件包工具
7. 用apkbuilder打包生成未签名的apk
8. 对apk文件进行签名
 

###使用最多的打包方式一：Gradle方式
* 使用友盟SDK
* 在清单文件中定义占位符
* 再在module中的gradle脚本中替换占位符，可以添加更多的渠道
* 最后使用Android studio的Build的generate Signed APK生成签名APK

###美团提供的打包思路-如果可以直接修改apk的渠道号，而不需要再重新签名就能节省不少打包时间
* 通过为不同渠道的应用添加不同的空文件，可以唯一标识一个渠道
* 然后在代码中读取空渠道文件名
* 具体步骤：
	* 首先要手动打一个签名包
	* 拷贝ChannelUtil.java到项目中
	* 代码中通过Channelutil.getChannel(this)方法获取渠道号
	* 安装Python
	* `..PythonTool\info\channel.txt`,里面去添加自己想要的渠道
	* 放置apk到MultiChannelBuildTool.py所在目录中，双击MultiChannelBuildTool.py执行脚本，完成多渠道打包


###apk瘦身
* apk瘦身主要优化 classes.dex、lib、资源文件这三类
	* classes.dex：通过代码混淆，删掉不必要的jar包和代码实现该文件的优化
	* lib：一个硬件设备对应一个架构（mips、arm、x86），只保留与设备架构相关的库文件夹，这样可以大大降低lib文件夹的大小
	* 资源文件：通过Lint工具扫描代码中没有使用到的静态资源

* 进一步优化：
	* 多分辨率适配
	* 预置数据，像游戏一样，程序和数据分离，进入模块时下载预置数据
	* 图片资源优化
		* 图片优化原则是：在不降低图片效果、保证APK显示效果的前提下缩小图片文件的大小
		* 使用tinypng优化大部分图片资源，tinypng是压缩png和jpg图片格式的网站，可以实现在无损压缩的情况下把图片大小缩小原来的30%-50%，但是他有个缺点，压缩某些带有过渡效果（带alpha值）的图片时，图片会失真
		* 使用webP图片格式。这种格式是谷歌研发出来的一种图片数据格式，支持有损压缩和无损压缩的图片文件格式，WebP相比于png最明显的问题就是加载稍慢，但是日益提高的硬件配置弥补可这个缺陷