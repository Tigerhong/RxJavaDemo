package com.example.demo.bean;

/**
 * Description: Apple
 * Date: 2018/8/21
 *
 * @author zhengong.hong@ubtrobot.com
 */
public class Apple implements Comparable<Apple>{
    public String name;
    public int bigValue;

    public Apple(String name, int bigValue) {
        this.name = name;
        this.bigValue = bigValue;
    }

    @Override
    public int compareTo(Apple o) {
        return bigValue-o.bigValue;
    }
}
