package com.example.demo.helper3;

/**
 * Description: Callback
 * Date: 2018/8/22
 *
 * @author zhengong.hong@ubtrobot.com
 */
public interface Callback<T> {
    void onSuccess(T t);

    void onError(Throwable t);
}
