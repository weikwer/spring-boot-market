package com.weikwer.market.mapper;

import com.weikwer.market.bean.MateData;
import org.springframework.stereotype.Service;

@Service
public class MateDataDaoImpl {


    public MateData get(){
        MateData mateData=new MateData();
        mateData.setDate("2019");
        mateData.setComment("无");
        mateData.setPictureurl("baidu.com");
        mateData.setTitle("matedata");
        return mateData;
    }
}
