package com.example.learnrxthought;

import java.util.List;

/**
 * Description: Api
 * Date: 2018/8/21
 *
 * @author zhengong.hong@ubtrobot.com
 */
public interface Api {
    /**
     * 猫查询的回调接口
     */
    interface CatsQueryCallback {
        void onCatListReceived(List<Cat> cats);
        void onError(Exception e);
    }

    /**
     * 猫保存的回调接口
     */
    interface StoreCallback{
        void onCatStored(String uri);
        void onStoreFailed(Exception e);
    }
    /**
     * 根据qurery查询猫列表
     * @param query 关键字
     * @return
     */
    void  queryCats(String query, CatsQueryCallback catsQueryCallback);

    /**
     * 存储猫
     * @param cat
     * @return 返回服务器保存猫的地址
     */
    String store(Cat cat, StoreCallback storeCallback);
}