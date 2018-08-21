package com.example.learnrxthought;

public class Demo {
    public static void main(String[] args) {
        CatsHelper catsHelper = new CatsHelper();

       catsHelper.saveTheCutestCat("黑猫").start( new ApiWrapper.Callback<String>() {
           @Override
           public void onResult(String result) {
               //成功获取到最可爱猫地址
           }

           @Override
           public void onError(Exception e) {
               //失败了
           }
       });
    }
}
