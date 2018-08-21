package com.example.sample.imitation.rx;

/**
 * Description: Subscriber
 * Date: 2018/8/16
 *订阅者/观察者
 * @author zhengong.hong@ubtrobot.com
 */
public interface Subscriber<T> {
    /**
     * 完成的动作
     */
    void onComplate();

    /**
     * 发送事件
     * @param t
     */
    void onNext(T t);

    /**
     *错误的动作
     * @param t
     */
    void onError(Throwable t);
}
