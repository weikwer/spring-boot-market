package com.weikwer.market.controller;

import com.weikwer.market.annotation.IsLogin;
import com.weikwer.market.bean.CartItem;
import com.weikwer.market.bean.Users;
import com.weikwer.market.common.bean.Result;
import com.weikwer.market.service.OrdersService;
import com.weikwer.market.service.Shop_cartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RestController
@RequestMapping(value = "/order")
public class OrderController extends BaseController{
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private Shop_cartService shop_cartService;

    /**
     * long userId,long goodsId, String order_context,int number
     * @param map
     * @return
     */
    @IsLogin
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody Map<String, String> map,Users users){
        String[] filters={"goodsId","number"};
        if(!mapfilter(filters,map)) return new Result(0).setDescription("参数不对");;

        return ordersService.add(users.getUserId(),LongWithNull(map.get("goodsId")),"",IngegerWithNull(map.get("number")));

    }

    /**
     * long goodsId,int number
     * @param map
     * @return
     */
    @IsLogin
    @RequestMapping(value = "unadd", method = RequestMethod.POST)
    public Result unadd(@RequestBody Map<String, String> map,Users user){
        String[] filters={"goodsId","number"};
        if(!mapfilter(filters,map)) return new Result(0).setDescription("参数不对");;

        int a=ordersService.unAdd(user.getUserId(),LongWithNull(map.get("goodsId")),IngegerWithNull(map.get("number")));
        return new Result(a).setDescription("添加商品");

    }

    @IsLogin
    @RequestMapping(value = "getcartitem", method = RequestMethod.POST)
    public Result<ArrayList<CartItem>> getCartItem(Users users){
        return new Result<ArrayList<CartItem>>(1).setData(shop_cartService.selectCartItem(users.getUserId()));
        //return new ArrayList<CartItem>();
    }

    @IsLogin
    @RequestMapping(value = "getcartitemwithids", method = RequestMethod.POST)
    public Result<ArrayList<CartItem>> getCartItemWithIds(@RequestBody Map<String, String> map, Users users){
        if(map.get("goodsIds")==null) return new Result<>(0).setDescription("参数错误");
        long userId=users.getUserId();
        System.out.println("userId"+userId);
        return new Result<ArrayList<CartItem>>(1).setData(shop_cartService.selectCartItemWithIds( userId, map.get("goodsIds")));
        //return new ArrayList<CartItem>();
    }

}
