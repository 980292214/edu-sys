package com.example.service;

import com.example.entity.category;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.categoryMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class categoryService extends ServiceImpl<categoryMapper, category> {

    @Resource
    private categoryMapper categoryMapper;

}
