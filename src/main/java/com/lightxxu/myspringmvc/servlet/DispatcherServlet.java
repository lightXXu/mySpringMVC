package com.lightxxu.myspringmvc.servlet;

import com.lightxxu.myspringmvc.annotation.*;
import com.lightxxu.myspringmvc.controller.UserController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
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
    //带包路径的全限定名称;注解上的名称
    private Map<String, String> nameMap = new HashMap<>();
    //URL地址与方法的映射关系 springMVC就是方法调用链
    private Map<String, Method> urlMethodMap = new HashMap<>();
    //Method 和全限定类名映射关系，通过Method找到该对象的方法利用反射执行
    private Map<Method, String> methodPackageMap = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        basePackag = config.getInitParameter("base-package");
        try {
            //扫描基包
            scanBasePackage(basePackag);
            //将带有注解的类实例化放入map中
            instance(packageNames);
            // IOC注入
            springIOC();
            //将url和方法进行映射
            handlerUrlMethodMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void scanBasePackage(String basePackag) {
        //为了得到基包下面的url,将包名的.转换为路径的/
        URL url = this.getClass().getClassLoader().getResource(basePackag.replaceAll("\\.", "/"));
        File basePackageFile = new File(url.getPath());
        System.out.println("scan" + basePackageFile);
        File[] childFiles = basePackageFile.listFiles();
        for (File file : childFiles) {
            if (file.isDirectory()) {
                scanBasePackage(basePackag + "." + file.getName());
            } else if (file.isFile()) {
                packageNames.add(basePackag + "." + file.getName().split("\\.")[0]);
            }
        }
    }

    private void instance(List<String> packageNames) throws Exception {
        if (packageNames.size() < 1) {
            return;
        }
        for (String string : packageNames) {
            Class c = Class.forName(string);

            if (c.isAnnotationPresent(Controller.class)) {
                Controller controller = (Controller) c.getAnnotation(Controller.class);
                String name = controller.value();
                instanceMap.put(name, c.newInstance());
                nameMap.put(string, name);
                System.out.println("controller = " + string + ",value = " + name);
            } else if (c.isAnnotationPresent(Service.class)) {
                Service service = (Service) c.getAnnotation(Service.class);
                String name = service.value();
                instanceMap.put(name, c.newInstance());
                nameMap.put(string, name);
                System.out.println("service = " + string + ",value = " + name);
            }else if (c.isAnnotationPresent(Repository.class)) {
                Repository Repository = (Repository) c.getAnnotation(Repository.class);
                String name = Repository.value();
                instanceMap.put(name, c.newInstance());
                nameMap.put(string, name);
                System.out.println("Repository = " + string + ",value = " + name);
            }
        }
    }

    private void springIOC() throws IllegalAccessException {
        for (String instanceName : instanceMap.keySet()) {
            Field[] fields = instanceMap.get(instanceName).getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Qualifier.class)) {
                    String name = field.getAnnotation(Qualifier.class).value();
                    field.setAccessible(true);
                    field.set(instanceMap.get(instanceName), instanceMap.get(name));
                }
            }
        }
    }

    private void handlerUrlMethodMap() throws ClassNotFoundException {
        if (packageNames.size() < 1) {
            return;
        }

        for (String string : packageNames) {
            Class c = Class.forName(string);
            if (c.isAnnotationPresent(Controller.class)) {
                Method[] methods = c.getMethods();
                StringBuilder baseUrl = new StringBuilder();
                if (c.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = (RequestMapping) c.getAnnotation(RequestMapping.class);
                    baseUrl.append(requestMapping.value());
                }

                for (Method method : methods) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        baseUrl.append(requestMapping.value());
                        urlMethodMap.put(baseUrl.toString(), method);
                        methodPackageMap.put(method, string);
                    }
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.replaceAll(contextPath, "");

        //通过路径寻找方法
        Method method = urlMethodMap.get(path);
        if (method != null) {
            String packageName = methodPackageMap.get(method);
            String controllerName = nameMap.get(packageName);

            //拿到controller对象
            UserController userController = (UserController) instanceMap.get(controllerName);
            try {
                method.setAccessible(true);
                method.invoke(userController);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
