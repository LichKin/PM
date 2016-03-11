package com.ricky.pm.utils;

/**
 * Created by liqi on 16/3/10.
 */
public class StringUtils {

    public static boolean isEmpty(String... args){
        if(args==null){
            return true;
        }
        int len = args.length;
        for(int i=0;i<len;i++){
            if(args[i].equalsIgnoreCase("")){
                return true;
            }
        }

        return false;
    }
}
