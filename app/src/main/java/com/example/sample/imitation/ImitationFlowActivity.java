package com.example.sample.imitation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.R;
import com.example.sample.imitation.rx.functions.Action1;
import com.example.sample.imitation.rx.Observable;
import com.example.sample.imitation.rx.Subscriber;
import com.example.sample.imitation.rx.functions.Fun;

public class ImitationFlowActivity extends AppCompatActivity {
    private static final String TAG = "ImitationFlowActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imitation_flow);

        testCreate();

        Log.i(TAG, "-------------------------------------");
        testAction();
        Log.i(TAG, "-------------------------------------");
        Observable.create(new Observable.OnSubscribe() {
            @Override
            public void call(Subscriber subscriber) {
                    subscriber.onNext(1);
                    subscriber.onNext(2);
                    subscriber.onNext(3);
            }
        }).map(new Fun<Integer, String>() {
            @Override
            public String apply(Integer o) {
                return "map改造后的数据:"+o;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String o) {
                Log.i(TAG, "call: "+o);
            }
        });
    }

    private void testAction() {
        Observable.create(new Observable.OnSubscribe() {
            @Override
            public void call(Subscriber subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
                subscriber.onComplate();
                subscriber.onError(new NullPointerException("有异常了"));
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer i) {
                Log.i(TAG, "只关注事件的发射的call: " + i);
            }
        });
    }

    private void testCreate() {
        Observable.create(new Observable.OnSubscribe() {
            @Override
            public void call(Subscriber subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
                subscriber.onNext(4);
                subscriber.onComplate();
                subscriber.onError(new NullPointerException("有异常了"));
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onComplate() {
                Log.i(TAG, "complate: ");
            }

            @Override
            public void onNext(Integer i) {
                Log.i(TAG, "onNext: " + i);
            }

            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: " + throwable.getMessage());
            }
        });
    }

}
