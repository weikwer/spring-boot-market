package com.weikwer.market.service;

import com.weikwer.market.Utils.Page;
import com.weikwer.market.Values;
import com.weikwer.market.bean.*;
import com.weikwer.market.common.bean.Result;
import com.weikwer.market.mapper.Order_detailMapper;
import com.weikwer.market.mapper.OrdersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


import java.util.*;

@Service
public class OrdersService extends BaseService{
    @Autowired
    UserService userService;

    @Autowired
    CommodityService commodityService;

    @Autowired
    OrdersMapper ordersMapper;

    @Autowired
    Order_detailMapper order_detailMapper;

    @Autowired
    Shop_cartService shop_cartService;

    @Autowired
    Order_detailService order_detailService;

    int deleteByOrderId(Orders record){
        if(record.getOrderId()==null||record.getOrderId()<1) return 0;
        return ordersMapper.deleteByOrderId(record);
    }

    @Transactional
    public Result add(long userId, long goodsId, String order_context, int number){//将货物添加进购物车

        if(userId<0||goodsId<0||order_context==null||number<=0) return new Result(0).setDescription("参数不对");

        Date date=new Date();
        Users users=new Users();
        users.setUserId(userId);
        Orders orders=new Orders();
        orders.setOrderDate(date);
        orders.setOrderUser((int) userId);
        orders.setOrderContext(order_context);
        if(userService.select(users,1,1).getRows()<1){//用户不存在
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new Result(0).setDescription("用户不存在");
        };

        orders.setOrderStatus(Values.unpay);

        Commodity commodityForSelect=new Commodity();
        commodityForSelect.setGoodsId(goodsId);
        Page<Commodity> pg=commodityService.select(commodityForSelect,1,1);
        if(pg.getRows()<1){//商品不存在
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new Result(0).setDescription("商品不存在");
        }

        Commodity commodityForPriceAndStock;
        double price = (commodityForPriceAndStock=pg.getData().get(0))!=null?commodityForPriceAndStock.getGoodsPrice():-10.0;
        if(price<0) {//价格不存在或有问题
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new Result(0).setDescription("价格不存在或有问题");
        }


        int goods_stock=commodityForPriceAndStock.getGoodsStock()==null?-1:commodityForPriceAndStock.getGoodsStock();
        if(goods_stock<0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new Result(0).setDescription("商品容量不足");
        }

        int goods_sale_Vol=commodityForPriceAndStock.getGoodsSalesVolume()==null?-1:commodityForPriceAndStock.getGoodsSalesVolume();

        if(goods_stock<number) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new Result(0).setDescription("商品容量不足");
        }

        long orderIdExist=shop_cartService.ifOrderExistInShopCart(userId,goodsId);
        if(orderIdExist>=1){//订单存在
            Orders ordersForExist=new Orders();
            ordersForExist.setOrderId(orderIdExist);
            Page<Orders> ordersPageForExist=select(ordersForExist,1,1);
            if(ordersPageForExist.getRows()!=1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new Result(0).setDescription("查找订单失败");
            }
            ordersForExist=ordersPageForExist.getData().get(0);
            Order_detail order_detail=new Order_detail();
            order_detail.setOrderId(orderIdExist);
            Page<Order_detail> order_detailPageForExist=order_detailService.select(order_detail,1,1);
            if(ordersPageForExist.getRows()!=1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new Result(0).setDescription("查找订单详情失败");
            }
            order_detail=order_detailPageForExist.getData().get(0);

            int numberForExist=order_detail.getGoodsNumber()+number;
            double priceForExist=ordersForExist.getOrderPrice()+number*price;

            Orders orders1ForExisrAndInsert = new Orders();
            orders1ForExisrAndInsert.setOrderPrice(priceForExist);
            orders1ForExisrAndInsert.setOrderId(orderIdExist);
            if(update(orders1ForExisrAndInsert)<1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new Result(0).setDescription("更新订单价格失败");
            }

            Commodity commodityForExistAndInsert=new Commodity();
            commodityForExistAndInsert.setGoodsId(goodsId);
            commodityForExistAndInsert.setGoodsStock(goods_stock-number);
            //commodityForExistAndInsert.setGoodsSalesVolume(goods_sale_Vol+number);
            if(commodityService.update(commodityForExistAndInsert)<1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new Result(0).setDescription("更新商品库存失败");
            }

            Order_detail order_detailForExistAndInsert=new Order_detail();
            order_detailForExistAndInsert.setOrderId(orderIdExist);
            order_detailForExistAndInsert.setGoodsNumber(numberForExist);
            if(order_detailService.update(order_detailForExistAndInsert)<1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new Result(0).setDescription("更新商品详情中的数量失败");
            }

            return new Result(1).setDescription("add 成功");

        }else{//相关shop cart信息不存在
            Shop_cart shop_cartForInsert=new Shop_cart();
            shop_cartForInsert.setUserId(userId);
            double priceSum=price*number;
            orders.setOrderPrice(priceSum);
            orders.setPaymentMethod(Values.PaymentMethodUnPay);
            orders.setLogisticsCompany("未发货");
            orders.setLogisticsPrice(0.0);
            orders.setDeleteFlag(0);
            String uuid= UUID.randomUUID().toString().replaceAll("\\-","");
            orders.setOrderUuid(uuid);
            ordersMapper.insert(orders);

            Order_detail order_detail=new Order_detail();
            order_detail.setGoodsId(goodsId);
            order_detail.setGoodsNumber(number);

            Orders ordersTemp=new Orders();
            ordersTemp.setOrderUuid(uuid);
            long orderId=((Page<Orders>)this.select(ordersTemp,1,1)).getData().get(0).getOrderId();
            order_detail.setOrderId(orderId);
            shop_cartForInsert.setOrderId(orderId);


            Commodity commodityForSetStock=new Commodity();
            commodityForSetStock.setGoodsStock(goods_stock-number);
            commodityForSetStock.setGoodsId(goodsId);
            //commodityForSetStock.setGoodsSalesVolume(goods_sale_Vol+number);
            commodityService.update(commodityForSetStock);//更新数量

            order_detailMapper.insert(order_detail);

            shop_cartService.insert(shop_cartForInsert.getUserId(),shop_cartForInsert.getOrderId());

            return new Result(1).setDescription("add 成功");
        }


    }

    @Transactional
    public int unAdd(long userId,long goodsId,int number){
        long orderId;
        if((orderId=shop_cartService.ifOrderExistInShopCart(userId,goodsId))>=1){//订单存在
            Order_detail order_detail=new Order_detail();
            order_detail.setOrderId(orderId);
            order_detail=(Order_detail)order_detailService.select(order_detail,1,1).getData().get(0);

            Commodity commodity=new Commodity();
            commodity.setGoodsId(goodsId);
            commodity=(Commodity)commodityService.select(commodity,1,1).getData().get(0);
            double goodsPrice=commodity.getGoodsPrice();
            int goodsStock=commodity.getGoodsStock();
            int numberExist=order_detail.getGoodsNumber();
            if(number<numberExist){
                Orders orders=new Orders();
                commodity=new Commodity();
                order_detail=new Order_detail();
                orders.setOrderId(orderId);
                commodity.setGoodsId(goodsId);
                order_detail.setOrderId(orderId);

                orders.setOrderPrice((numberExist-number)*goodsPrice);
                order_detail.setGoodsNumber(numberExist-number);
                commodity.setGoodsStock(goodsStock+number);

                int a1=update(orders);
                int a2=order_detailService.update(order_detail);
                int a3=commodityService.update(commodity);
                if(a1<1||a2<1||a3<1){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return 0;
                }
                return 1;


            }else if(number==numberExist){
                ArrayList<Integer> k=new ArrayList<Integer>();
                commodity=new Commodity();
                commodity.setGoodsStock(goodsStock+numberExist);
                commodity.setGoodsId(goodsId);
                commodityService.update(commodity);

                Orders orders=new Orders();
                orders.setOrderId(orderId);
                deleteByOrderId(orders);

                order_detail.setOrderId(orderId);
                order_detailService.deleteByOrderId(order_detail);

                Shop_cart shop_cart=new Shop_cart();
                shop_cart.setOrderId(orderId);
                shop_cart.setUserId(userId);
                shop_cartService.deleteByUserIdAndOrderId(shop_cart);

            }else{
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return 0;
            }
            return 1;

        }else{//订单不存在
            return 0;
        }
    }

    @Transactional
    public Page select(Orders orders, int currPage, int pageSize){
        return this.selectUtil(orders,currPage,pageSize,ordersMapper);
    }

    @Transactional
    public int update(Orders orders){
        return this.updateUtil(orders,ordersMapper);
    }

    @Transactional
    public int delWithFlag(Long orderId){
        return ordersMapper.delWithFlag(orderId);
    }

}
