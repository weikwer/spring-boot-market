package com.weikwer.market.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)//作用范围,方法上
@Retention(RetentionPolicy.RUNTIME)//运行时
@Documented
public @interface IsManager {
}
