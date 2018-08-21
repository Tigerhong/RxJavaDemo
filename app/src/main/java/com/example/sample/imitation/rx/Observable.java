package com.example.sample.imitation.rx;

import com.example.sample.imitation.rx.functions.Action1;
import com.example.sample.imitation.rx.functions.Fun;
import com.example.sample.imitation.rx.operation.MapOnSubscribe;

/**
 * Description: Observable
 * Date: 2018/8/16
 * 被观察者
 *
 * @author zhengong.hong@ubtrobot.com
 */
public class Observable<T> {
    private OnSubscribe onSubscribe;

    public Observable(OnSubscribe onSubscribe) {
        this.onSubscribe = onSubscribe;
    }

    /**
     * 这个接口是为了将Observable和Subscriber连接起来
     * 就是观察者自己   将自己   注册给了   观察者Observable
     * <p>
     * 虽然是观察者Subscriber订阅了被观察者Observable
     * 其实就是将观察者自己将自己注册给了观察者Observable
     * 当观察者Observable做了一系列动作话，通过观察者的回调
     * ，来告诉观察者，被观察者Observable做了什么
     */
    public interface OnSubscribe<T> {
        /**
         * Invoked when Observable.subscribe is called. 意思是 当Observable被订阅(subscribe)
         * OnSubscribe接口的call方法会被执行。
         *
         * @param subscriber
         */
        void call(Subscriber<T> subscriber);
    }

    public Observable map(Fun f) {
        return create(new MapOnSubscribe(this, f));
    }

    public static Observable create(OnSubscribe onSubscribe) {
        return new Observable(onSubscribe);
    }

    public void subscribe(Action1<T> action1) {

        Subscriber<T> subscriber = new Subscriber<T>() {
            @Override
            public void onComplate() {

            }

            @Override
            public void onNext(T t) {
                action1.call(t);
            }

            @Override
            public void onError(Throwable t) {

            }
        };
        subscribe(subscriber);

    }

    /**
     * 订阅操作
     *
     * @param subscriber
     */
    public void subscribe(Subscriber subscriber) {
        //subscriber订阅Observable后，调用OnSubscribe.call实现真正的订阅操作
        onSubscribe.call(subscriber);
    }
}
