package com.weikwer.market.aop;


import com.weikwer.market.bean.Users;
import com.weikwer.market.common.bean.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LoginHelper {

    @Pointcut(value = "execution(public * com.weikwer.market.controller.*Controller.*(..)) && @annotation(com.weikwer.market.annotation.IsLogin)")
    public void userPointcut(){

    }

    @Pointcut(value = "execution(public * com.weikwer.market.controller.*Controller.*(..)) && @annotation(com.weikwer.market.annotation.IsManager)")
    public void managerPointcut(){

    }

    @Around("userPointcut()")
    public Object isLogin(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Object loginO=request.getSession().getAttribute("login");
        int logins=loginO !=null?(int)loginO:0;
        if(logins>0){
            Object args[]=proceedingJoinPoint.getArgs();
            for(int i=0;i<args.length;i++){
                if(args[i].getClass() == Users.class){
                    args[i]=(Users)request.getSession().getAttribute("user");
                }
            }
            Object o=proceedingJoinPoint.proceed(args);
            return o;
        }

        return new Result<>(0).setDescription("未登录或权限不够");
    }


    @Around("managerPointcut()")
    public Object ManagerLogin(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Object loginO=request.getSession().getAttribute("login");
        int logins=loginO !=null?(int)loginO:0;
        if(logins>0){
            Users users=(Users)request.getSession().getAttribute("user");
            if(users==null) return new Result<>(0).setDescription("非管理员，无此操作权限");
            //if(user==null || users.getUserType()!=2)  return new Result<>(0).setDescription("非管理员，无此操作权限");
            Object args[]=proceedingJoinPoint.getArgs();
            for(int i=0;i<args.length;i++){
                if(args[i].getClass() == Users.class){
                    args[i]=users;
                }
            }
            Object o=proceedingJoinPoint.proceed(args);
            return o;
        }

        return new Result<>(0);
    }

}
