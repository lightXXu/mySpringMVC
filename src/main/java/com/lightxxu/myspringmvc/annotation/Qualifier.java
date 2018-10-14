package com.lightxxu.myspringmvc.annotation;

import java.lang.annotation.*;

//依赖注入
@Documented
@Target(ElementType.FIELD)//作用于字段
@Retention(RetentionPolicy.RUNTIME)

public @interface Qualifier {
    public String value();

}
