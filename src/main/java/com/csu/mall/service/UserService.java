package com.csu.mall.service;

import com.csu.mall.entity.User;
import com.csu.mall.common.CommonResponse;
import com.csu.mall.util.TokenCache;

import javax.servlet.http.HttpSession;
import javax.xml.stream.events.Comment;
import java.net.CookieManager;

public interface UserService {
    CommonResponse<User> login(String username, String password);

    CommonResponse<String> register(User user);

    CommonResponse<String> checkValid(String str,String type);

    CommonResponse<User> getUserInfo(User user);

    CommonResponse<String> forgetGetQuestion(String username);

    CommonResponse<String> forgetCheckAnswer(String username, String question, String answer);

    CommonResponse<String> forgetResetPassword(String username, String passwordNew);

    CommonResponse<String> resetPassword(User user, String passwordOld,String passwordNew);

    CommonResponse<String> updateInfo(User user, String email, String phone, String question, String answer);

    CommonResponse<User> getInfo(User user);

    CommonResponse<String> logout(User user);

    CommonResponse<String> checkAdminRole(User user);

}
