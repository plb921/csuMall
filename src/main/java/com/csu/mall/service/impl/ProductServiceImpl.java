package com.csu.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csu.mall.common.CommonResponse;
import com.csu.mall.common.Constant;
import com.csu.mall.common.ResponseCode;
import com.csu.mall.entity.Category;
import com.csu.mall.entity.Product;
import com.csu.mall.persistence.CategoryMapper;
import com.csu.mall.persistence.ProductMapper;
import com.csu.mall.service.CategoryService;
import com.csu.mall.service.ProductService;
import com.csu.mall.util.PropertiesUtil;
import com.csu.mall.vo.ProductVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryService categoryService;

    @Override
    public CommonResponse<Page<Product>> getProductList(Integer categoryId, String keyword, int pageNum, int pageSize, String orderBy){

        Page<Product> result = new Page<>();
        result.setCurrent(pageNum);
        result.setSize(pageSize);

        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        if(categoryId != null){
            List<Integer> categoryList = categoryService.getAllChildrenCategories(categoryId).getData();
            queryWrapper.in("category_id",categoryList);
        }
        if(StringUtils.isNotBlank(keyword)){
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
            queryWrapper.like("name", keyword);
        }
        if(StringUtils.isNotBlank(orderBy)){
            if(Constant.ProductOrderBy.ORDER_STRING.contains(orderBy)){
                String [] order = orderBy.split("_");
                if(order[1].equals("desc")){
                    queryWrapper.orderByDesc(order[0]);
                }
                else if(order[1].equals("asc")){
                    queryWrapper.orderByAsc(order[0]);
                }
            }
        }
        result = productMapper.selectPage(result, queryWrapper);
        if (result != null){
            return CommonResponse.creatForSuccess(result);
        }
        return CommonResponse.creatForError("参数错误");
    }

    @Override
    public CommonResponse<ProductVO> getProductDetail(Integer productId){
        if(productId == null){
            return CommonResponse.creatForError(
                    ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDescription());
        }
        Product product = productMapper.selectById(productId);
        if (product == null){
            return CommonResponse.creatForError("商品不存在或已删除");
        }
        if(product.getStatus() != Constant.ProductStatus.ONSALE.getCode()){
            return CommonResponse.creatForError("商品已下架");
        }
        return CommonResponse.creatForSuccess(entityToProductVO(product));
    }

    private ProductVO entityToProductVO(Product product){
        ProductVO productVO = new ProductVO();

        productVO.setId(product.getId());
        productVO.setCategoryId(product.getCategoryId());
        productVO.setName(product.getName());
        productVO.setSubtitle(product.getSubtitle());
        productVO.setMainImage(product.getMainImage());
        productVO.setSubImages(product.getSubImages());
        productVO.setDetail(product.getDetail());
        productVO.setPrice(product.getPrice());
        productVO.setStock(product.getStock());
        productVO.setStatus(product.getStatus());

        productVO.setCreateTime(product.getCreateTime().toString());
        productVO.setUpdateTime(product.getUpdateTime().toString());

        Category category = categoryMapper.selectById(product.getCategoryId());
        productVO.setParentCategoryId(category.getParentId());

        productVO.setImageHost(PropertiesUtil.getProperty("image.server.url"));
        return productVO;
    }
}
