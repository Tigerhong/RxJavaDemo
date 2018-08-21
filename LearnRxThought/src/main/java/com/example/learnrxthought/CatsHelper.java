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

    Api api;
    private String someDefaultValue="服务器上默认最可爱猫的地址";

    public interface CutestCatCallback {
        void onCutestCatSaved(String uri);
        void onQueryFailed(Exception e);
    }

    /**
     *保存最可爱的猫，并将其服务器路径返回
     * @param query 检索的关键字
     * @return
     */
    public void saveTheCutestCat(String query, final CutestCatCallback cutestCatCallback){
        api.queryCats(query, new Api.CatsQueryCallback() {
            @Override
            public void onCatListReceived(List<Cat> cats) {
                Cat cutest = findCutest(cats);

                String savedUri = api.store(cutest, new Api.StoreCallback() {
                    @Override
                    public void onCatStored(String uri) {
                        cutestCatCallback.onCutestCatSaved(uri);
                    }

                    @Override
                    public void onStoreFailed(Exception e) {
                        cutestCatCallback.onQueryFailed(e);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                cutestCatCallback.onQueryFailed(e);
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