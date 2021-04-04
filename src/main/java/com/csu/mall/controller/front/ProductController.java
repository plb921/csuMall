package com.csu.mall.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csu.mall.common.CommonResponse;
import com.csu.mall.entity.Product;
import com.csu.mall.service.ProductService;
import com.csu.mall.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping("list")
    @ResponseBody
    public CommonResponse<Page<Product>> getProductList(
            @RequestParam(name = "categoryId", required = false) Integer categoryId,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(name = "pageSize", defaultValue = "2") int pageSize,
            @RequestParam(name = "orderBy", defaultValue = "") String orderBy){
        return productService.getProductList(categoryId, keyword, pageNum, pageSize, orderBy);
    }

    @GetMapping("detail")
    @ResponseBody
    public CommonResponse<ProductVO> getProductDetail(Integer productId){
        return productService.getProductDetail(productId);
    }
}
