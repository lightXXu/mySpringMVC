package com.lightxxu.myspringmvc.service;

import com.lightxxu.myspringmvc.annotation.Qualifier;
import com.lightxxu.myspringmvc.annotation.Service;
import com.lightxxu.myspringmvc.dao.UserDAO;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService{

    @Qualifier("userDaoImpl")
    private UserDAO userDAO;

    @Override
    public void insert() {
        System.out.println("insert start");
        userDAO.insert();
        System.out.println("insert end");

    }
}
