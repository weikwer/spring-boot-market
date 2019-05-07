package com.weikwer.market.service;

import com.weikwer.market.Utils.CommonFuns;
import com.weikwer.market.Utils.Page;
import com.weikwer.market.bean.Users;
import com.weikwer.market.bean.WechatUserInfo;
import com.weikwer.market.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;


@Service
public class UserService extends BaseService{
    @Autowired
    UsersMapper usersMapper;

    @Transactional
    public Integer insertByOpenId(WechatUserInfo wechatUserInfo){
        if(wechatUserInfo.getOpenId()==null || wechatUserInfo.getOpenId().equals("")){//openId不能为空
            return 0;
        }

        int i=usersMapper.insertByOpenId(wechatUserInfo);
        if(i==0) return 0;
        else{
            Users users=new Users();
            users.setOpenId(wechatUserInfo.getOpenId());
            users.setRegDate(new Date());
            return this.updateUtil(users,usersMapper);
        }
    }

    @Transactional
    public Page select(Users users,int currPage,int pageSize){
        return this.selectUtil(users,currPage,pageSize,usersMapper);
    }

    public Users selectOne(Users users){
        return (Users)this.selectSingle(users,usersMapper);
    }

    @Transactional
    public Integer addUserByPhone(Users users){
        String TelNum= CommonFuns.strNullOrEmpty(users.getUserTelNum());
        if(TelNum.equals("")) {
            //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return 0;
        }
        if(isTelNum(TelNum)){
            users.setRegDate(new Date());
            return usersMapper.insertByTelNum(users);
        }else{
            return 0;
        }
    }

    @Transactional
    public int update(Users users){
        return this.updateUtil(users,usersMapper);
    }

    /**
     * 通过电话和密码注册用户
     * @param userTelNum
     * @param userPwd
     * @return
     */
    @Transactional
    public int register(String userTelNum,String userPwd){
        Users users=new Users();
        users.setUserPwd(userPwd);
        users.setUserTelNum(userTelNum);
        return usersMapper.insertByTelNum(users);
    }

    public boolean isTelNum(String telNum){
        return true;
    }

    /**
     * 先判断是否有该openid
     * @param map
     * @return
     */
    public Users LoginWithOpenid(Map map) {
        Users users=new Users();
        users.setOpenId((String) map.get("openId"));
        users = selectOne(users);
        if(users!=null) return users;
        else{

            WechatUserInfo wechatUserInfo=new WechatUserInfo();
            wechatUserInfo.setOpenId((String)map.get("openId"));
            wechatUserInfo.setWechatAvatarUrl((String)map.get("avatarUrl"));
            wechatUserInfo.setWechatCity((String)map.get("city"));
            wechatUserInfo.setWechatCountry((String)map.get("country"));
            wechatUserInfo.setWechatNickname((String)map.get("nickName"));
            wechatUserInfo.setGender((Integer) map.get("gender"));
            wechatUserInfo.setWechatProvince((String)map.get("province"));

            insertByOpenId(wechatUserInfo);
            users=new Users();
            users.setOpenId((String) map.get("openId"));
            users = selectOne(users);

            return users;
        }
    }
}
