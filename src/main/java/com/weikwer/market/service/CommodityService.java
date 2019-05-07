package com.weikwer.market.service;

import com.weikwer.market.Utils.Page;
import com.weikwer.market.bean.Commodity;
import com.weikwer.market.mapper.CommodityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CommodityService extends BaseService{
    @Autowired
    CommodityMapper commodityMapper;

    @Transactional
    public int update(Commodity commodity){
        if(commodity.getGoodsId()==null||commodity.getGoodsId()<1) return 0;
        return commodityMapper.update(commodity);
    }

    @Transactional
    public Page select(Commodity commodity, int currPage, int pageSize){
        return this.selectUtil(commodity,currPage,pageSize,commodityMapper);
    }

    public int insert(Commodity commodity){
       return commodityMapper.insertSelective(commodity);
    }

    @Transactional
    int deleteByGoodsId(Commodity commodity){
        if(commodity.getGoodsId()==null||commodity.getGoodsId()<1) return 0;
        return commodityMapper.deleteByGoodsId(commodity);
    }


    /**
     * 修改库存
     * @param goodsId
     * @param number
     * @param tag 0 for addtion and 1 for deleton
     * @return
     */
    @Transactional
    public int changeStock(long goodsId,int number,int tag){
        Commodity commodity=new Commodity();
        commodity.setGoodsId(goodsId);
        if((commodity=(Commodity)selectSingle(commodity,commodityMapper))==null) return 0;
        int CNum=commodity.getGoodsStock();
        if(tag==0){//添加
            if(number>0){
                commodity=new Commodity();
                commodity.setGoodsId(goodsId);
                commodity.setGoodsStock(number+CNum);
                return update(commodity);
            }else{
                return 0;
            }
        }else if(tag==1){//减去
            if(number>CNum){
                return 0;
            }else{
                commodity=new Commodity();
                commodity.setGoodsId(goodsId);
                commodity.setGoodsStock(CNum-number);
                return update(commodity);
            }
        }else{
            return 0;
        }

    }

    /**
     * 添加商品
     *   商品名称--非空
     *   商品价格--非空,非负
     *   商品描述 默认""
     *   商品推荐顺序 默认 1
     *   商品图片地址 默认 ""
     * @param commodity
     * @return
     */
    public int add(Commodity commodity){
        Commodity commodityForAdd=new Commodity();
        if(commodity.getGoodsName()==null || commodity.getGoodsName().equals("")) {
            return 0;
        }else{
            commodityForAdd.setGoodsName(commodity.getGoodsName());
        }

        if(commodity.getGoodsPrice()==null || commodity.getGoodsPrice()<0){
            return 0;
        }else{
            commodityForAdd.setGoodsPrice(commodity.getGoodsPrice());
        }

        if(commodity.getGoodsDescribe()==null){
            commodityForAdd.setGoodsDescribe("");
        }else {
            commodityForAdd.setGoodsDescribe(commodity.getGoodsDescribe());
        }

        if(commodity.getGoodsUrl()==null){
            commodityForAdd.setGoodsUrl("");
        }else{
            commodityForAdd.setGoodsUrl(commodity.getGoodsUrl());
        }

        commodityForAdd.setRecommendedOrder(1);

        commodityForAdd.setAddDate(new Date());

        commodityForAdd.setGoodsStock(0);

        commodityForAdd.setGoodsSalesVolume(0);

        commodityForAdd.setDeleteFlag(0);

        return commodityMapper.insertSelective(commodityForAdd);
    }

    /**
     * 修改商品url
     * @param goodsId
     * @param url
     * @return
     */
    public int changeUrl(long goodsId,String url){
        if(url==null||url.equals("")) return 0;

        Commodity commodity=new Commodity();
        commodity.setGoodsId(goodsId);
        commodity.setGoodsUrl(url);
        return update(commodity);
    }

    /**
     * 修改商品描述
     * @param goodsId
     * @param desc
     * @return
     */
    public int changeDesc(Long goodsId,String desc){
        if(desc==null||desc.equals("")) return 0;
        Commodity commodity=new Commodity();
        commodity.setGoodsDescribe(desc);
        return update(commodity);
    }

    /**
     * 修改价格
     * @param goodsId
     * @param price
     * @return
     */
    public int changePrice(Long goodsId,double price){
        if(price<=0) return 0;
        Commodity commodity=new Commodity();
        commodity.setGoodsPrice(price);
        return update(commodity);
    }

    public Commodity selectOne(Long goodsId){
        Commodity commodity=new Commodity();
        commodity.setGoodsId(goodsId);
        return  (Commodity) this.selectSingle(commodity,commodityMapper);
    }
}
