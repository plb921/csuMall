package com.csu.mall.common;

public class Constant {
    public static final String CURRENT_USER = "currentUser";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";

    public interface Role{
        int CUSTOMER = 0;//普通用户
        int ADMIN = 1; //管理员
    }

    public interface CategoryStatus{
        Boolean USING = Boolean.TRUE; //使用
        Boolean DISCARD = Boolean.FALSE; //废弃
    }
}
