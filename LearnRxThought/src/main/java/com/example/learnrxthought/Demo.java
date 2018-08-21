package com.example.learnrxthought;

public class Demo {
    public static void main(String[] args) {
        CatsHelper catsHelper = new CatsHelper();
       catsHelper.saveTheCutestCat("黑猫", new CatsHelper.CutestCatCallback() {
           @Override
           public void onCutestCatSaved(String uri) {
               //成功获取到最可爱猫地址
           }

           @Override
           public void onQueryFailed(Exception e) {
                //失败了
           }
       });
    }
}
