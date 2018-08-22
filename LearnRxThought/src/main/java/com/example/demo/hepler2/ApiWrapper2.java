package com.example.demo.hepler2;

import com.example.demo.bean.Apple;
import com.example.demo.api.Api;
import com.example.demo.api.ApiImpl;

import java.util.List;

/**
 * Description: ApiWrapper4
 * Date: 2018/8/22
 *
 * @author zhengong.hong@ubtrobot.com
 */
public class ApiWrapper2 {
    Api api = new ApiImpl();

    public interface Callback<T> {
        void onSuccess(T t);

        void onError(Throwable t);
    }

    public void query(final String query, final Callback callback) {
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

    public void save(Apple a, final Callback callback) {
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
}
