package com.csu.mall.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csu.mall.common.CommonResponse;
import com.csu.mall.common.Constant;
import com.csu.mall.entity.Cart;
import com.csu.mall.entity.Product;
import com.csu.mall.persistence.CartMapper;
import com.csu.mall.persistence.ProductMapper;
import com.csu.mall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("cartService")
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public CommonResponse<Page<Cart>> getCartList(Integer userId ,int pageNum, int pageSize){

        Page<Cart> result = new Page<>();
        result.setCurrent(pageNum);
        result.setSize(pageSize);

        result = cartMapper.selectPage(result, Wrappers.<Cart>query().eq("user_id", userId).orderByDesc("id"));
        if(result == null){
            return CommonResponse.creatForError("参数错误");
        }
        return CommonResponse.creatForSuccess(result);
    }

    @Override
    public CommonResponse<String> addCart(Integer userId, Integer productId, Integer quantity){

        Product product = productMapper.selectOne(Wrappers.<Product>query().eq("id", productId));
        Integer stock = product.getStock();
        if(stock < quantity){
            return CommonResponse.creatForError("库存不足");
        }

        Cart cart = cartMapper.selectOne(Wrappers.<Cart>query().eq("user_id",userId).eq("product_id",productId));
        if(cart != null){
            if(stock < cart.getQuantity()+quantity){
                return CommonResponse.creatForError("库存不足");
            }
            int rows = cartMapper.update(null,
                    Wrappers.<Cart>update().eq("id",cart.getId()).set("quantity",cart.getQuantity()+quantity).set("update_time", LocalDateTime.now()));
            if(rows > 0){
                return CommonResponse.creatForSuccessMesseage("已加入购物车");
            }
            return CommonResponse.creatForError("添加失败");
        }
        cart = new Cart();
        cart.setUserId(userId);
        cart.setProductId(productId);
        cart.setQuantity(quantity);
        cart.setChecked(Constant.CartChecked.MISCHOOSING);
        cart.setCreateTime(LocalDateTime.now());
        cart.setUpdateTime(LocalDateTime.now());
        int rows = cartMapper.insert(cart);
        if(rows == 0){
            return CommonResponse.creatForError("添加失败");
        }
        return CommonResponse.creatForSuccessMesseage("已加入购物车");
    }

    @Override
    public CommonResponse<String> updateQuantity(Integer id, Integer quantity){

        if(quantity < 1){
            return CommonResponse.creatForError("商品数不得小于一件");
        }
        Cart cart = cartMapper.selectOne(Wrappers.<Cart>query().eq("id",id));
        if(cart == null){
            return CommonResponse.creatForError("购物车不存在");
        }
        Product product = productMapper.selectById(cart.getProductId());
        Integer stock = product.getStock();
        if(stock < quantity){
            return CommonResponse.creatForError("库存不足");
        }
        int rows = cartMapper.update(null,
                Wrappers.<Cart>update().eq("id", id).set("quantity", quantity).set("update_time", LocalDateTime.now()));
        if(rows > 0){
            return CommonResponse.creatForSuccessMesseage("");
        }
        return CommonResponse.creatForError("添加失败");
    }

    @Override
    public CommonResponse<String> deleteCart(Integer id){
        int rows = cartMapper.deleteById(id);
        if(rows == 0){
            return CommonResponse.creatForError("删除购物车失败");
        }
        return CommonResponse.creatForSuccessMesseage("购物车已删除");
    }
}
