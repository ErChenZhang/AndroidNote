##网络编程

* Android中设计到访问网络有六种方式：
	* 针对TCP/IP的Socket、ServerSocket
	* 针对UDP的DatagramSocket、DatagramPackage
	* 针对直接URL的HttpURLConnection
	* Google集成了Apache HTTP客户端，可使用HTTP进行网络编程
	* 使用WebService
	* 直接使用WebView视图组件显示网页。基于WebView进行开发，Google已经提供了一个基于

###Socket 套接字
* 网络上的两个程序通过一个双向的通信连接实现数据的交换，这个连接的一端成为一个socket
* socket提供一些操作对应的函数接口，包括打开-读写-关闭

####socket()构造函数
* socket函数对应于普通文件的打开操作 -- 参数设定其协议域、socket类型、传输协议
	
		//服务端
		//1、创建一个服务器端Socket，即ServerSocket，指定绑定的端口，并监听此端口
		ServerSocket serverSocket =newServerSocket(10086);//1024-65535的某个端口
		//2、调用accept()方法开始监听，等待客户端的连接
		Socket socket = serverSocket.accept();
		//3、获取输入流，并读取客户端信息
		InputStream is = socket.getInputStream();
		InputStreamReader isr =newInputStreamReader(is);
		BufferedReader br =newBufferedReader(isr);
		String info =null;
		while((info=br.readLine())!=null){
		System.out.println("Hello,我是服务器，客户端说："+info)；
		}
		socket.shutdownInput();//关闭输入流
		//4、获取输出流，响应客户端的请求
		OutputStream os = socket.getOutputStream();
		PrintWriter pw = new PrintWriter(os);
		pw.write("Hello World！");
		pw.flush(); 
		//5、关闭资源
		pw.close();
		os.close();
		br.close();
		isr.close();
		is.close();
		socket.close();
		serverSocket.close();
***
		//客户端
		//1、创建客户端Socket，指定服务器地址和端口
		Socket socket =newSocket("127.0.0.1",10086);
		//2、获取输出流，向服务器端发送信息
		OutputStream os = socket.getOutputStream();//字节输出流
		PrintWriter pw =newPrintWriter(os);//将输出流包装成打印流
		pw.write("用户名：admin；密码：admin");
		pw.flush();
		socket.shutdownOutput();
		//3、获取输入流，并读取服务器端的响应信息
		InputStream is = socket.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String info = null;
		while((info=br.readLine())!null){
		 System.out.println("Hello,我是客户端，服务器说："+info);
		}
		  
		//4、关闭资源
		br.close();
		is.close();
		pw.close();
		os.close();
		socket.close();  


