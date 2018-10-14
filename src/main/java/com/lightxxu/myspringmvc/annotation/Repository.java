package com.lightxxu.myspringmvc.annotation;

import java.lang.annotation.*;


//持久化层注释
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Repository {
    public String value();
}
