package com.example.demo.helper3;


/**
 * Description: AsyncJob
 * Date: 2018/8/22
 *
 * @author zhengong.hong@ubtrobot.com
 */
public abstract class AsyncJob<T> {
    public abstract void start(Callback<T> callback);

    public <R> AsyncJob<R> map(final Func<T, R> func) {
        /*AsyncJob<Apple> cutestAppleAsyncJob = new AsyncJob<Apple>() {
            @Override
            public void start(final Callback<Apple> callback) {
                applesAsyncJob.start(new Callback<List<Apple>>() {
                    @Override
                    public void onSuccess(List<Apple> apples) {
                        callback.onSuccess(getBigApple(apples));
                    }
                    @Override
                    public void onError(Throwable t) {
                        callback.onError(t);
                    }
                });
            }
        };*/
        final AsyncJob<T> souce = this;
        return new AsyncJob<R>() {
            @Override
            public void start(final Callback<R> callback) {
                souce.start(new Callback<T>() {
                    @Override
                    public void onSuccess(T t) {
                        callback.onSuccess(func.call(t));
                    }

                    @Override
                    public void onError(Throwable t) {
                        callback.onError(t);
                    }
                });
            }
        };
    }

    public <R> AsyncJob<R> flatMap(final Func<T, AsyncJob<R>> func) {
         /*AsyncJob<String> saveAppleAsyncJob=new AsyncJob<String>() {
            @Override
            public void start(final Callback<String> callback) {
                appleAsyncJob.start(new Callback<Apple>() {
                    @Override
                    public void onSuccess(Apple apple) {
                        System.out.println(TAG + "start save apple.......................");
                        api.save(apple).start(new Callback<String>() {
                            @Override
                            public void onSuccess(String saveUrl) {
                                System.out.println(TAG + "serverUrl " + saveUrl);
                                System.out.println("-----------------");
                                callback.onSuccess(saveUrl);
                            }
                            @Override
                            public void onError(Throwable t) {
                                callback.onError(t);
                            }
                        });
                    }
                    @Override
                    public void onError(Throwable t) {
                        callback.onError(t);
                    }
                });
            }
        };*/
        final AsyncJob<T> souce = this;
        return new AsyncJob<R>() {
            @Override
            public void start(final Callback<R> callback) {
                souce.start(new Callback<T>() {
                    @Override
                    public void onSuccess(T t) {
                        AsyncJob<R> call = func.call(t);
                        call.start(new Callback<R>() {
                            @Override
                            public void onSuccess(R r) {
                                callback.onSuccess(r);
                            }
                            @Override
                            public void onError(Throwable t) {
                                callback.onError(t);
                            }
                        });
                    }
                    @Override
                    public void onError(Throwable t) {
                        callback.onError(t);
                    }
                });
            }
        };
    }
}
