package com.atguigu.product.service.impl;

import com.atguigu.product.bean.Product;
import com.atguigu.product.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public Product getProductById(Long productId) {
        Product product = new Product();
        product.setProductId(productId);
        product.setPrice(new BigDecimal("100"));
        product.setProductName("商品1 - "+productId);
        product.setNum(100);
        return product;
    }
}
