package com.csu.mall.service.impl;

import com.csu.mall.common.Constant;
import com.csu.mall.common.ResponseCode;
import com.csu.mall.util.TokenCache;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csu.mall.common.CommonResponse;
import com.csu.mall.entity.User;
import com.csu.mall.persistence.UserMapper;
import com.csu.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.ConversionServiceFactory;
import org.springframework.stereotype.Service;
import com.csu.mall.util.MD5Util;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public CommonResponse<User> login(String username, String password) {
        int rows = userMapper.selectCount(Wrappers.<User>query().eq("username",username));
        if(rows == 0){
            return CommonResponse.creatForError("用户名不存在");
        }
        String md5Password = MD5Util.md5Encrypt32Upper(password);
        User user = userMapper.selectOne(Wrappers.<User>query().eq("username",username).eq("password",password));
        if(user == null){
            return CommonResponse.creatForError("用户名或密码不正确");
        }
        user.setPassword(StringUtils.EMPTY);
        return CommonResponse.creatForSuccess(user);
    }

    @Override
    public CommonResponse<String> register(User user){
        int rows = userMapper.selectCount(Wrappers.<User>query().eq("username",user.getUsername()));
        if(rows > 0){
            return CommonResponse.creatForError("用户名已存在");
        }
        rows = userMapper.selectCount(Wrappers.<User>query().eq("email",user.getEmail()));
        if(rows > 0){
            return CommonResponse.creatForError("邮箱已存在");
        }

        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setRole(Constant.Role.CUSTOMER);
        user.setPassword(MD5Util.md5Encrypt32Upper(user.getPassword()));
        rows = userMapper.insert(user);
        if(rows == 0){
            return CommonResponse.creatForError("注册失败");
        }
        return CommonResponse.creatForSuccessMesseage("注册成功");
    }

    @Override
    public CommonResponse<String> checkValid(String str,String type){
        if(StringUtils.isNotBlank(str)){
            if(Constant.USERNAME.equals(str)){
                int rows = userMapper.selectCount(Wrappers.<User>query().eq("username",type));
                if(rows > 0) {
                    return CommonResponse.creatForError("用户名已存在");
                }
            }
            if(Constant.EMAIL.equals(str)){
                int rows = userMapper.selectCount(Wrappers.<User>query().eq("email",type));
                if(rows > 0){
                    return CommonResponse.creatForError("邮箱已存在");
                }
            }
        }
        else {
            return CommonResponse.creatForError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDescription());
        }
        return CommonResponse.creatForSuccessMesseage("字段校验成功");
    }

    @Override
    public CommonResponse<User> getUserInfo(User user){
        if(user == null){
            return CommonResponse.creatForError("用户未登录,无法获取当前用户信息");
        }
        user.setQuestion(StringUtils.EMPTY);
        user.setAnswer(StringUtils.EMPTY);
        return CommonResponse.creatForSuccess(user); //没有用户密码信息
    }

    @Override
    public CommonResponse<String> forgetGetQuestion(String username){
        int rows = userMapper.selectCount(Wrappers.<User>query().eq("username",username));
        if(rows == 0){
            return CommonResponse.creatForError("用户名不存在");
        }
        User user = userMapper.selectOne(Wrappers.<User>query().eq("username",username));
        String question = user.getQuestion();
        if(question == null){
            return CommonResponse.creatForError("该用户未设置找回密码问题");
        }
        return CommonResponse.creatForSuccess(question);
    }

    @Override
    public CommonResponse<String> forgetCheckAnswer(String username, String question, String answer){
        int rows = userMapper.selectCount(Wrappers.<User>query().eq("username",username).eq("question",question).eq("answer",answer));
        if(rows == 0){
            return CommonResponse.creatForError("问题答案错误");
        }
        TokenCache.setToken(username, RandomStringUtils.random( 10 ));
        return CommonResponse.creatForSuccess(TokenCache.getToken(username));
    }

    @Override
    public  CommonResponse<String> forgetResetPassword(String username, String passwordNew){
        String token = TokenCache.getToken(username);
        if (token == null){
            return CommonResponse.creatForError("超出时间限制，Token已失效");
        }
        User user = userMapper.selectOne(Wrappers.<User>query().eq("username",username));
        user.setPassword(MD5Util.md5Encrypt32Upper(passwordNew));
        int rows = userMapper.updateById(user);
        if(rows == 0){
            return CommonResponse.creatForError("修改密码操作失效");
        }
        return CommonResponse.creatForSuccessMesseage("修改密码操作成功");
    }

    @Override
    public  CommonResponse<String> resetPassword(User user, String passwordOld,String passwordNew){
        if(user == null){
            return CommonResponse.creatForError("不在登录状态");
        }
        user = userMapper.selectOne(Wrappers.<User>query().eq("username",user.getUsername()));
        passwordOld = MD5Util.md5Encrypt32Upper(passwordOld);
        if(!passwordOld.equals(user.getPassword())){
            return CommonResponse.creatForError("旧密码输入错误");
        }
        user.setPassword(MD5Util.md5Encrypt32Upper(passwordNew));
        user.setUpdateTime(LocalDateTime.now());
        int rows = userMapper.updateById(user);
        if(rows == 0){
            return CommonResponse.creatForError("修改密码操作失效");
        }
        return CommonResponse.creatForSuccessMesseage("修改密码操作成功");
    }

    @Override
    public CommonResponse<String> updateInfo(User user, String email, String phone, String question, String answer){
        if(user == null){
            return CommonResponse.creatForError("不在登录状态");
        }
        Integer id = user.getId();
        if(StringUtils.isNotBlank(email)){
            int rows = userMapper.update(null,Wrappers.<User>update().eq("id",id).set("email",email));
            if(rows == 0){
                return CommonResponse.creatForError("更新失败");
            }
        }
        if(StringUtils.isNotBlank(phone)){
            int rows = userMapper.update(null,Wrappers.<User>update().eq("id",id).set("phone",phone));
            if(rows == 0){
                return CommonResponse.creatForError("更新失败");
            }
        }
        if(StringUtils.isNotBlank(question)){
            int rows = userMapper.update(null,Wrappers.<User>update().eq("id",id).set("question",question));
            if(rows == 0){
                return CommonResponse.creatForError("更新失败");
            }
        }
        if(StringUtils.isNotBlank(answer)){
            int rows = userMapper.update(null,Wrappers.<User>update().eq("id",id).set("answer",answer));
            if(rows == 0){
                return CommonResponse.creatForError("更新失败");
            }
        }
        return CommonResponse.creatForSuccessMesseage("更新个人信息成功");
    }

    @Override
    public CommonResponse<User> getInfo(User user){
        if(user == null){
            return CommonResponse.creatForError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDescription());
        }
        return CommonResponse.creatForSuccess(user);
    }

    @Override
    public CommonResponse<String> logout(User user){
        if(user == null){
            return CommonResponse.creatForError("服务端异常");
        }
        return CommonResponse.creatForSuccessMesseage("退出成功");
    }

    @Override
    public CommonResponse<String> checkAdminRole(User user){
        if(user != null && user.getRole().equals(Constant.Role.ADMIN)){
            return CommonResponse.creatForSuccess();
        }
        return CommonResponse.creatForError("当前用户不是管理员");
    }

}
