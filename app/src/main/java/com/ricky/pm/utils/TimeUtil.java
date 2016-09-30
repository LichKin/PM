package com.ricky.pm.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liqi on 16/9/30.
 */
public class TimeUtil {

    public static String timestamp2Date(String stamp){
        long longtime = Long.valueOf(stamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(longtime));
    }

}
