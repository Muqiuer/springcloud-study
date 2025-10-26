package com.atguigu.order.service.impl;

import com.atguigu.order.bean.Order;
import com.atguigu.order.service.OrderService;
import com.atguigu.product.bean.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
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
    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Override
    public Order createOrder(Long userId, Long productId) {
        Product product = getProductFromRemoteWithLoadBalancerAnnotation(productId);
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
    //1 直接发送请求
    private Product getProductFromRemote(Long productId) {
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

    //2 负载均衡发送请求
    private Product getProductFromRemoteWithLoadBalancer(Long productId) {

        //1 获取商品服务所在的所有机器IP+port
        ServiceInstance instance = loadBalancerClient.choose("service-product");
        //远程URL
        String url ="http://" + instance.getHost() + ":" + instance.getPort() + "/product/" + productId;
        log.info("远程请求：{}", url);
        //2 给远程发送结果
        Product product = restTemplate.getForObject(url, Product.class);
        return product;
    }

    //3 负载均衡发送请求（注解式）
    private Product getProductFromRemoteWithLoadBalancerAnnotation(Long productId) {

        String url = "http://service-product/product/" + productId;
        //2 给远程发送结果 service-product 会被动态替换
        Product product = restTemplate.getForObject(url, Product.class);
        return product;
    }
}
