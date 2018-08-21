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

    public void queryCats(String query, final Callback<List<Cat>> catsCallback){
        api.queryCats(query, new Api.CatsQueryCallback() {
            @Override
            public void onCatListReceived(List<Cat> cats) {
                catsCallback.onResult(cats);
            }

            @Override
            public void onQueryFailed(Exception e) {
                catsCallback.onError(e);
            }
        });
    }

    public void store(Cat cat, final Callback<String> uriCallback){
        api.store(cat, new Api.StoreCallback() {
            @Override
            public void onCatStored(String uri) {
                uriCallback.onResult(uri);
            }

            @Override
            public void onStoreFailed(Exception e) {
                uriCallback.onError(e);
            }
        });
    }
}
