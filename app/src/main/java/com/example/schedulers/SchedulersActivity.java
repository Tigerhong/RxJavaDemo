package com.example.schedulers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.example.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 该activity分析ObserveOn()，SubscribeOn()以及Delay(),timer()
 * 切换线程
 */
public class SchedulersActivity extends AppCompatActivity {
    private static final String TAG = "SchedulersActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_demo);
        Log.i(TAG, "timer执行前: " + "   当前线程:" + Thread.currentThread().getName());
//        testThread1();
//        onlyUseObserveOn();
//        onlyUseSubscribeOn();
//        useSubscribeOnAndObserveOn();
        userDelayAndSubscribeOnAndObserveOn();
    }

    /**
     * 当使用delay延迟操作符的时
     * 使用delay延迟操作时
     * 可以将其以下的操作符所运行的环境
     * 切换到delay指点延迟的线程中
     * delay延迟时调度线程时，和observeOn调度线程一样
     * 都可以更改它一下操作符锁运行的线程。
     * delay和observeOn连用时，哪个后执行，线程就切换到哪个指定的线程环境中
     * delay和subscribeOn连用时，就和subscribeOn和observeOn连用一样
     * 不管先后，线程都会被调度到delay指定的线程中
     */
    private void userDelayAndSubscribeOnAndObserveOn() {
        Observable
                .just(new Long(123))
                .delay(1,TimeUnit.SECONDS,Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())//No1
                .flatMap(new Function<Long, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Long aLong) throws Exception {
                        Log.i(TAG, "timer执行中: 0" + "   当前线程:" + Thread.currentThread().getName());
                        return Observable.just("我跳出来了");
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())//No2
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.i(TAG, "timer执行中: 1" + "   当前线程:" + Thread.currentThread().getName());
                        return s + 1;
                    }
                })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.i(TAG, "timer执行中: 2" + "   当前线程:" + Thread.currentThread().getName());
                        return s + 1;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())//No3
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.i(TAG, "timer执行中: 3" + "   当前线程:" + Thread.currentThread().getName());
                        return s + 1;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())//No4
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.i(TAG, "timer执行中: 4" + "   当前线程:" + Thread.currentThread().getName());
                        return s + 1;
                    }
                })
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())//No5
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, "timer执行后: " + "   当前线程:" + Thread.currentThread().getName());
                        Log.i(TAG, "timer执行后: " + s);
                    }
                });
    }

    /**
     * SubscribeOn和ObserveOn一起用时
     * 当 .observeOn().subscribeOn()连着一起用的时候，
     * 不管是 .observeOn().subscribeOn()或者是 .subscribeOn().observeOn()
     * 在其下面的操作符操作的线程都会被调度到observeOn()调度的线程中
     */
    private void useSubscribeOnAndObserveOn() {
        Observable
                .just(new Long(123))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())//No1
                .flatMap(new Function<Long, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Long aLong) throws Exception {
                        Log.i(TAG, "timer执行中: 0" + "   当前线程:" + Thread.currentThread().getName());
                        return Observable.just("我跳出来了");
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())//No2
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.i(TAG, "timer执行中: 1" + "   当前线程:" + Thread.currentThread().getName());
                        return s + 1;
                    }
                })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.i(TAG, "timer执行中: 2" + "   当前线程:" + Thread.currentThread().getName());
                        return s + 1;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())//No3
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.i(TAG, "timer执行中: 3" + "   当前线程:" + Thread.currentThread().getName());
                        return s + 1;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())//No4
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.i(TAG, "timer执行中: 4" + "   当前线程:" + Thread.currentThread().getName());
                        return s + 1;
                    }
                })
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())//No5
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, "timer执行后: " + "   当前线程:" + Thread.currentThread().getName());
                        Log.i(TAG, "timer执行后: " + s);
                    }
                });
    }

    /**
     * 只使用subscribeOn调度线程
     * subscribeOn()操作符 指定 subscribe() 所发生的线程，事件产生的线程
     * 在一条流道中，不管使用了多少个subscribeOn来调度线程
     * 只有在流道最开始调度的subscribeOn起作用
     * 在其下面的使用subscribeOn调度线程都会被调度到第一个subscribeOn调度的线程
     * 既在一条数据流中使用了No1，No2，No3，No4共4个subscribeOn指定调度
     * 只有数据流中第一个No1起作用
     */
    private void onlyUseSubscribeOn() {
        Observable
                .just(new Long(123))
                .subscribeOn(Schedulers.io())//No1
                .flatMap(new Function<Long, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Long aLong) throws Exception {
                        Log.i(TAG, "timer执行中: 0" + "   当前线程:" + Thread.currentThread().getName());
                        return Observable.just("我跳出来了");
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())//No2
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.i(TAG, "timer执行中: 1" + "   当前线程:" + Thread.currentThread().getName());
                        return s + 1;
                    }
                })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.i(TAG, "timer执行中: 2" + "   当前线程:" + Thread.currentThread().getName());
                        return s + 1;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())//No3
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.i(TAG, "timer执行中: 3" + "   当前线程:" + Thread.currentThread().getName());
                        return s + 1;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())//No4
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.i(TAG, "timer执行中: 4" + "   当前线程:" + Thread.currentThread().getName());
                        return s + 1;
                    }
                })
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())//No5
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, "timer执行后: " + "   当前线程:" + Thread.currentThread().getName());
                        Log.i(TAG, "timer执行后: " + s);
                    }
                });
    }

    /**
     * 只使用ObserveOn调度线程
     * ObserveOn() 指定 Observer 所运行在的线程，事件消费的线程。
     * observeOn操作符能够切换其下面操作符运行的线程
     * 其作用域是第一个observeOn操作符到与其相邻最近的第二个observeOn操作符之前
     * 在这之间使用的操作符后，它们运行的线程环境都在
     * 即
     * No1-No2之间的操作操作符运行的线程环境都在是No1处的oberveOn指定的线程
     */
    private void onlyUseObserveOn() {
        Observable
                .just(new Long(123))
                .observeOn(AndroidSchedulers.mainThread())//No1
                .flatMap(new Function<Long, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Long aLong) throws Exception {
                        Log.i(TAG, "timer执行中: 0" + "   当前线程:" + Thread.currentThread().getName());
                        return Observable.just("我跳出来了");
                    }
                })
                .observeOn(Schedulers.io())//No2
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.i(TAG, "timer执行中: 1" + "   当前线程:" + Thread.currentThread().getName());
                        return s + 1;
                    }
                })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.i(TAG, "timer执行中: 2" + "   当前线程:" + Thread.currentThread().getName());
                        return s + 1;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//No3
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.i(TAG, "timer执行中: 3" + "   当前线程:" + Thread.currentThread().getName());
                        return s + 1;
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.i(TAG, "timer执行中: 4" + "   当前线程:" + Thread.currentThread().getName());
                        return s + 1;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, "timer执行后: " + "   当前线程:" + Thread.currentThread().getName());
                        Log.i(TAG, "timer执行后: " + s);
                    }
                });
    }


    private void testThread1() {
        Observable
//                .timer(2, TimeUnit.SECONDS)
                .just(new Long(123))
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Long, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Long aLong) throws Exception {
                        Log.i(TAG, "timer执行中: 0" + "   当前线程:" + Thread.currentThread().getName());
                        return Observable.just("我跳出来了");
                    }
                })
                .observeOn(Schedulers.io())
                .delay(1, TimeUnit.SECONDS)
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.i(TAG, "timer执行中: 1" + "   当前线程:" + Thread.currentThread().getName());
                        return s + 1;
                    }
                })
                .delay(2, TimeUnit.SECONDS)
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.i(TAG, "timer执行中: 2" + "   当前线程:" + Thread.currentThread().getName());
                        return s + 1;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .delay(3, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.i(TAG, "timer执行中: 3" + "   当前线程:" + Thread.currentThread().getName());
                        return s + 1;
                    }
                })
                .delay(4, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.i(TAG, "timer执行中: 4" + "   当前线程:" + Thread.currentThread().getName());
                        return s + 1;
                    }
                })
//                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, "timer执行后: " + "   当前线程:" + Thread.currentThread().getName());
                        Log.i(TAG, "timer执行后: " + s);
                    }
                });
    }
}
