package com.example.demo.api;

import com.example.demo.bean.Apple;

import java.util.ArrayList;

/**
 * Description: ApiImpl
 * Date: 2018/8/22
 *
 * @author zhengong.hong@ubtrobot.com
 */
public class ApiImpl implements Api{

    @Override
    public void query(final String query, final AppleQueryCallback appleQueryCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<Apple> apples = new ArrayList<>();
                    for (int i = 10; i < 100; i++) {
                        if (String.valueOf(i).contains(query)) {
                            apples.add(new Apple("apple:" + i, i));
                        }
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        appleQueryCallback.onError(e);
                    }
                    if (apples.isEmpty()) {
                        throw new RuntimeException("get apples by " + query + "error");
                    }
                    appleQueryCallback.onSuccess(apples);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    appleQueryCallback.onError(e);
                }
            }
        }).start();
        ;
    }

    @Override
    public void save(final Apple a, final AppleSaveCallback appleSaveCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (a.bigValue == 5) {
                        throw new RuntimeException("save bigApple error,beacuse it bigApple.bigValue contains 5");
                    }
                    appleSaveCallback.onSuccess(a.name + " serverUrl:" + "http://xx.xx.xx/" + a.bigValue);
                } catch (Exception e) {
                    e.printStackTrace();
                    appleSaveCallback.onError(e);
                }
            }
        }).start();
    }
}
