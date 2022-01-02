package com.example.service;

import com.example.entity.product;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.productMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class productService extends ServiceImpl<productMapper, product> {

    @Resource
    private productMapper productMapper;

}
