package com.lightxxu.myspringmvc.controller;

import com.lightxxu.myspringmvc.annotation.Controller;
import com.lightxxu.myspringmvc.annotation.Qualifier;
import com.lightxxu.myspringmvc.annotation.RequestMapping;
import com.lightxxu.myspringmvc.service.UserService;

@Controller("userController")
@RequestMapping("/user")
public class UserController {
    private String use;

    @Qualifier("userServiceImpl")
    private UserService userService;

    @RequestMapping("/insert")
    public void insert() {
        userService.insert();
    }


}
