package com.example.demo.hepler1;

import com.example.demo.bean.Apple;
import com.example.demo.api.Api;
import com.example.demo.api.ApiImpl;

import java.util.Collections;
import java.util.List;

/**
 * Description: GetAppleHelper3
 * Date: 2018/8/21
 *简单的回调
 * 每一个异步操作你都必须创建出回调接口并在代码中插入它们，每一次都需要手动地加入
 * @author zhengong.hong@ubtrobot.com
 */
public class GetAppleHelper {
    private static final String TAG = "GetAppleHelper4     ";


   public interface GetBigAppleUrlCallback {
        void onSuccess(String bigAppleUrl);

        void onError(Throwable t);
    }

    Api api = new ApiImpl();

    /**
     * @param key
     * @param appleUrlCallback
     */
    public void saveBigAppleToServer(final String key, final GetBigAppleUrlCallback appleUrlCallback) {
        System.out.println(TAG + "start get apples by key.......");
        api.query(key, new Api.AppleQueryCallback() {
            @Override
            public void onSuccess(List<Apple> apples) {
                System.out.println(TAG + "apples size:" + apples.size());
                System.out.println("-----------------");
                Apple bigApple = getBigApple(apples);
                System.out.println(TAG + "bigApple is " + bigApple.name);

                System.out.println(TAG + "start save apple.......................");
                api.save(bigApple, new Api.AppleSaveCallback() {
                    @Override
                    public void onSuccess(String saveUrl) {
                        System.out.println(TAG + "serverUrl " + saveUrl);
                        System.out.println("-----------------");
                        appleUrlCallback.onSuccess(saveUrl);
                    }

                    @Override
                    public void onError(Throwable t) {
                        appleUrlCallback.onError(t);
                    }
                });
            }

            @Override
            public void onError(Throwable t) {
                appleUrlCallback.onError(t);
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
