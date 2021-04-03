package com.csu.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@TableName("csumall_category")
public class Category {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer parentId;
    private String name;
    private Boolean status;
    private Integer sortOrder;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() == o.getClass()) return false;
        Category category = (Category)o;
        return Objects.equals(id,category.getId());
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }
}
