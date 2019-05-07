package com.weikwer.market.service;

import com.weikwer.market.Utils.Page;
import com.weikwer.market.mapper.BaseMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseService<T> {

    public int updateUtil(T t, BaseMapper tMapper){
        Map<String,Object> tmap=new HashMap<String,Object>();
        Field[] filed = t.getClass().getDeclaredFields();

        try {
            for(Field fd:filed){
                String fdname=fd.getName();
                String fun= "get"+fdname.substring(0,1).toUpperCase()+fdname.substring(1,fdname.length());

                tmap.put(fdname,t.getClass().getMethod(fun).invoke(t));

            }

            return tMapper.updateWithMap(tmap);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return 0;


    }


    public Page selectUtil(T t, int currPage, int pageSize, BaseMapper tMapper) {
        if(pageSize<=0) return new Page();
        Map<String,Object> tmap=new HashMap<String,Object>();
        Field[] filed = t.getClass().getDeclaredFields();
        ArrayList<T> date = null;
        int count=0;
        Page page=null;
        tmap.put("pageSize",pageSize);
        try {
              for(Field fd:filed){
                  String fdname=fd.getName();
                  String fun= "get"+fdname.substring(0,1).toUpperCase()+fdname.substring(1,fdname.length());

                  tmap.put(fdname,t.getClass().getMethod(fun).invoke(t));

              }

              count = (Integer) tMapper.selectCount(tmap);

              double totalCount = count;
              int totalPage = (int) Math.ceil(totalCount / pageSize);

              int begin = (currPage - 1) * pageSize;
              tmap.put("begin",begin);
              page=new Page<ArrayList<T>>();




              date = (ArrayList<T>)tMapper.select(tmap);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        if(page==null) return new Page();

        page.setData(date);
        page.setPagesize(pageSize);
        page.setRows(count);
        page.setPagenum(currPage);

        return page;
    }


    T selectSingle(T t,BaseMapper b){
        Page<T> p=selectUtil(t,1,1,b);
        if(p.getRows()!=1) return null;
        return p.getData().get(0);
    }

    /**
     *
     * @param t
     * @param b
     * @return  true if exist  else false
     */
    boolean selectExistUtil(T t,BaseMapper b){
        Page<T> p=selectUtil(t,1,1,b);
        if(p.getRows()>=1) return true;
        return false;
    }



}
