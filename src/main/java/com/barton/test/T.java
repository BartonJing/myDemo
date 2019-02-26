package com.barton.test;

import java.math.BigDecimal;

public class T {
    public static void main(String  []  args){
        long l = Runtime.getRuntime().totalMemory();
        System.out.println(l);
        float a =  3.4f;
        aa:for(int i=0;i<10;i++){
            break aa;
        }
    }
}
