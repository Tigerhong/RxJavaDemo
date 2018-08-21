package com.example.learnrxthought;

/**
 * Description: Func
 * Date: 2018/8/21
 *因为在 Java 中不能直接传递方法（函数）所以我们需要通过类（和接口）来间接实现这样的功能
 * ，然后我们就来定义这个 “方法”
 * @author zhengong.hong@ubtrobot.com
 */
public interface Func<T,R> {
    R call(T t);
}
