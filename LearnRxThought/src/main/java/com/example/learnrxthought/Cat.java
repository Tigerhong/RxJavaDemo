package com.example.learnrxthought;

/**
 * Description: Cat
 * Date: 2018/8/21
 *
 * @author zhengong.hong@ubtrobot.com
 */
public class Cat implements Comparable<Cat>{
    String image;
    /**
     * 可爱值
     */
    int cuteness;

    @Override
    public int compareTo(Cat another) {
        return Integer.compare(cuteness, another.cuteness);
    }
}