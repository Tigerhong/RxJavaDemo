package com.example.operator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.R;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class CatchActivity extends AppCompatActivity {
    private static final String TAG = "CatchActivity1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch);

        testOnErrorReturn();
        testOnErrorResumeNext();
        testOnExceptionResumeNext();
    }
    private void testOnExceptionResumeNext() {
        Log.i(TAG, "testOnExceptionResumeNext: -----------------------");
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                for (int i=0;i<5;i++){
                    if (i==2){
//                        e.onError(new Throwable("第二个出错了"));
//                        e.onError(new Error("第二个出错了"));
                        e.onError(new Exception("第二个出错了"));
                    }else{
                        e.onNext(i+"");
                    }
                }
                e.onComplete();
            }
        }).onExceptionResumeNext(Observable.just("出现了一个错误1","出现了一个错误2"))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {
                        Log.i(TAG, "onNext: "+value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    private void testOnErrorResumeNext() {
        Log.i(TAG, "testOnErrorResumeNext: -----------------------");
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                for (int i=0;i<5;i++){
                    if (i==2){
//                        e.onError(new Throwable("第二个出错了"));
//                        e.onError(new Error("第二个出错了"));
                        e.onError(new Exception("第二个出错了"));
                    }else{
                        e.onNext(i+"");
                    }
                }
                e.onComplete();
            }
        }).onErrorResumeNext(new Function<Throwable, ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> apply(Throwable throwable) throws Exception {
                return Observable.just("出现了一个错误："+throwable.getMessage());
            }
        })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {
                        Log.i(TAG, "onNext: "+value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    private void testOnErrorReturn() {
        Log.i(TAG, "testOnErrorReturn: ---------------------");
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                for (int i=0;i<5;i++){
                    if (i==2){
//                        e.onError(new Throwable("第二个出错了"));
//                        e.onError(new Error("第二个出错了"));
                        e.onError(new Exception("第二个出错了"));
                    }else{
                        e.onNext(i+"");
                    }
                }
                e.onComplete();
            }
        }).onErrorReturn(new Function<Throwable, String>() {
            @Override
            public String apply(Throwable throwable) throws Exception {
                return "出现了一个错误："+throwable.getMessage();
            }
        })
                .subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                Log.i(TAG, "onNext: "+value);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: "+e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        });
    }
}
