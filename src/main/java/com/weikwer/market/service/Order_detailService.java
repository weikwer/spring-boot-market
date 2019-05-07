package com.weikwer.market.service;

import com.weikwer.market.Utils.Page;
import com.weikwer.market.bean.Order_detail;
import com.weikwer.market.mapper.Order_detailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Order_detailService extends BaseService{
    @Autowired
    Order_detailMapper order_detailMapper;

    @Transactional
    int deleteByOrderId(Order_detail record){
        if(record.getOrderId()==null || record.getOrderId()<1) return 0;
        return order_detailMapper.deleteByOrderId(record);
    }

    @Transactional
    public Page select(Order_detail users, int currPage, int pageSize){
        return this.selectUtil(users,currPage,pageSize,order_detailMapper);
    }

    @Transactional
    public int update(Order_detail order_detail){
        return this.updateUtil(order_detail,order_detailMapper);
    }

}
