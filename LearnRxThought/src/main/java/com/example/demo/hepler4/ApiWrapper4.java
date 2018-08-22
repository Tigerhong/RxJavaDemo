package com.example.demo.hepler4;

import com.example.demo.api.Api;
import com.example.demo.api.ApiImpl;
import com.example.demo.bean.Apple;

import java.util.List;

/**
 * Description: ApiWrapper4
 * Date: 2018/8/22
 *
 * @author zhengong.hong@ubtrobot.com
 */
public class ApiWrapper4 {
    Api api = new ApiImpl();

    public interface Callback<T> {
        void onSuccess(T t);

        void onError(Throwable t);
    }

    public Observable<List<Apple>> query(final String query) {
        return new Observable<List<Apple>>(){
            @Override
            public void subscribe(final Callback<List<Apple>> callback) {
                api.query(query, new Api.AppleQueryCallback() {
                    @Override
                    public void onSuccess(List<Apple> apples) {
                        callback.onSuccess(apples);
                    }
                    @Override
                    public void onError(Throwable t) {
                        callback.onError(t);
                    }
                });
            }
        };
    }

    public Observable<String> save(final Apple a) {
        return new Observable<String>(){
            @Override
            public void subscribe(final Callback<String> callback) {
                api.save(a, new Api.AppleSaveCallback() {
                    @Override
                    public void onSuccess(String saveUrl) {
                        callback.onSuccess(saveUrl);
                    }

                    @Override
                    public void onError(Throwable t) {
                        callback.onError(t);
                    }
                });
            }
        };
    }
}
