package com.weikwer.market.service;

import com.weikwer.market.Utils.Page;
import com.weikwer.market.bean.CartItem;
import com.weikwer.market.bean.Shop_cart;
import com.weikwer.market.mapper.Shop_cartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class Shop_cartService extends BaseService{

    @Autowired
    Shop_cartMapper shop_cartMapper;

    @Transactional
    public int deleteByUserIdAndOrderId(Shop_cart record){
        return shop_cartMapper.deleteByUserIdAndOrderId(record);
    }

    @Transactional
    public Page select(Shop_cart shop_cart, int currPage, int pageSize){
        return this.selectUtil(shop_cart,currPage,pageSize,shop_cartMapper);
    }

    long ifOrderExistInShopCart(long userId,long goodsId){
        Map shopcartMap=new HashMap<String,Object>();
        shopcartMap.put("userId",userId);
        shopcartMap.put("goodsId",goodsId);
        Long orderIdret=shop_cartMapper.ifOrderExistInShopCart(shopcartMap);
        if(orderIdret==null) return -1;
        else return orderIdret;
    }

    int insert(long userId,long orderId){
        if(userId<=0||orderId<=0) return 0;

        else {
            Shop_cart shop_cart=new Shop_cart();
            shop_cart.setUserId(userId);
            shop_cart.setOrderId(orderId);
            return shop_cartMapper.insert(shop_cart);
        }
    }

    @Transactional
    public ArrayList<CartItem> selectCartItem(long userId){
         if(userId>0) return shop_cartMapper.selectCartItem(userId);
         return new ArrayList<CartItem>();
    }

    /**
     * goodsIds是用逗号隔开的id
     * @param userId
     * @param goodsIds
     * @return
     */
    @Transactional
    public ArrayList<CartItem> selectCartItemWithIds(long userId,String goodsIds){
        if(userId<=0) return new ArrayList<CartItem>();
        ArrayList<CartItem> ret=new ArrayList<CartItem>();
        String[] goodsIdList = goodsIds.split(",");
        for(String str:goodsIdList){

            if(str==null||str.equals("")){

            }else{
                long goodsId2=Long.valueOf(str);
                if(goodsId2<=0) return ret;
                Map<String ,Object> map=new HashMap<String ,Object>();
                map.put("userId",userId);
                map.put("goodsId",goodsId2);

                CartItem cartItem=shop_cartMapper.selectCartItemOne(map);
                if(cartItem!=null){
                    ret.add(cartItem);
                }
            }
        }
         return ret;
    }
}
