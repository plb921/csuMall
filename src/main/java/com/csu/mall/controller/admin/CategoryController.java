package com.csu.mall.controller.admin;

import com.csu.mall.common.CommonResponse;
import com.csu.mall.common.Constant;
import com.csu.mall.entity.Category;
import com.csu.mall.entity.User;
import com.csu.mall.service.CategoryService;
import com.csu.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin/category/")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    @GetMapping("getChildrenCategories")
    @ResponseBody
    public CommonResponse<List<Category>> getChildrenCategories(
            HttpSession session, @RequestParam(name = "categoryId", defaultValue = "0") Integer categoryId){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return CommonResponse.creatForError("用户未登录");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            return categoryService.getChildrenCategories(categoryId);
        }
        return CommonResponse.creatForError("需要管理员权限");
    }

    @GetMapping("addCategory")
    @ResponseBody
    public CommonResponse<String> addCategory(
            HttpSession session, @RequestParam(name = "parentId", defaultValue = "0") Integer parentId, String categoryName){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return CommonResponse.creatForError("用户未登录");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            return categoryService.addCategory(parentId, categoryName);
        }
        return CommonResponse.creatForError("需要管理员权限");
    }

    @GetMapping("setCategoryName")
    @ResponseBody
    public CommonResponse<String> setCategoryName(HttpSession session, Integer categoryId, String categoryName){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return CommonResponse.creatForError("用户未登录");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            return categoryService.setCategoryName(categoryId, categoryName);
        }
        return CommonResponse.creatForError("需要管理员权限");
    }

    @GetMapping("getAllChildrenCategories")
    @ResponseBody
    public CommonResponse<List<Integer>> getAllChildrenCategories(
            HttpSession session, @RequestParam(name = "categoryId", defaultValue = "0")Integer categoryId){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return CommonResponse.creatForError("用户未登录");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            return categoryService.getAllChildrenCategories(categoryId);
        }
        return CommonResponse.creatForError("需要管理员权限");
    }
}
