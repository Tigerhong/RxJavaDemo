package com.example.learnrxthought;

import java.util.List;

/**
 * Description: ApiWrapper
 * Date: 2018/8/21
 *包装了一下api接口，使其调用者调用时可以使用 泛型的callback 来接收接口返回的结果
 * @author zhengong.hong@ubtrobot.com
 */
public class ApiWrapper {

    public interface Callback<T> {
        void onResult(T result);
        void onError(Exception e);
    }

    Api api;

    public AsyncJob<List<Cat>> queryCats(final String query){
        return new AsyncJob<List<Cat>>() {
            @Override
            public void start(final Callback<List<Cat>> callback) {
                api.queryCats(query, new Api.CatsQueryCallback() {
                    @Override
                    public void onCatListReceived(List<Cat> cats) {
                        callback.onResult(cats);
                    }

                    @Override
                    public void onQueryFailed(Exception e) {
                        callback.onError(e);
                    }
                });
            }
        };
    }

    public AsyncJob<String> store(final Cat cat){
        return new AsyncJob<String>(){
            @Override
            public void start(final Callback<String> callback) {
                api.store(cat, new Api.StoreCallback() {
                    @Override
                    public void onCatStored(String uri) {
                        callback.onResult(uri);
                    }

                    @Override
                    public void onStoreFailed(Exception e) {
                        callback.onError(e);
                    }
                });
            }
        };
    }
}
