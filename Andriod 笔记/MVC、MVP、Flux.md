###Flux -- 通常一个应用分成四个部分
* View：视图层
* Action：视图层发出的消息
* Store：数据层，通常用来存放应用的状态，一旦发生变动，就提醒Views要更新页面
* Dispatcher：派发器，用来接收Actions、执行回调函数

####数据总是“单向流动”的，任何相邻的部分都不会发生数据的“双向流动”。这保证了流程的清晰
1. 用户访问View
2. View发出用户的Action
3. Dispatcher接收到View发来的Action，对Store做相应的操作
4. Store操作之后会发生Action给View
5. View接收到Action后更新页面