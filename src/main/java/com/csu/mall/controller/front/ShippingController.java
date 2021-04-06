package com.csu.mall.controller.front;

import com.csu.mall.common.CommonResponse;
import com.csu.mall.common.Constant;
import com.csu.mall.entity.Shippping;
import com.csu.mall.entity.User;
import com.csu.mall.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @GetMapping("list")
    @ResponseBody
    public CommonResponse<List<Shippping>> getShippingList(HttpSession session){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return CommonResponse.creatForError("用户未登录");
        }
        return shippingService.getShippingList(user.getId());
    }

    @PostMapping("add")
    @ResponseBody
    public CommonResponse<String> addShipping(HttpSession session, Shippping shippping){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return CommonResponse.creatForError("用户未登录");
        }
        return shippingService.addShipping(shippping);
    }

    @PostMapping("update")
    @ResponseBody
    public CommonResponse<String> updateShipping(HttpSession session, Integer id, String type, String str){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return CommonResponse.creatForError("用户未登录");
        }
        return shippingService.updateShipping(id, type, str);
    }

    @PostMapping("delete")
    @ResponseBody
    public CommonResponse<String> deleteShipping(HttpSession session, Integer id){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return CommonResponse.creatForError("用户未登录");
        }
        return shippingService.deleteShipping(id);
    }
}
