package com.weikwer.market.controller;


import com.weikwer.market.Utils.Page;
import com.weikwer.market.annotation.IsLogin;
import com.weikwer.market.annotation.IsManager;
import com.weikwer.market.bean.Commodity;
import com.weikwer.market.bean.Users;
import com.weikwer.market.common.bean.Result;
import com.weikwer.market.service.CommodityService;
import com.weikwer.market.service.OrdersService;
import com.weikwer.market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RestController
public class MateDataController extends BaseController{


    @Autowired
    UserService userService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    CommodityService commodityService;

    @RequestMapping(value = "/")
    public String get(){
        return "添加orders成功";
    }

    @RequestMapping(value = "/getcommodities")
    public Result<Page<Commodity>> getCommodities(String curpage, String pagesize){
        Commodity commodity=new Commodity();
        Integer curpageI=IngegerWithNull(curpage);
        Integer pageS = IngegerWithNull(pagesize);
        if(curpageI==null||curpageI<1||pageS==null||pageS<1) return new Result<Page<Commodity>>(1).setData(new Page<Commodity>());
        return new Result<Page<Commodity>>(1).setData(commodityService.select(commodity,curpageI,pageS));
    }

    @IsLogin
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Result test(Users users){
        return new Result<Users>(1).setData(users);
    }

    @IsManager
    @RequestMapping(value = "/addcommodity", method = RequestMethod.POST)
    public int addCommodity(@RequestBody Map<String, String> map){

        if(map.get("goodsName")==null||map.get("goodsPrice")==null||map.get("goodsName").equals("")||map.get("goodsPrice").equals(""))
            return 0;
        Commodity commodity=new Commodity();
        commodity.setGoodsName(map.get("goodsName"));
        commodity.setGoodsPrice(Double.valueOf(map.get("goodsPrice")));

        return commodityService.add(commodity);
    }

    @IsManager
    @RequestMapping(value = "/changecommoditystock", method = RequestMethod.POST)
    public Result changeCommodityStock(@RequestBody Map<String, String> map){
        String[] filters={"goodsId","number","tag"};
        if(!mapfilter(filters,map)) return new Result(0);
        int n= commodityService.changeStock(Long.valueOf(map.get("goodsId")),Integer.valueOf(map.get("number")),Integer.valueOf(map.get("tag")));
        return new Result(n);

    }

    /**
     * 修改描述
     * 修改价格
     * 修改图片url
     * @param map
     * @return
     */
    @IsManager
    @RequestMapping(value = "/changecommodity", method = RequestMethod.POST)
    public Result changeCommodity(@RequestBody Map<String, String> map){

        Commodity commodity=new Commodity();
        commodity.setGoodsId(LongWithNull(map.get("goodsId")));
        commodity.setGoodsUrl(map.get("goodsUrl"));
        commodity.setGoodsPrice(DoubleWithNull(map.get("goodsPrice")));
        commodity.setGoodsDescribe(map.get("goodsDescribe"));


        return new Result(commodityService.update(commodity));

    }

    @RequestMapping(value = "/selectonecommodity", method = RequestMethod.GET)
    public Commodity selectOneCommodity(String goodsId){
        Commodity ret= commodityService.selectOne(LongWithNull(goodsId));
        if(ret==null) return new Commodity();
        else return ret;
    }


    @IsLogin
    @RequestMapping(value = "/st1", method = RequestMethod.GET)
    public int sessiontest1(HttpServletRequest request){
        HttpSession session = request.getSession();
        return (Integer) session.getAttribute("test");
    }

    @IsLogin
    @RequestMapping(value = "/st2", method = RequestMethod.GET)
    public int sessiontest2(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("test",25);
        return 1;
    }




}
