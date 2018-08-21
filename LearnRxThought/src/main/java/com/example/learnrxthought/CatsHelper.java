package com.example.learnrxthought;

import com.sun.jndi.toolkit.url.Uri;

import java.util.Collections;
import java.util.List;

/**
 * Description: CatsHelper
 * Date: 2018/8/21
 *
 * @author zhengong.hong@ubtrobot.com
 */
public class CatsHelper {

    Api api;

    /**
     *保存最可爱的猫，并将其服务器路径返回
     * @param query 检索的关键字
     * @return
     */
    public String saveTheCutestCat(String query){
        List<Cat> cats = api.queryCats(query);
        Cat cutest = findCutest(cats);
        String savedUri = api.store(cutest);
        return savedUri;
    }

    /**
     * 找到最可爱的猫
     * @param cats
     * @return
     */
    private Cat findCutest(List<Cat> cats) {
        return Collections.max(cats);
    }
}