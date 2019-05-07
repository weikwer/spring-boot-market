package com.weikwer.market.mapper;

import com.weikwer.market.bean.Address;

import java.util.ArrayList;
import java.util.Map;

public interface AddressMapper extends BaseMapper{
    int insert(Address record);

    int insertSelective(Address record);

    int deleteByAddrId(Long addrId);

    @Override
    ArrayList select(Map map);

    @Override
    Integer selectCount(Map users);

    @Override
    Integer updateWithMap(Map map);
}