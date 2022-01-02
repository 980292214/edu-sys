package com.example.service;

import com.example.entity.select;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.selectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class selectService extends ServiceImpl<selectMapper, select> {

    @Resource
    private selectMapper selectMapper;

}
