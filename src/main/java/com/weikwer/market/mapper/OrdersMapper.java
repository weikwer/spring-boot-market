package com.weikwer.market.mapper;

import com.weikwer.market.bean.Orders;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;

@Repository
public interface OrdersMapper extends BaseMapper{
    int insert(Orders record);

    int insertSelective(Orders record);

    int deleteByOrderId(Orders record);

    ArrayList<Orders> select(Map users);

    Integer selectCount(Map users);

    @Override
    Integer updateWithMap(Map map);

    Integer delWithFlag(Long orderId);
}