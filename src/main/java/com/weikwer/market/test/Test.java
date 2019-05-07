package com.weikwer.market.test;

public class Test {
    public static void main(String[] args){
        String[] a = "6,9,".split(",");
        for(String str:a)
        System.out.println(Long.valueOf(str));
    }
}
