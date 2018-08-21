package com.example.operator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.R;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 1、doOnNext
 * 它产生的Observable每发射一项数据就会调用它一次
 * ，但是它的Action不是接受一个Notification参数，而是接受发射的数据项；
 * doOnNext一般用于在subscribe之前对数据的一些处理，比如数据的保存等
 *
 * 2、doFinally
 *当它产生的Observable终止之后会被调用，无论是正常还 是异常终止
 *
 */
public class DoOperatorActivity extends AppCompatActivity {
    private static final String TAG = "DoOperatorActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_operator);
        testDoOperator(1);
//        testDoOperator(1,2,3,4);
    }

    /**
     * 测试doOnNext，doAfterNext，doOnSubscribe
     * 运行顺序
     * 1.doOnSubscribe()操作符运行在下游的observer订阅了（subscribe）observable之前
     *
     * 2.doOnNext操作符运行在上游发射数据之前
     *   即：上游的被订阅（subscribe）后，使用了ObservableEmitter调用onNext（）之前
     *
     * 3.doAfterNext操作符运行在下游消费数据之后
     *
     */
    private void testDoOperator(Integer...item) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (Integer integer : item) {
                    e.onNext(integer);
                }
                e.onComplete();
//                e.onError(new RuntimeException("发射数据失败"));
            }
        })
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        printlnLog("just后的doOnNext"+integer);
                    }
                })
                .doAfterNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        printlnLog("just后的AfterNext"+integer);
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        printlnLog("just后的doOnSubscribe");
                    }
                })
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "改造后的"+integer;
                    }
                })
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        printlnLog("map后的doOnNext"+s);
                    }
                })
                .doAfterNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        printlnLog("map后的doAfterNext"+s);
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        printlnLog("map后的doOnSubscribe");
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        printlnLog("map后的doOnComplete");
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        printlnLog("map后的doOnError");
                    }
                })
                .subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i(TAG, "accept: ---------------");
            }
        });
    }

    private void printlnLog(String text){
        Log.i(TAG, "printlnLog: "+text);
    }
}
