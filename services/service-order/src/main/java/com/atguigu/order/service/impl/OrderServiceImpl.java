package com.atguigu.order.service.impl;

import com.atguigu.order.bean.Order;
import com.atguigu.order.service.OrderService;
import com.atguigu.product.bean.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderServiceImpl implements OrderService {


    @Override
    public Order createOrder(Long productId, Long userId) {
        Order order = new Order();
        order.setId(1L);
        //TODD 总金额
        order.setTotalAmount(new BigDecimal("0"));
        order.setUserId(userId);
        order.setNickName("zhangsan");
        order.setAddress("地球");
        //TODD 远程调用商品列表
        order.setProductList(null);
        return order;
    }

    private Product getProductFromRemote(Long productId) {
        //TODD 远程调用商品服务
        //1 获取商品服务所在的所有机器IP+port
        return null;
    }
}
