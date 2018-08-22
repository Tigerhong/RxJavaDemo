package com.example.demo.helper3;

import com.example.demo.bean.Apple;

import java.util.Collections;
import java.util.List;

/**
 * Description: GetAppleHelper3
 * Date: 2018/8/21
 *
 * @author zhengong.hong@ubtrobot.com
 */
public class GetAppleHelper3 {
    private static final String TAG = "GetAppleHelper3    ";
    ApiWrapper3 api = new ApiWrapper3();

    /**
     * @param key
     */
    public AsyncJob<String> saveBigAppleToServer(final String key) {

        System.out.println(TAG + "start get apples by key.......");

        final AsyncJob<List<Apple>> applesAsyncJob = api.query(key);

        final AsyncJob<Apple> appleAsyncJob = applesAsyncJob.map(new Func<List<Apple>, Apple>() {
            @Override
            public Apple call(List<Apple> apples) {
                System.out.println(TAG + "apples size:" + apples.size());
                System.out.println("-----------------");
                Apple bigApple = getBigApple(apples);
                System.out.println(TAG + "bigApple is " + bigApple.name);
                System.out.println("-----------------");
                return bigApple;
            }
        });

        AsyncJob<String> stringAsyncJob = appleAsyncJob.flatMap(new Func<Apple, AsyncJob<String>>() {
            @Override
            public AsyncJob<String> call(Apple apple) {
                return api.save(apple);
            }
        });
        //上面的可以转化成以下形式
        /*AsyncJob<String> stringAsyncJob = api.query(key).map(new Func<List<Apple>, Apple>() {
            @Override
            public Apple call(List<Apple> apples) {
                return getBigApple(apples);
            }
        }).flatMap(new Func<Apple, AsyncJob<String>>() {
            @Override
            public AsyncJob<String> call(Apple apple) {
                return api.save(apple);
            }
        });*/
        return stringAsyncJob;
    }

    /**
     * 获取最大的苹果
     *
     * @param apples
     * @return
     */
    Apple getBigApple(List<Apple> apples) {
        return Collections.max(apples);
    }
}
