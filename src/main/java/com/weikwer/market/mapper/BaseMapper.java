package com.weikwer.market.mapper;

import java.util.ArrayList;
import java.util.Map;

public interface BaseMapper<T> {
    /**
     * 通过map分页查找信息
     * map中包含begin和pageSize
     * @param map
     * @return
     */
    ArrayList<T> select(Map map);

    /**
     * 查找数量
     * @param users
     * @return
     */
    Integer selectCount(Map users);

    Integer updateWithMap(Map map);
}
