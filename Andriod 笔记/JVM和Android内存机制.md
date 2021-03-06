##JVM
* 内存管理
* 垃圾回收
* 类加载
* 虚拟机性能监控和优化


### 内存管理
* 正常认为的内存区域包括：堆、栈，这种分法比较粗糙
* JVM较为详细地将内存划分为若干个不同的数据区

	* 程序计数器：当前线程所执行的字节码的行号指示器，就是记录正在执行的虚拟机字节码指令的行数
	* java虚拟机栈：我们所谓的“栈”，描述的是java方法执行的内存模型，一个方法对应一个栈帧，存储局部变量表等，一个方法的生命周期对应一个栈帧的进栈和弹栈
	* 本地方法栈：类似于虚拟机栈，区别就是本地方法栈为虚拟机使用的Native方法服务
	* java堆：所有的对象实例以及数组都要在堆上分配；堆可以再进行细分为新生代和老年代
	* 方法区：用于存储已被虚拟机加载的类信息、常量、静态变量等数据，相对于java堆，方法区又叫做永久区；其中有一部分是运行时常量池，用于存储编译期生成的各种字面量和符号引用

###垃圾回收
* 垃圾回收器会回收不再存活的对象，但是有没有想过回收器怎么判断该对象是否存活
* 早期的JVM中有一个计数算法，当有一个地方引用这个对象的时候，计数器加一，当引用失效的时候，计数器减一，任何时刻计数器为0时的对象都不能再被使用，但是现在主流的JVM里面都没有使用这个算法，因为它很难解决对象之间的相互调用的问题
* 判断对象是否存活，使用可达性分析算法，基本思路：通过一系列称为“GC Roots”的对象作为起始点，从这些节点开始向下搜索，将引用链作为其向下走的路径，，当一个对象没有任何引用链相连的时候，就说明这个对象是可回收的。

####垃圾收集算法
* 标记-清除算法：先标记在清除，不足：效率问题；空间问题
* 复制算法：将内存分为大小相等的两部分，，每次只使用其中一部分，当这一部分用完，将其存活的对象复制到另一块上面，再将使用过的内存一次性清除掉，这样会提高效率；
* 标记-整理算法：当对象存活率较高的时候，使用复制算法效率将变低，先标记在整理，标记后将所有存活的对象都向一端移动，最后一次清理边界以外的对象
* 分代收集算法：java堆中分为新生代和老年代，再根据不同区域的特点采用不同的清理算法，新生代中每次都有大批的对象死去，就选用复制算法，老年代中对象存活率较高，就可以使用标记-清除，标记-整理的算法

####垃圾收集器
* Serial收集器：这是一个单线程的收集器，最重要的是它在进行垃圾回收的过程中，其他工作的线程必须暂停，直到它收集结束。（stop the world）
* ParNew收集器：Serial收集器的多线程版本
* Parallel Scavenge 收集器：新生代收集器，使用复制算法的收集器，又是并行的多线程收集器
* Serial Old收集器：Serial收集器的老年代版本，也是单线程收集器
* Parallel Old收集器：Parallel Scavenge 收集器的老年代版本，使用多线程和“标记-整理”算法
* CMS收集器：一种以获取最短回收停顿时间为目标的收集器
* G1收集器：最新收集器，JDK1.7中采用的收集器，是一款面向服务端应用的垃圾回收器，具有下面特点：
	* 并行和并发
	* 分代收集
	* 空间整合
	* 可预测的停顿


###类加载
####类从被加载到虚拟机内存中开始，直到卸载出内存为止，它的整个生命周期包括了：加载、验证、准备、解析、初始化、使用和卸载 这七个阶段，其中验证、准备和解析这三个部分统称为连接（linking）
1. 加载
	* 使用类加载器完成，生成字节码文件，使用类加载器的时候，会有一个双亲委派模型
2. 验证
	* 文件格式验证
	* 元数据验证
	* 字节码验证
	* 符号引用验证
	* 验证对于类加载机制而言，并不是必要的阶段，如果所运行的代码是确保安全的
3. 准备
	* 为类的静态变量分配内存和将其初始化为默认值，这些内存都将在方法区中进行分配。
4. 解析
	* 虚拟机将常量池中的符号引用替换为直接引用的过程
	* 符号引用：一组符号来描述引用的对象
	* 直接引用：能够直接指向目标的指针，相对偏移量或是一个能间接定位到目标的句柄
5. 初始化
	* 初始化是类加载的最后一步，开始真正的执行类中定义的java程序代码。
	* 初始化阶段是执行类构造器<clinit>()方法的过程。这个方法是由编译器自动收集类中的所有变量的赋值动作和静态语句块中（static{}中的语句合并产生的）
6. 使用
7. 卸载

####双亲委派模型
* 类加载分类：启动类加载器、扩展类加载器、应用程序类加载器
* 双气委派模型的工作过程：一个类加载器收到类加载的请求，它首先不会自己尝试加载这个类，而是把这个请求委派给父类加载器完成，每个层次的类加载器都是这样，因此所有的加载请求都会传递到启动类加载器中，只有父加载器反馈自己无法完成这个加载请求时，子加载器才会尝试自己去加载。

###虚拟机性能监控与优化
####性能监控
* JDK的可视化工具
	* JConsole：java监视与管理控制台：可以监控内存、线程、类、CPU使用情况等4种信息的曲线图
	* VisualVM：多合一故障处理工具：可以做到显示虚拟机进程以及进程的配置；监视应用程序的CPU、GC等信息；方法级的程序运行性能分析；离线程序快照
