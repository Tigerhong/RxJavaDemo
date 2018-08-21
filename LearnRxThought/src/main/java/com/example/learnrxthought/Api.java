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
     * 根据qurery查询猫列表
     * @param query 关键字
     * @return
     */
    List<Cat> queryCats(String query);

    /**
     * 存储猫
     * @param cat
     * @return 返回服务器保存猫的地址
     */
    String store(Cat cat);
}