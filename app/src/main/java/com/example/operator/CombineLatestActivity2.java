package com.example.operator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 使用CombineLatest操作符，操作一串observables
 * 本例子来源于RxJava 沉思录（二）：空间维度（https://juejin.im/post/5b8f5470e51d450e3d2c8ddf）
 */
public class CombineLatestActivity2 extends AppCompatActivity {
    private static final String TAG = "CombineLatestActivity22";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine_latest);
        Log.i(TAG, "onCreate: ");
//        refresh1();
        refreshWithCombineLast();
    }

    private void refreshWithCombineLast() {
        getColumnObservables()
                .map(types -> {
                    List<Observable<? extends List<? extends User>>> requestObservableList = new ArrayList<>();
                    for (String type : types) {
                        switch (type) {
                            case "A":
                                requestObservableList.add(
                                        getUserObservablesA().startWith(new ArrayList<User>())
                                );
                                break;
                            case "B":
                                requestObservableList.add(
                                        getUserObservablesB().startWith(new ArrayList<User>())
                                );
                                break;
                            case "C":
                                requestObservableList.add(
                                        getUserObservablesC().startWith(new ArrayList<User>())
                                );
                                break;
                        }
                    }
                    return requestObservableList;
                })
                .flatMap(new Function<List<Observable<? extends List<? extends User>>>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(List<Observable<? extends List<? extends User>>> observables) throws Exception {
                        Log.i(TAG, "flatMap:");
                        return Observable.combineLatest(observables, new Function<Object[], Object>() {
                            @Override
                            public List<User> apply(Object[] objects) throws Exception {
                                List<User> items = new ArrayList<>();
                                for (Object response : objects) {
                                    items.addAll((List<? extends User>) response);
                                }
                                Log.i(TAG, "combineLatest:");
                                return items;
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    Gson gson = new Gson();
                    String s = gson.toJson(data);
                    Log.i(TAG, "subscribe: " + s);
                });

    }


    // 不同类型数据出现的顺序
    private List<String> resultTypes;
    // 这些类型对应的数据的集合
    private LinkedList<List<? extends User>> responseList;

    public void refresh1() {
        getColumnObservables().subscribe(columns -> {
            // 保存配置的栏目顺序
            resultTypes = columns;
            responseList = new LinkedList<>(Collections.nCopies(columns.size(), new ArrayList<>()));
            for (String type : columns) {
                switch (type) {
                    case "A":
                        getUserObservablesA().startWith(new ArrayList<User>()).subscribe(data -> onOk("A", data));
                        break;
                    case "B":
                        getUserObservablesB().startWith(new ArrayList<User>()).subscribe(data -> onOk("B", data));
                        break;
                    case "C":
                        getUserObservablesC().startWith(new ArrayList<User>()).subscribe(data -> onOk("C", data));
                        break;
                }
            }
        });
    }

    private void onOk(String type, List<? extends User> response) {
        // 按配置的顺序，更新对应位置上的数据
        responseList.set(resultTypes.indexOf(type), response);
        // 把当前已返回的数据填充到一个 List 中
        List<User> data = new ArrayList<>();
        for (List<? extends User> itemList : responseList) {
            data.addAll(itemList);
        }
        // 更新列表
        Gson gson = new Gson();
        String s = gson.toJson(data);
        Log.i(TAG, "onOk: " + s);
    }


    private Observable<List<String>> getColumnObservables() {
        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
                String[] column = {"C", "A", "B"};
                List<String> list = Arrays.asList(column);
                e.onNext(list);
                e.onComplete();
            }
        }).delay(2, TimeUnit.SECONDS);
    }

    /**
     * 获取A类型用户数据
     *
     * @return
     */
    private Observable<List<User>> getUserObservablesA() {
        return Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> e) throws Exception {
                List<User> users = new ArrayList<>();
                for (int i = 0; i < 6; i++) {
                    users.add(new User("A" + i, i));
                }
                e.onNext(users);
                e.onComplete();
            }
        }).delay(5, TimeUnit.SECONDS);
    }

    private Observable<List<User>> getUserObservablesB() {
        return Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> e) throws Exception {
                List<User> users = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    users.add(new User("B" + i, i));
                }
                e.onNext(users);
                e.onComplete();
            }
        }).delay(3, TimeUnit.SECONDS);
    }

    private Observable<List<User>> getUserObservablesC() {
        return Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> e) throws Exception {
                List<User> users = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    users.add(new User("C" + i, i));
                }
                e.onNext(users);
                e.onComplete();
            }
        }).delay(9, TimeUnit.SECONDS);
    }

    class User {
        public String name;
        public int id;

        public User(String name, int id) {
            this.name = name;
            this.id = id;
        }
    }
}
