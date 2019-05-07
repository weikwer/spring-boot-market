package com.weikwer.market.controller;

import java.util.Map;

public abstract class BaseController {
    /**
     * 通过返回true，否则返回false
     * @param strs
     * @param map
     * @return
     */
    public boolean mapfilter(String[] strs, Map<String, String> map){
        for(String s:strs){
            if(!map.containsKey(s)||map.get(s)==null||map.get(s).equals("")) return false;
        }
        return true;
    }

    public Double DoubleWithNull(Object o){
        try {
            if (o != null && !o.toString().equals("")) return Double.valueOf(o.toString());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public Integer IngegerWithNull(Object o){
        try{
            if(o!=null && !o.toString().equals("")) return Integer.valueOf(o.toString());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public Long LongWithNull(Object o){
        try{
            if(o!=null && !o.toString().equals("")) return Long.valueOf(o.toString());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
