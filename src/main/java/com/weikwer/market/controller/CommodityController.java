package com.weikwer.market.controller;

import com.weikwer.market.Utils.Page;
import com.weikwer.market.bean.Commodity;
import com.weikwer.market.common.bean.Result;
import com.weikwer.market.service.CommodityService;
import com.weikwer.market.service.GetMateDataService;
import com.weikwer.market.service.OrdersService;
import com.weikwer.market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Controller
@RestController
@RequestMapping(value = "/commodity")
public class CommodityController extends BaseController{
    @Autowired
    private GetMateDataService getMateDataService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    CommodityService commodityService;

    @RequestMapping(value = "getcommodities", method = RequestMethod.POST)
    public Result<Page<Commodity>> getCommodities(@RequestBody Map<String,String> map){
        Commodity commodity=new Commodity();
        Integer curpageI=IngegerWithNull(map.get("curpage"));
        Integer pageS = IngegerWithNull(map.get("pagesize"));
        if(curpageI==null||curpageI<1||pageS==null||pageS<1) return new Result<Page<Commodity>>(1).setData(new Page<Commodity>());
        return new Result<Page<Commodity>>(1).setData(commodityService.select(commodity,curpageI,pageS));
    }


    @RequestMapping(value = "getcommodity", method = RequestMethod.POST)
    public Result<Commodity> getCommodity(@RequestBody Map<String,String> map){
        Commodity commodity=new Commodity();
        Long goodsId=LongWithNull(map.get("goodsId"));

        if(goodsId==null||goodsId<1) return new Result<Commodity>(0).setData(new Commodity());
        return new Result<Commodity>(1).setData(commodityService.selectOne(goodsId));
    }


}
