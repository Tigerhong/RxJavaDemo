package com.example.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity1";
    private User defaultUser=new User("小名","123");
    private TextView textView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar = findViewById(R.id.pb);
        textView = findViewById(R.id.tv);

        testRegisterRequet();
//        testLoginRequet();
//        testLoginAfterRegisterRequet();
    }
    private void showTv(String text) {
        textView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        textView.setText(text);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void hideTv() {
        textView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }
    private void testLoginAfterRegisterRequet() {
        //发送错误格式的帐号密码
        sendLoginAfterRegisterRequet(new RegisterRequestBean(new User("", "123")));

        //延迟10s后，发送正确格式的请求
        Observable.timer(10, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                sendLoginAfterRegisterRequet(new RegisterRequestBean(new User("小明", "123")));
            }
        });
    }

    /**
     * 发送注册后就登录的请求
     * @param bean
     */
    private void sendLoginAfterRegisterRequet(RegisterRequestBean bean) {
        Log.i(TAG,"------------------------");
        getRegisterObservable(bean)
                .flatMap(new Function<Integer, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(Integer integer) throws Exception {
                        return getLoginObservable(bean);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        printThreadName("请求成功");
                        Toast.makeText(LoginActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                        hideTv();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        printThreadName("请求失败");
                        hideTv();
                        Toast.makeText(LoginActivity.this, "请求失败,失败原因为：" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void testRegisterRequet() {
        //发送错误格式的帐号密码
        sendRegisterRequest(new RegisterRequestBean(new User("", "123")));
        //延迟10s后，发送正确格式的请求
        Observable.timer(10, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                sendRegisterRequest(new RegisterRequestBean(new User("小明", "123")));
            }
        });
    }

    private void testLoginRequet() {
        //发送错误格式的帐号密码
        sendLoginRequest(new RegisterRequestBean(new User("", "123")));
        //延迟10s后，发送正确格式的请求
        Observable.timer(10, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                sendLoginRequest(new RegisterRequestBean(new User("小明", "123")));
            }
        });
    }

    /**
     * 发送登录请求
     * @param bean
     */
    private void sendLoginRequest(RegisterRequestBean bean) {
        getLoginObservable(bean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        printThreadName("登录成功");
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        hideTv();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        printThreadName("登录失败");
                        hideTv();
                        Toast.makeText(LoginActivity.this, "登录失败,失败原因为：" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 获取登录的的Observable
     * @param bean
     * @return
     */
    private Observable<Integer> getLoginObservable(RegisterRequestBean bean) {
        return Observable.just(bean)
                .subscribeOn(Schedulers.io())//这里的subscribeOn能控制当前数据流整个上游发送事件的线程
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        printThreadName("登录发起请求前的doOnSubscribe");
                        showTv("登录中....");
                    }
                }).filter(new Predicate<RegisterRequestBean>() {
                    @Override
                    public boolean test(RegisterRequestBean bean) throws Exception {
                        printThreadName("输入的帐号或者密码是否为空");
                        if (TextUtils.isEmpty(bean.user.name)) {
                            throw new RuntimeException("帐号不能为空!!!!");
                        }
                        if (TextUtils.isEmpty(bean.user.pwd)) {
                            throw new RuntimeException("密码不能为空!!!!");
                        }
                        return true;
                    }
                })
                //这里的subscribeOn能控制在其以上相连的doOnSubscribe的线程
                //即相临的上游发射事件的线程、
                //这里这个subscribeOn能短暂的控制当前上游的线程
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<RegisterRequestBean, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(RegisterRequestBean registerRequestBean) throws Exception {
                        printThreadName("后台处理数据中flatMap");
                        if (registerRequestBean.user.pwd.equals(defaultUser.pwd)) {
                            return Observable.just(1);
                        } else {
                            return Observable.error(new RuntimeException("密码错误!!!!"));
                        }
                    }
                })
                .delay(5, TimeUnit.SECONDS,true);//模拟后台操作,延迟5s才发送事件到下游
    }
    /**
     * 发送注册请求
     *
     * @param bean
     */
    private void sendRegisterRequest(RegisterRequestBean bean) {
        getRegisterObservable(bean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        printThreadName("注册成功");
                        Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        hideTv();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        printThreadName("注册失败");
                        hideTv();
                        Toast.makeText(LoginActivity.this, "注册失败,失败原因为：" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 获取注册的Observable
     * @param bean
     * @return
     */
    private Observable<Integer> getRegisterObservable(RegisterRequestBean bean) {
        return Observable.just(bean)
                .subscribeOn(Schedulers.io())//这里的subscribeOn能控制当前数据流整个上游发送事件的线程
                .filter(new Predicate<RegisterRequestBean>() {
                    @Override
                    public boolean test(RegisterRequestBean bean) throws Exception {
                        printThreadName("输入的帐号或者密码是否为空");
                        if (TextUtils.isEmpty(bean.user.name)) {
                            throw new RuntimeException("帐号不能为空!!!!");
                        }
                        if (TextUtils.isEmpty(bean.user.pwd)) {
                            throw new RuntimeException("密码不能为空!!!!");
                        }
                        return true;
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        printThreadName("注册发起请求前的doOnSubscribe");
                        showTv("注册中....");
                    }
                })
                //这里的subscribeOn能控制在其以上相连的doOnSubscribe的线程
                //即相临的上游发射事件的线程、
                //这里这个subscribeOn能短暂的控制当前上游的线程
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<RegisterRequestBean, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(RegisterRequestBean registerRequestBean) throws Exception {
                        printThreadName("后台处理数据中flatMap");
                        if (registerRequestBean.user.pwd.equals(defaultUser.pwd)) {
                            return Observable.just(1);
                        } else {
                            return Observable.error(new RuntimeException("密码错误!!!!"));
                        }
                    }
                })
                .delay(5, TimeUnit.SECONDS,true);//模拟后台操作,延迟5s才发送事件到下游
    }

    /**
     * 打印当前运行环境
     * @param text
     */
    private void printThreadName(String text) {
        Log.i(TAG, text + "+++运行在：" + Thread.currentThread().getName());
    }
    class User{
        public String name;
        public String pwd;

        public User(String name, String pwd) {
            this.name = name;
            this.pwd = pwd;
        }
    }
    class RegisterRequestBean {
       public User user;

        public RegisterRequestBean(User user) {
            this.user = user;
        }
    }
}
