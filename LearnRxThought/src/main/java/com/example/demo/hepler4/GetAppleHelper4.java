package com.example.demo.hepler4;

import com.example.demo.bean.Apple;
import java.util.Collections;
import java.util.List;

/**
 * Description: GetAppleHelper3
 * Date: 2018/8/21
 * 优化版的简单回调
 * 使用一个泛型回调接口去替代原来所有的接口
 * 比之前的简明了一些。我们可以通过直接传递
 * 一个顶级的callback回调接口给apiWrapper.store来减少回调间的层级调用
 *
 * @author zhengong.hong@ubtrobot.com
 */
public class GetAppleHelper4 {
    private static final String TAG = "GetAppleHelper4     ";

    ApiWrapper4 api = new ApiWrapper4();

    /**
     * @param key
     */
    public Observable<String> saveBigAppleToServer(final String key) {
        System.out.println(TAG + "start get apples by key.......");
        //获取 key的苹果列表 Observable<List<Apple>>
        final Observable<List<Apple>> appleListObservable = api.query(key);
        //获取 最大苹果的Observable<Apple>
        Observable<Apple> appleObservable = appleListObservable.map(new Func<List<Apple>, Apple>() {
            @Override
            public Apple call(List<Apple> apples) {
                System.out.println(TAG + "apples size:" + apples.size());
                System.out.println("-----------------");
                Apple bigApple = getBigApple(apples);
                System.out.println(TAG + "bigApple is " + bigApple.name);
                return bigApple;
            }
        });
        //获取保存苹果地址的Observable<String>
        Observable<String> saveObservable = appleObservable.flatMap(new Func<Apple, Observable<String>>() {
            @Override
            public Observable<String> call(Apple apple) {
                System.out.println(TAG + "start save apple.......................");
                return api.save(apple);
            }
        });
        return saveObservable;
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
