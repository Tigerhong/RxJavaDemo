# RxJavaDemo
学习rxjava
Rxjava1+版的

Observable在使用的过程中，操作符从上往下被使用（调用）最终通过Observable.subscibe()订阅Subscriber，
便从下往上通过各个OnSubscribe（各个OnSubscribe指的是不同操作符产生的Observable里的OnSubscribe）发起通知（订阅）
然后事件就可以从上往下发送了
