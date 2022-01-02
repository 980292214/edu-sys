package com.example.service;

import com.example.entity.resource;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.resourceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class resourceService extends ServiceImpl<resourceMapper, resource> {

    @Resource
    private resourceMapper resourceMapper;

}
