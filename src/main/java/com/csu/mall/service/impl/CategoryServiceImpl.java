package com.csu.mall.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csu.mall.common.CommonResponse;
import com.csu.mall.common.Constant;
import com.csu.mall.entity.Category;
import com.csu.mall.persistence.CategoryMapper;
import com.csu.mall.service.CategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public CommonResponse<List<Category>> getChildrenCategories(Integer categoryId){
        List<Category> catogories = categoryMapper.selectList(Wrappers.<Category>query().eq("parent_id", categoryId));
        if(CollectionUtils.isEmpty(catogories)){
            logger.info("查找子分类，没有对应的子分类");
            return CommonResponse.creatForError("未找到该品类");
        }
        return CommonResponse.creatForSuccess(catogories);
    }

    @Override
    public CommonResponse<String> addCategory(Integer parentId, String categoryName){
        int rows = categoryMapper.selectCount(Wrappers.<Category>query().eq("id", parentId));
        if(rows == 0){
            return CommonResponse.creatForError("添加品类失败，未找到父品类");
        }
        rows = categoryMapper.selectCount(Wrappers.<Category>query().eq("name",categoryName));
        if(rows > 0) {
            return CommonResponse.creatForError("添加品类失败，品类名已存在");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setStatus(Constant.CategoryStatus.USING);
        rows = categoryMapper.insert(category);
        if(rows == 0){
            return CommonResponse.creatForError("添加品类失败");
        }
        return CommonResponse.creatForSuccessMesseage("添加品类成功");
    }

    @Override
    public CommonResponse<String> setCategoryName(Integer categoryId, String categoryName){
        Category category = categoryMapper.selectOne(Wrappers.<Category>query().eq("id", categoryId));
        if(category == null){
            return CommonResponse.creatForError("更新品类名称失败，未查到该品类");
        }
        int rows = categoryMapper.update(null, Wrappers.<Category>update().eq("id",categoryId).set("name",categoryName));
        if(rows == 0){
            return CommonResponse.creatForError("更新品类名称失败");
        }
        return CommonResponse.creatForSuccessMesseage("更新品类名称成功");
    }

    @Override
    public CommonResponse<List<Integer>> getAllChildrenCategories(Integer categoryId){
        if(categoryId !=0){
            int rows = categoryMapper.selectCount(Wrappers.<Category>query().eq("id", categoryId));
            if (rows == 0){
                return CommonResponse.creatForError("品类名不存在");
            }
        }
        Set<Category> categorySet = Sets.newHashSet();
        findChildrenCategories(categoryId, categorySet);
        List<Integer> categoryIds = Lists.newArrayList();
        for(Category category : categorySet){
            categoryIds.add(category.getId());
        }
        return CommonResponse.creatForSuccess(categoryIds);
    }

    private Set<Category> findChildrenCategories(Integer categoryId, Set<Category> categorySet){
        Category category = categoryMapper.selectById(categoryId);
        if(category != null){
            categorySet.add(category);
        }

        List<Category> categoryList = categoryMapper.selectList(Wrappers.<Category>query().eq("parent_id",categoryId));
        for(Category categoryItem : categoryList){
            findChildrenCategories(categoryItem.getId(), categorySet);
        }
        return categorySet;
    }
}
