package com.csu.mall.service;

import com.csu.mall.common.CommonResponse;
import com.csu.mall.entity.Shippping;

import java.util.List;

public interface ShippingService {

    CommonResponse<List<Shippping>> getShippingList(Integer userId);

    CommonResponse<String> addShipping(Shippping shippping);

    CommonResponse<String> updateShipping(Integer id, String type, String str);

    CommonResponse<String> deleteShipping(Integer id);
}
