package com.csu.mall.controller.front;

import com.csu.mall.common.CommonResponse;
import com.csu.mall.entity.User;
import com.csu.mall.service.UserService;
import com.csu.mall.util.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.csu.mall.common.Constant;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("login")
    @ResponseBody
    public CommonResponse<User> login(String username, String password, HttpSession session){
        CommonResponse<User> response = userService.login(username, password);
        if(response.isSuccess()){
            session.setAttribute(Constant.CURRENT_USER, response.getData());
        }
        return response;
    }

    @PostMapping("register")
    @ResponseBody
    public CommonResponse<String> register(User user){
        return userService.register(user);
    }

    @GetMapping("checkValid")
    @ResponseBody
    public CommonResponse<String> checkValid(String str,String type){
        return userService.checkValid(str, type);
    }

    @PostMapping("getUserInfo")
    @ResponseBody
    public CommonResponse<User> getUserInfo(HttpSession session){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        return userService.getUserInfo(user);
    }

    @GetMapping("forgetGetQuestion")
    @ResponseBody
    public CommonResponse<String> forgetGetQuestion(String username){

        return userService.forgetGetQuestion(username);
    }

    @PostMapping("forgetCheckAnswer")
    @ResponseBody
    public CommonResponse<String> forgetCheckAnswer(String username, String question, String answer){
        return userService.forgetCheckAnswer(username,question,answer);
    }

    @PostMapping("forgetResetPassword")
    @ResponseBody
    public CommonResponse<String> forgetResetPassword(String username,String passwordNew){
        return userService.forgetResetPassword(username, passwordNew);
    }

    @PostMapping("resetPassword")
    @ResponseBody
    public CommonResponse<String> resetPassword(String passwordOld, String passwordNew, HttpSession session){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        return userService.resetPassword(user, passwordOld, passwordNew);
    }

    @PostMapping("updateInfo")
    @ResponseBody
    public CommonResponse<String> updateInfo(String email, String phone, String question, String answer, HttpSession session){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        return userService.updateInfo(user, email, phone, question, answer);
    }

    @PostMapping("getInfo")
    @ResponseBody
    public CommonResponse<User> getInfo(HttpSession session){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        return userService.getInfo(user);
    }

    @PostMapping("logout")
    @ResponseBody
    public CommonResponse<String> logout(HttpSession session){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        CommonResponse<String> response = userService.logout(user);
        if(response.isSuccess()){
            session.invalidate();
        }
        return response;
    }

}
