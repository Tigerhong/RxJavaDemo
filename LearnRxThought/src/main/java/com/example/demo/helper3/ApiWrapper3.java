package com.example.demo.helper3;

import com.example.demo.bean.Apple;
import com.example.demo.api.Api;
import com.example.demo.api.ApiImpl;

import java.util.List;

/**
 * Description: ApiWrapper3
 * Date: 2018/8/22
 *
 * @author zhengong.hong@ubtrobot.com
 */
class ApiWrapper3 {
    Api api = new ApiImpl();

    public AsyncJob<List<Apple>> query(final String query) {
        return new AsyncJob<List<Apple>>() {
            @Override
            public void start(final Callback<List<Apple>> callback) {
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

    public AsyncJob<String> save(final Apple a) {
            return new AsyncJob<String>() {
                @Override
                public void start(final Callback<String> callback) {
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
