package com.csu.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csu.mall.common.CommonResponse;
import com.csu.mall.entity.Product;
import com.csu.mall.vo.ProductVO;

public interface ProductService {

    CommonResponse<ProductVO> getProductDetail(Integer productId);

    CommonResponse<Page<Product>> getProductList(
            Integer categoryId, String keyword, int pageNum, int pageSize, String orderBy);

}
