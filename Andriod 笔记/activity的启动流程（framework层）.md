##启动流程：

1. 调用者进程通过AMS这个Framework端Binder将启动另一个Activity的信息传给SystemServer进程。
2. ActivityStarter处理了这些intent和flag等信息之后，然后交给ActivityStackSupervisior/ActivityStack去处理被调用进程的Activity进栈。如果被调用者进程存在，就会使用ApplicationThread这个Application端Binder通知已存在的被调用者进程启动Activity。如果被调用者进程不存在，就会使用Socket通知Zygote进程fork出一个进程，用来承载即将启动的Activity。
3. 在新的进程里面会创建ActivityThread对象，完成开启主线程loop、ApplicationThread依附在AMS、初始化Context、Application等工作，并且通过Classload加载Activity，创建Activity对象，完成Activity生命周期的调用。