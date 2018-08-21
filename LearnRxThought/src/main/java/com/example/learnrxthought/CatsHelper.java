package com.example.learnrxthought;

import java.util.Collections;
import java.util.List;

/**
 * Description: CatsHelper
 * Date: 2018/8/21
 *
 * @author zhengong.hong@ubtrobot.com
 */
public class CatsHelper {

    ApiWrapper apiWrapper;

    private String someDefaultValue="服务器上默认最可爱猫的地址";

    /**
     *保存最可爱的猫，并将其服务器路径返回
     * @param query 检索的关键字
     * @return
     */
    public void saveTheCutestCat(String query, final ApiWrapper.Callback<String> cutestCatCallback){
        apiWrapper.queryCats(query, new ApiWrapper.Callback<List<Cat>>() {
            @Override
            public void onResult(List<Cat> cats) {
                Cat cutest = findCutest(cats);
                apiWrapper.store(cutest, cutestCatCallback);
            }

            @Override
            public void onError(Exception e) {
                cutestCatCallback.onError(e);
            }
        });
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