package com.example.learnrxthought;

/**
 * Description: AsyncJob
 * 封装异步回调
 * 之前的任何异步操作需要携带所需的常规参数和一个回调实例对象。（这里是传一个回调监听进去）
 * 如果我们试图去分开这几个阶段
 * 每个异步操作仅仅将会携带一个参数对象，然后返回一些携带着回调（信息）的临时对象。（这里是注册一个回调监听）
 *异步操作中返回一些临时对象
 * @author zhengong.hong@ubtrobot.com
 */

/***
 * 异步回调也是观察者模式
 * 1.第一种观察者模式（简单的观察者模式）：
 * 类似： public void queryCats(String query, Callback<List<Cat>> catsCallback)｛｝
 * 调用者在调用queryCats的时候必须传参数和回调监听
 * ，直接一步到位 调用，传参，注册回调监听，将三步组合成一步；这种形式个人理解为简单的观察者模式
 * 容易导致产生多个回调嵌套，陷入回调地狱中，代码不易查看。
 * 2.第二种观察者模式（组合式的观察者模式）
 * AsyncJob现在的这种回调方式
 * public AsyncJob<List<Cat>> queryCats(String query) {｝
 * 调用者在没有start时，是还没有实现注册回调监听的
 * AsyncJob这种回调方式将  调用，传参，注册回调监听都分开了，
 * 这样利于组合，不会产生回调嵌套，代码容易查看
 */
public abstract class AsyncJob<T> {
    /**
     * 当这个方法被调用的时候
     * 相当于往异步任务中注册了一个回调监听
     * @param callback 回调的接口
     */
    public abstract void start(ApiWrapper.Callback<T> callback);

    public <R> AsyncJob<R> map(final Func<T,R>func){
        final AsyncJob<T>socrce=this;
        return new AsyncJob<R>() {
            @Override
            public void start(final ApiWrapper.Callback<R> callback) {
                socrce.start(new ApiWrapper.Callback<T>() {
                    @Override
                    public void onResult(T result) {
                        R mapped = func.call(result);
                        callback.onResult(mapped);
                    }

                    @Override
                    public void onError(Exception e) {
                        callback.onError(e);
                    }
                });
            }
        };
    }

    public <R> AsyncJob<R> flatMap(final Func<T,AsyncJob<R>>func){
        final AsyncJob<T>source=this;
        return  new AsyncJob<R>() {
            @Override
            public void start(final ApiWrapper.Callback<R> callback) {
                source.start(new ApiWrapper.Callback<T>() {
                    @Override
                    public void onResult(T result) {
                        AsyncJob<R> mapped  = func.call(result);
                        mapped.start(new ApiWrapper.Callback<R>() {
                            @Override
                            public void onResult(R result) {
                                callback.onResult(result);
                            }

                            @Override
                            public void onError(Exception e) {
                                callback.onError(e);
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        callback.onError(e);
                    }
                });
            }
        };
    }
}
