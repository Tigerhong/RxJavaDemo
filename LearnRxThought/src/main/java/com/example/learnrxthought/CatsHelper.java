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

    private String someDefaultValue = "服务器上默认最可爱猫的地址";

    /**
     * 保存最可爱的猫，并将其服务器路径返回
     *
     * @param query 检索的关键字
     * @return
     */
    public AsyncJob<String> saveTheCutestCat(final String query) {
        final AsyncJob<List<Cat>> catsListAsyncJob = apiWrapper.queryCats(query);

        final AsyncJob<Cat> cutestCatAsyncJob = catsListAsyncJob.map(new Func<List<Cat>, Cat>() {
            @Override
            public Cat call(List<Cat> result) {
                return findCutest(result);
            }
        });
        AsyncJob<String> storedUriAsyncJob = cutestCatAsyncJob.flatMap(new Func<Cat, AsyncJob<String>>() {
            @Override
            public AsyncJob<String> call(Cat cat) {
                return apiWrapper.store(cat);
            }
        });
        return storedUriAsyncJob;
    }

    /**
     * 找到最可爱的猫
     *
     * @param cats
     * @return
     */
    private Cat findCutest(List<Cat> cats) {
        return Collections.max(cats);
    }
}