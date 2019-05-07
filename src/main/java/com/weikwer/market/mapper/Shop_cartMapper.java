package com.weikwer.market.mapper;

import com.weikwer.market.bean.CartItem;
import com.weikwer.market.bean.Shop_cart;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;

@Repository
public interface Shop_cartMapper extends BaseMapper{
    int insert(Shop_cart record);

    int insertSelective(Shop_cart record);

    Long ifOrderExistInShopCart(Map map);

    int deleteByUserIdAndOrderId(Shop_cart record);

    ArrayList<CartItem> selectCartItem(Long userId);

    CartItem selectCartItemOne(Map map);

    @Override
    ArrayList select(Map map);

    @Override
    Integer selectCount(Map users);

    @Override
    Integer updateWithMap(Map map);
}