package com.example.service;

import com.example.entity.depart;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.departMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class departService extends ServiceImpl<departMapper, depart> {

    @Resource
    private departMapper departMapper;

}
