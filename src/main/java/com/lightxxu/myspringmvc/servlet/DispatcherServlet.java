package com.lightxxu.myspringmvc.servlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "dispatcherServlet", urlPatterns = "/*", loadOnStartup = 1,
        initParams = {@WebInitParam(name = "base-package", value = "com.lightxxu.myspringmvc")})
public class DispatcherServlet extends HttpServlet {
    //扫描的基包
    private String basePackag = "";
    //基包下面所有的带包路径仅限定类名
    private List<String> packageNames = new ArrayList<>();
    //注解实例化 注解上的名称是实例化对象
    private Map<String, Object> instanceMap = new HashMap<>();
    //带包路径的权限定名称;注解上的名称
    private Map<String, String> nameMap = new HashMap<>();
    //URL地址与方法的映射关系 springMVC就是方法调用链
    private Map<String, Method> urlMethodMap = new HashMap<>();
    //Method 和权限定类名映射关系，通过Method找到该对象的方法利用反射执行
    private Map<Method, String> methodPackageMap = new HashMap<>();

}
