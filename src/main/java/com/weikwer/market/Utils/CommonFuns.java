package com.weikwer.market.Utils;

import java.text.SimpleDateFormat;

public class CommonFuns {
    static public String strNullOrEmpty(String str){
        if(str==null) return "";
        else return str;
    }

    public static java.sql.Date strToDate(String strDate) {
        String str = strDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date d = null;
        try {
            d = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        java.sql.Date date = new java.sql.Date(d.getTime());
        return date;
    }
}
