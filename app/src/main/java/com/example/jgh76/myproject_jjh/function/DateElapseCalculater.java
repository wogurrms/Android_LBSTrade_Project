package com.example.jgh76.myproject_jjh.function;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jgh76 on 2017-12-09.
 */

public class DateElapseCalculater {
    public static String calculate(String preDate_str) {
        int diffDay = 0;
        int diffHour = 0;
        int diffMin = 0;
        String result = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date preDate = dateFormat.parse(preDate_str);
            Date now = new Date();

            now = dateFormat.parse(dateFormat.format(now));

            diffDay = now.getDay() - preDate.getDay();
            diffHour = now.getHours() - preDate.getHours();
            diffMin = now.getMinutes() - preDate.getMinutes();

            if(diffDay != 0){
                result = diffDay + " 일 전";
            }else{
                if(diffHour != 0){
                    result = diffHour + " 시간 전";
                }else{
                    result = diffMin + " 분 전";
                }
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }
}
