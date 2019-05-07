package com.weikwer.market.controller;

import com.weikwer.market.bean.Users;
import com.weikwer.market.common.bean.Result;
import com.weikwer.market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RestController
public class LoginManagerController extends  BaseController{
    @Autowired
    UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result<Users> login(@RequestBody Map<String, String> map, HttpServletRequest request){
        String[] filters={"telNum","password"};
        if(!mapfilter(filters,map)) return new Result<>(0).setDescription("登入失败");

        Users users=new Users();
        users.setUserPwd(map.get("password"));
        users.setUserTelNum(map.get("telNum"));
        users= userService.selectOne(users);

        if(users==null) return new Result<>(0).setDescription("登入失败");
        else {
            HttpSession session = request.getSession();
            session.setAttribute("user",users);
            session.setAttribute("login", 1);
            users.setUserPwd(null);
            Result<Users> ret=new Result<Users>(1).setData(users);
            ret.setDescription(request.getSession().getId());
            return ret;
        }
    }

    @RequestMapping(value = "/loginstatus", method = RequestMethod.GET)
    public Users loginStatus( HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer logins=(Integer)session.getAttribute("login");
        if(logins!=null&&logins==1) return (Users) session.getAttribute("user");
        else return new Users();

    }


}
