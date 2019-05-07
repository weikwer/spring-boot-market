package com.weikwer.market.mapper;

import com.weikwer.market.bean.Users;
import com.weikwer.market.bean.WechatUserInfo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;

@Repository
public interface UsersMapper extends BaseMapper{


    int insertSelective(Users record);

    int insertByOpenId(WechatUserInfo record);

    int insertByTelNum(Users record);


    @Override
    ArrayList<Users> select(Map users);

    @Override
    Integer selectCount(Map users);

    @Override
    Integer updateWithMap(Map map);

}