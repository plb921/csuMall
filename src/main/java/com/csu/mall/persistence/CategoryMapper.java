package com.csu.mall.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csu.mall.entity.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMapper extends BaseMapper<Category> {
}
