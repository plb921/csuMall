package com.csu.mall.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csu.mall.entity.Cart;
import org.springframework.stereotype.Repository;

@Repository
public interface CartMapper extends BaseMapper<Cart> {
}
