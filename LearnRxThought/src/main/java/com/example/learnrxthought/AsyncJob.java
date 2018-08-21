package com.example.learnrxthought;

/**
 * Description: AsyncJob
 * 封装异步操作
 * 之前的任何异步操作需要携带所需的常规参数和一个回调实例对象。（这里是传一个回调监听进去）
 * 如果我们试图去分开这几个阶段
 * 每个异步操作仅仅将会携带一个参数对象，然后返回一些携带着回调（信息）的临时对象。（这里是注册一个回调监听）
 *异步操作中返回一些临时对象
 * @author zhengong.hong@ubtrobot.com
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
