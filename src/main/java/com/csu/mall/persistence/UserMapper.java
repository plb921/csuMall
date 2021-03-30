package com.csu.mall.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csu.mall.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
}
