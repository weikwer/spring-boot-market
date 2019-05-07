package com.weikwer.market.controller;

import com.weikwer.market.Utils.AesCbcUtil;
import com.weikwer.market.Utils.CommonFuns;
import com.weikwer.market.Utils.HttpRequest;
import com.weikwer.market.bean.Users;
import com.weikwer.market.common.bean.Result;
import com.weikwer.market.service.UserService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RestController
@RequestMapping(value = "/users")
public class UsersController extends BaseController{
    @Autowired
    private UserService userService;

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public Result<Integer> register(@RequestBody Map<String, String> map){
        String[] filters={"userTelNum","userPwd"};
        if(!mapfilter(filters,map)) new Result<Integer>(0).setDescription("登入失败");
        Result<Integer> r=new Result<Integer>(0);
        int c=userService.register(map.get("userTelNum"),map.get("userPwd"));
        if(c==1) {
            r.setCode(1);
            r.setDescription("登入成功");
            return r;
        }else{
            return r.setDescription("登录失败");
        }


    }

    @RequestMapping(value = "edituser", method = RequestMethod.POST)
    public Result editUser(@RequestBody Map<String, String> map){
        String[] filters={"userTelNum"};
        if(!mapfilter(filters,map)) new Result<>(0);
        Users users=new Users();
        users.setUserTelNum(map.get("userTelNum"));
        users.setUserPwd(map.get("userPwd"));
        users.setUserSex(map.get("userSex"));
        users.setUserBirthday(CommonFuns.strToDate(map.get("userBirthday")));
        return new Result(userService.update(users));

    }


    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public Result logout( HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer logins=(Integer)session.getAttribute("login");
        if(logins!=null&&logins==1) {
            session.setAttribute("login",0);
            session.setAttribute("user",null);
            return new Result(1);
        } else {
            return new Result(0);
        }

    }



    /**
     * decoding encrypted data to get openid
     *
     * @param iv
     * @param encryptedData
     * @param code
     * @return
     */
    @RequestMapping(value = "decodeUserInfo", method = RequestMethod.GET)
    private Result decodeUserInfo(String iv, String encryptedData, String code) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Map map = new HashMap();
        // login code can not be null
        if (code == null || code.length() == 0) {
            map.put("status", 0);
            map.put("msg", "code 不能为空");
            return new Result(0).setDescription("code 不能为空");
        }
        // mini-Program's AppID
        String wechatAppId = "wx1ea7006d5daac450";

        // mini-Program's session-key
        String wechatSecretKey = "02d1ac761d31ad6a2d0851020d65f25b";

        String grantType = "authorization_code";

        // using login code to get sessionId and openId
        String params = "appid=" + wechatAppId + "&secret=" + wechatSecretKey + "&js_code=" + code + "&grant_type=" + grantType;

        // sending request
        String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);

        // analysis request content
        JSONObject json = JSONObject.fromObject(sr);

        // getting session_key
        String sessionKey = json.get("session_key").toString();

        // getting open_id
        String openId = json.get("openid").toString();

        // decoding encrypted info with AES
        try {
            String result = AesCbcUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");
            if (null != result && result.length() > 0) {
                map.put("status", 1);
                map.put("msg", "解密成功");

                JSONObject userInfoJSON = JSONObject.fromObject(result);
                Map userInfo = new HashMap();
                userInfo.put("openId", userInfoJSON.get("openId"));
                userInfo.put("nickName", userInfoJSON.get("nickName"));
                userInfo.put("gender", userInfoJSON.get("gender"));
                userInfo.put("city", userInfoJSON.get("city"));
                userInfo.put("province", userInfoJSON.get("province"));
                userInfo.put("country", userInfoJSON.get("country"));
                userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
                userInfo.put("unionId", userInfoJSON.get("unionId"));
                map.put("userInfo", userInfo);


                if(userService==null) return new Result(0).setDescription("userService null");
                Users users=userService.LoginWithOpenid(userInfo);
                if(users!=null){
                    request.getSession().setAttribute("login",1);
                    request.getSession().setAttribute("user",users);
                    return new Result(1).setDescription(request.getSession().getId());
                }else{
                    return new Result(0);
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("status", 0);
        map.put("msg", "解密失败");
        return new Result(0);
    }

}
