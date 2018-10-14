package com.lightxxu.myspringmvc.annotation;

import java.lang.annotation.*;

//地址映射处理注释
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    public String value();
}
