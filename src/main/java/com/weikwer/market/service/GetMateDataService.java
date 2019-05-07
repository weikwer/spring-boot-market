package com.weikwer.market.service;

import com.weikwer.market.bean.MateData;
import com.weikwer.market.mapper.MateDataDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetMateDataService {

    @Autowired
    private MateDataDaoImpl mateDataDao;

    public String get(){
        MateData mateData=mateDataDao.get();
        String ans="";
        ans+="title:"+mateData.getTitle()+"\n";
        ans+="comment:"+mateData.getComment()+"\n";
        ans+="picturerul:"+mateData.getPictureurl()+"\n";
        return ans;
    }



}
