package com.example.operator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.R;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CombineLatestActivity extends AppCompatActivity {
    private static final String TAG = "CombineLatestActivity11";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine_latest);

        testCombineLatest();
        testCombineLatest2();
    }

    private void testCombineLatest2() {
        getUserInfoObervable(1)
                .flatMap(new Function<User, ObservableSource<User>>() {
                    @Override
                    public ObservableSource<User> apply(User user) throws Exception {
                        return fetchFriendsInfo(user);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        Log.i(TAG, "accept: 获取成功当前用户数据 用户名为:"+user.name+"  用户id："+user.id);
                        Log.i(TAG, "accept: 获取成功当前用户数据 用户名列表"+user.friendInfoList.toString());
                    }
                });
    }

    /**
     * 获取用的好友信息
     * @param user
     * @return
     */
    private Observable<User> fetchFriendsInfo(User user){
        Observable<User> userObservable = Observable.just(user);

        Observable<List<User>> listSingle = Observable.fromIterable(user.friendsIdList)
                .flatMap(new Function<String, ObservableSource<User>>() {
                    @Override
                    public Observable<User> apply(String id) throws Exception {
                        return getUserInfoObervable(Integer.valueOf(id));
                    }
                }).toList().toObservable();

        return Observable.combineLatest(userObservable, listSingle, new BiFunction<User, List<User>, User>() {
            @Override
            public User apply(User user, List<User> users) throws Exception {
                Log.i(TAG, " 找到当前user的好友信息: ");
                user.setFriendInfoList(users);
                return user;
            }
        }).delay(4,TimeUnit.SECONDS);
    }

    /**
     * 根据id获取用户信息
     * @param id
     * @return
     */
    private Observable<User> getUserInfoObervable(int id){
        return Observable.just(id)
                .map(new Function<Integer, User>() {
                    @Override
                    public User apply(Integer integer) throws Exception {
                        User user = new User("姓名为:" + integer, integer);
                            List<String>list= Arrays.asList(integer+"1",integer+"2",integer+"3",integer+"4");
                            user.setFriendsIdList(list);
                        return user;
                    }
                }).delay(2,TimeUnit.SECONDS);
    }


    void testCombineLatest() {
        Observable<Integer> integerObservable = Observable
                .just(1, 2, 3,4,5).delay(1, TimeUnit.SECONDS);

        Observable<String> stringObservable = Observable
                .just("A", "B", "C").delay(2, TimeUnit.SECONDS);

        Observable.combineLatest(integerObservable, stringObservable, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return "数字："+integer+"和："+s+"合并";
            }
        }).observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<String>() {
                @Override
                public void accept(String s) throws Exception {
                    Log.i(TAG, "accept: "+s);
                }
            });
    }

    class  User{
        public String name;
        public int id;

        public List<String>friendsIdList;
        public List<User>friendInfoList;
        public User(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public List<String> getFriendsIdList() {
            return friendsIdList;
        }

        public void setFriendsIdList(List<String> friendsIdList) {
            this.friendsIdList = friendsIdList;
        }

        public List<User> getFriendInfoList() {
            return friendInfoList;
        }

        public void setFriendInfoList(List<User> friendInfoList) {
            this.friendInfoList = friendInfoList;
        }
    }
}
