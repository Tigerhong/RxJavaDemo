package com.example.sample.imitation.rx.operation;

import com.example.sample.imitation.rx.Observable;
import com.example.sample.imitation.rx.Subscriber;
import com.example.sample.imitation.rx.functions.Fun;

/**
 * Description: MapOnSubscribe
 * Date: 2018/8/16
 *
 * @author zhengong.hong@ubtrobot.com
 */
public class MapOnSubscribe<T, R> implements Observable.OnSubscribe<R> {
    private Observable source;
    private Fun<T, R> transformer;

    public MapOnSubscribe(Observable<T> source, Fun<T, R> transformer) {
        this.source = source;
        this.transformer = transformer;
    }

    @Override
    public void call(Subscriber<R> subscriber) {
//        this.source.subscribe(new Subscriber<T>() {
//            @Override
//            public void onComplate() {
//                subscriber.onComplate();
//            }
//
//            @Override
//            public void onNext(T o) {
//                R apply = transformer.apply(o);
//                subscriber.onNext(apply);
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                subscriber.onError(t);
//            }
//        });
        MapSubscriber<T, R> mapSubscriber = new MapSubscriber<>(subscriber, transformer);
        this.source.subscribe(mapSubscriber);

    }

    class MapSubscriber<T, R> implements Subscriber<T> {

        private Subscriber<R> actual;
        private Fun transformer;

        public MapSubscriber(Subscriber<R> subscriber, Fun<T, R> transformer) {
            this.actual = subscriber;
            this.transformer = transformer;
        }

        @Override
        public void onComplate() {
            this.actual.onComplate();
        }

        @Override
        public void onNext(T t) {
            R r = (R) transformer.apply(t);
            this.actual.onNext(r);
        }

        @Override
        public void onError(Throwable t) {
            this.actual.onError(t);
        }
    }

}
