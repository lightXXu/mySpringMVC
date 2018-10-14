package com.lightxxu.myspringmvc.annotation;

import java.lang.annotation.*;


@Documented //JAVADOC
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {

    /**
     * 有一个value属性，就是controller的名字
     * @return
     */
    public String value();

}
