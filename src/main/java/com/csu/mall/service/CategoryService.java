package com.csu.mall.service;

import com.csu.mall.common.CommonResponse;
import com.csu.mall.entity.Category;

import java.util.List;

public interface CategoryService {

    CommonResponse<List<Category>> getChildrenCategories(Integer categoryId);

    CommonResponse<String> addCategory(Integer parentId, String categoryName);

    CommonResponse<String> setCategoryName(Integer categoryId, String categoryName);

    CommonResponse<List<Integer>> getAllChildrenCategories(Integer categoryId);
}
