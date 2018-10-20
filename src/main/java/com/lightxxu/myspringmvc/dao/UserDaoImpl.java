package com.lightxxu.myspringmvc.dao;

import com.lightxxu.myspringmvc.annotation.Repository;

@Repository("userDaoImpl")
public class UserDaoImpl implements UserDAO{

    @Override
    public void insert() {
        System.out.println("插入数据");
    }
}
