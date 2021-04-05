package com.csu.mall.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csu.mall.common.CommonResponse;
import com.csu.mall.common.Constant;
import com.csu.mall.entity.Cart;
import com.csu.mall.entity.User;
import com.csu.mall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("list")
    @ResponseBody
    public CommonResponse<Page<Cart>> getCartList(
            HttpSession session,
            @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return CommonResponse.creatForError("用户未登录");
        }
        return cartService.getCartList(user.getId(), pageNum, pageSize);
    }

    @GetMapping("add")
    @ResponseBody
    public CommonResponse<String> addCart(HttpSession session, Integer productId, Integer quantity){
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return CommonResponse.creatForError("用户未登录");
        }
        return cartService.addCart(user.getId(), productId, quantity);
    }

    @GetMapping("updateQuantity")
    @ResponseBody
    public CommonResponse<String> updateQuantity(HttpSession session, Integer id, Integer quantity){
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return CommonResponse.creatForError("用户未登录");
        }
        return cartService.updateQuantity(id, quantity);
    }

    @GetMapping("deleteCart")
    @ResponseBody
    public CommonResponse<String> deleteCart(HttpSession session, Integer id){
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return CommonResponse.creatForError("用户未登录");
        }
        return cartService.deleteCart(id);
    }
}
