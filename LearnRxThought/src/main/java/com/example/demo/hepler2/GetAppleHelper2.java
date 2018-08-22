package com.example.demo.hepler2;

import com.example.demo.bean.Apple;

import java.util.Collections;
import java.util.List;

/**
 * Description: GetAppleHelper3
 * Date: 2018/8/21
 *优化版的简单回调
 * 使用一个泛型回调接口去替代原来所有的接口
 * 比之前的简明了一些。我们可以通过直接传递
 * 一个顶级的callback回调接口给apiWrapper.store来减少回调间的层级调用
 * @author zhengong.hong@ubtrobot.com
 */
public class GetAppleHelper2 {
    private static final String TAG = "GetAppleHelper4     ";

    ApiWrapper2 api = new ApiWrapper2();

    /**
     * @param key
     * @param callback
     */
    public void saveBigAppleToServer(final String key, final ApiWrapper2.Callback callback) {
        System.out.println(TAG + "start get apples by key.......");
        api.query(key, new ApiWrapper2.Callback<List<Apple>>() {
            @Override
            public void onSuccess(List<Apple> apples) {
                System.out.println(TAG + "apples size:" + apples.size());
                System.out.println("-----------------");
                Apple bigApple = getBigApple(apples);
                System.out.println(TAG + "bigApple is " + bigApple.name);
                System.out.println(TAG + "start save apple.......................");
                api.save(bigApple, callback);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }

    /**
     * 获取最大的苹果
     *
     * @param apples
     * @return
     */
    public Apple getBigApple(List<Apple> apples) {
        return Collections.max(apples);
    }
}
