package com.csu.mall.common;

import com.google.common.collect.Sets;

import java.util.Set;

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

    public enum ProductStatus{

        ONSALE(1,"在售"),
        REMOVE(2,"下架"),
        DELETE(3,"删除");

        private int code;
        private String description;

        ProductStatus(int code, String description){
            this.code = code;
            this.description = description;
        }

        public int getCode(){
            return code;
        }

        public String getDescription(){
            return description;
        }
    }

    public interface ProductOrderBy{
        Set<String> ORDER_STRING=Sets.newHashSet("price_desc", "price_asc");
    }

    public interface CartChecked{
        Boolean CHOOSING = Boolean.TRUE; //选中
        Boolean MISCHOOSING = Boolean.FALSE; //未选中
    }
}
