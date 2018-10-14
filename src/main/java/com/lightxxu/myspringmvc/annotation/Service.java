package com.lightxxu.myspringmvc.annotation;


import java.lang.annotation.*;

//业务层注解
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
    public String value();
}
