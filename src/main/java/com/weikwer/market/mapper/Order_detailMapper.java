package com.weikwer.market.mapper;

import com.weikwer.market.bean.Order_detail;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;

@Repository
public interface Order_detailMapper extends BaseMapper{
    int insert(Order_detail record);

    int insertSelective(Order_detail record);

    int deleteByOrderId(Order_detail record);

    ArrayList<Order_detail> select(Map users);

    Integer selectCount(Map users);
}