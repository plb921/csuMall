package com.csu.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csu.mall.common.CommonResponse;
import com.csu.mall.entity.Cart;

import java.util.List;

public interface CartService {

    CommonResponse<Page<Cart>> getCartList(Integer userId, int pageNum, int pageSize);

    CommonResponse<String> addCart(Integer userId, Integer productId, Integer quantity);

    CommonResponse<String> updateQuantity(Integer id, Integer quantity);

    CommonResponse<String> deleteCart(Integer id);
}
