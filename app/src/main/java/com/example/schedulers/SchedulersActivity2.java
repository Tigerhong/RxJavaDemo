package com.example.schedulers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.example.R;

import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 该activity分析ObserveOn()，SubscribeOn()以及Delay(),timer()
 * 切换线程
 */
public class SchedulersActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        testobserveOn();
//        testsubscribeOn();
        Observable
                .just(0)
                .observeOn(getNamedScheduler("map之前的observeOn1"))
                .subscribeOn(getNamedScheduler("subscribeOn"))
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        threadInfo(".map()1-------"+integer);
                        return  integer + 1;
                    }
                })
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        threadInfo(".map()2--------"+integer);
                        return  integer + 1;
                    }
                })
                .subscribeOn(getNamedScheduler("subscribeOn1"))
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        threadInfo(".map()3--------"+integer);
                        return  integer + 1;
                    }
                })
                .doOnSubscribe(disposable -> threadInfo(".doOnSubscribe()-1"))
                .subscribeOn(getNamedScheduler("subscribeOn2")) // 多了这一行
                .doOnSubscribe(disposable -> threadInfo(".doOnSubscribe()-2"))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        threadInfo(".accept "+integer);
                    }
                });
    }

    private void testsubscribeOn() {
        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        threadInfo("OnSubscribe.call()");
                        e.onNext("RxJava");
                    }
                })
                .subscribeOn(getNamedScheduler("create之后的subscribeOn"))
                .doOnSubscribe(disposable -> threadInfo(".doOnSubscribe()-1"))
                //连续的subscribeOn，最终会切换到最近的subscribeOn线程
                .subscribeOn(getNamedScheduler("doOnSubscribe1之后的subscribeOn1"))
                .subscribeOn(getNamedScheduler("doOnSubscribe1之后的subscribeOn2"))
                .doOnSubscribe(disposable -> threadInfo(".doOnSubscribe()-2"))
                .subscribe(s -> {
                    threadInfo(".onNext()");
                    System.out.println(s + "-onNext");
                });
    }

    private void testobserveOn() {
        Observable.just("RxJava")
                .observeOn(getNamedScheduler("map之前的observeOn1"))
//                .observeOn(getNamedScheduler("map之前的observeOn2"))
// 连续的observeOn，最终会切换到最后面的observeOn线程上
                .map(s -> {
                    threadInfo(".map()-1");
                    return s + "-map1";
                })
                .map(s -> {
                    threadInfo(".map()-2");
                    return s + "-map2";
                })
                .observeOn(getNamedScheduler("subscribe之前的observeOn"))
                .subscribe(s -> {
                    threadInfo(".onNext()");
                    System.out.println(s + "-onNext");
                });
    }

    public static Scheduler getNamedScheduler(String name) {
        return Schedulers.from(Executors.newCachedThreadPool(r -> new Thread(r, name)));
    }

    public static void threadInfo(String caller) {
        System.out.println(caller + " => " + Thread.currentThread().getName());
    }
}
