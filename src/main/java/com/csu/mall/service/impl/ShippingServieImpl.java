package com.csu.mall.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csu.mall.common.CommonResponse;
import com.csu.mall.common.Constant;
import com.csu.mall.common.ResponseCode;
import com.csu.mall.entity.Shippping;
import com.csu.mall.persistence.ShippingMapper;
import com.csu.mall.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("shippingService")
public class ShippingServieImpl implements ShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public CommonResponse<List<Shippping>> getShippingList(Integer userId){
        if(userId == null){
            return CommonResponse.creatForError("参数异常");
        }
        List<Shippping> shipppingList = shippingMapper.selectList(Wrappers.<Shippping>query().eq("user_id",userId));
        if(shipppingList == null){
            return CommonResponse.creatForSuccessMesseage("还未创建任何收货地址");
        }
        return CommonResponse.creatForSuccess(shipppingList);
    }

    @Override
    public CommonResponse<String> addShipping(Shippping shippping){
        shippping.setCreateTime(LocalDateTime.now());
        shippping.setUpdateTime(LocalDateTime.now());
        int rows = shippingMapper.insert(shippping);
        if (rows == 0) {
            return CommonResponse.creatForError("添加地址失败");
        }
        return CommonResponse.creatForSuccessMesseage("添加地址成功");
    }

    @Override
    public CommonResponse<String> updateShipping(Integer id, String type, String str){
        Shippping shippping = shippingMapper.selectById(id);
        if(shippping == null){
            return CommonResponse.creatForError("参数异常");
        }
        if(StringUtils.isNotBlank(type)){
            if(Constant.ShippingUpdateType.TYPE.contains(type)){
                int rows = shippingMapper.update(
                        null, Wrappers.<Shippping>update().eq("id", id).set(type,str).set("update_time",LocalDateTime.now()));
                if (rows > 0) {
                    return CommonResponse.creatForSuccessMesseage("更新成功");
                }
                else{
                    return CommonResponse.creatForError("更新失败");
                }
            }
        }
        return CommonResponse.creatForError(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDescription());
    }

    @Override
    public CommonResponse<String> deleteShipping(Integer id){
        int rows = shippingMapper.deleteById(id);
        if(rows == 0){
            return CommonResponse.creatForError("删除地址失败");
        }
        return CommonResponse.creatForSuccessMesseage("删除地址成功");
    }

}
