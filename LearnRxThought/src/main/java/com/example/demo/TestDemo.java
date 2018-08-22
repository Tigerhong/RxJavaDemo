package com.example.demo;


import com.example.demo.helper3.GetAppleHelper3;
import com.example.demo.helper3.Callback;
import com.example.demo.hepler1.GetAppleHelper;
import com.example.demo.hepler2.ApiWrapper2;
import com.example.demo.hepler2.GetAppleHelper2;
import com.example.demo.hepler4.ApiWrapper4;
import com.example.demo.hepler4.GetAppleHelper4;

/**
 * Description: TestDemo
 * Date: 2018/8/21
 *
 * @author zhengong.hong@ubtrobot.com
 */
public class TestDemo {
    private static final String TAG = "TestDemo";

    public static void main(String[] args) {
//        saveBigApple();
//        saveBigApple2();
//        saveBigApple3();
        saveBigApple4();
        System.out.println(TAG + "thing1");
        System.out.println(TAG + "thing2");
        System.out.println(TAG + "thing3");
    }
    private static void saveBigApple4() {
        GetAppleHelper4 appleHelper = new GetAppleHelper4();
        appleHelper.saveBigAppleToServer("2").subscribe(new ApiWrapper4.Callback<String>() {
            @Override
            public void onSuccess(String bigAppleUrl) {
                System.out.println(TAG + " onSuccess:" + bigAppleUrl);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(TAG + "onError" + t.getMessage());
            }
        });
    }

    private static void saveBigApple3() {
        GetAppleHelper3 appleHelper = new GetAppleHelper3();
        appleHelper.saveBigAppleToServer("2").start(new Callback<String>() {
            @Override
            public void onSuccess(String bigAppleUrl) {
                System.out.println(TAG + " onSuccess:" + bigAppleUrl);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(TAG + "onError" + t.getMessage());
            }
        });
    }

    private static void saveBigApple2() {
        GetAppleHelper2 getAppleHelper2 = new GetAppleHelper2();
        getAppleHelper2.saveBigAppleToServer("3", new ApiWrapper2.Callback<String>() {
            @Override
            public void onSuccess(String bigAppleUrl) {
                System.out.println(TAG + " onSuccess:" + bigAppleUrl);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(TAG + "onError" + t.getMessage());
            }
        });
    }

    private static void saveBigApple() {
        GetAppleHelper appleHelper = new GetAppleHelper();

        appleHelper.saveBigAppleToServer("2", new GetAppleHelper.GetBigAppleUrlCallback() {
            @Override
            public void onSuccess(String bigAppleUrl) {
                System.out.println(TAG + " onSuccess:" + bigAppleUrl);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(TAG + "onError" + t.getMessage());
            }
        });
    }
}
