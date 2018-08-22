package com.example.demo.hepler4;

/**
 * Description: Observable
 * Date: 2018/8/22
 *
 * @author zhengong.hong@ubtrobot.com
 */
public abstract class Observable<T> {

    public abstract void subscribe(ApiWrapper4.Callback<T> callback);


    public <R> Observable<R> map(final Func<T, R> func) {
        /*final Observable<Apple> appleObservable = new Observable<Apple>() {
            @Override
            public void subscribe(final ApiWrapper4.Callback<Apple> callback) {
                appleListObservable.subscribe(new ApiWrapper4.Callback<List<Apple>>() {
                    @Override
                    public void onSuccess(List<Apple> apples) {
                        System.out.println(TAG + "apples size:" + apples.size());
                        System.out.println("-----------------");
                        Apple bigApple = getBigApple(apples);
                        System.out.println(TAG + "bigApple is " + bigApple.name);
                        callback.onSuccess(bigApple);
                    }

                    @Override
                    public void onError(Throwable t) {
                        callback.onError(t);
                    }
                });
            }
        };*/
        final Observable<T> souce = this;
        return new Observable<R>() {
            @Override
            public void subscribe(final ApiWrapper4.Callback<R> callback) {
                souce.subscribe(new ApiWrapper4.Callback<T>() {
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

    public <R> Observable<R> flatMap(final Func<T, Observable<R>> func) {
        /*Observable<String> saveObservable = new Observable<String>() {
            @Override
            public void subscribe(final ApiWrapper4.Callback<String> callback) {
                appleObservable.subscribe(new ApiWrapper4.Callback<Apple>() {
                    @Override
                    public void onSuccess(Apple bigApple) {
                        System.out.println(TAG + "start save apple.......................");
                        api.save(bigApple).subscribe(new ApiWrapper4.Callback<String>() {
                            @Override
                            public void onSuccess(String s) {
                                callback.onSuccess(s);
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
        final Observable<T> souce = this;
        return new Observable<R>() {
            @Override
            public void subscribe(final ApiWrapper4.Callback<R> callback) {
                souce.subscribe(new ApiWrapper4.Callback<T>() {
                    @Override
                    public void onSuccess(T t) {
                        func.call(t).subscribe(new ApiWrapper4.Callback<R>() {
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
