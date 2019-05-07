package com.weikwer.market.mapper;

import com.weikwer.market.bean.Commodity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;

@Repository
public interface CommodityMapper extends BaseMapper{
    int insert(Commodity record);

    int insertSelective(Commodity record);

    int update(Commodity commodity);

    int deleteByGoodsId(Commodity commodity);

    ArrayList<Commodity> select(Map commodity);

    Integer selectCount(Map users);
}