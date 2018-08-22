package com.example.demo.api;

import com.example.demo.bean.Apple;

import java.util.List;

/**
 * Description: Api2
 * Date: 2018/8/21
 *
 * @author zhengong.hong@ubtrobot.com
 */
public interface Api {

     interface AppleQueryCallback{
        void onSuccess(List<Apple>apples);
        void onError(Throwable t);
    }

    interface AppleSaveCallback{
        void onSuccess(String saveUrl);
        void onError(Throwable t);
    }
    /**
     * 根据query关键词查询出相关苹果数据
     * @param query
     */
    void query(String query,AppleQueryCallback appleQueryCallback);

    /**
     * 保存苹果
     * @param a
     */
    void save(Apple a,AppleSaveCallback appleSaveCallback);
}
