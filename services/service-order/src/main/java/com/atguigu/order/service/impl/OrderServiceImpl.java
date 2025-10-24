package com.atguigu.order.service.impl;

import com.atguigu.order.bean.Order;
import com.atguigu.order.service.OrderService;
import com.atguigu.product.bean.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    RestTemplate restTemplate;

    @Override
    public Order createOrder(Long userId, Long productId) {
        Product product = getProductFromRemote(productId);
        Order order = new Order();
        order.setId(1L);
        //总金额
        order.setTotalAmount(new BigDecimal(String.valueOf(product.getPrice().multiply(new BigDecimal(product.getNum())))));
        order.setUserId(userId);
        order.setNickName("zhangsan");
        order.setAddress("地球");
        //远程调用商品列表
        order.setProductList(Arrays.asList(product));
        return order;
    }

    private Product getProductFromRemote(Long productId) {
        //TODD 远程调用商品服务
        //1 获取商品服务所在的所有机器IP+port
        List<ServiceInstance> instances = discoveryClient.getInstances("service-product");

        ServiceInstance instance = instances.get(0);
        //远程URL
        String url ="http://" + instance.getHost() + ":" + instance.getPort() + "/product/" + productId;
        log.info("远程请求：{}", url);
        //2 给远程发送结果
        Product product = restTemplate.getForObject(url, Product.class);
        return product;
    }
    //TODO 完成负载均衡发送请求
}
