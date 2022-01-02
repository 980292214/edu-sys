package com.example.service;

import com.example.entity.major;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.majorMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class majorService extends ServiceImpl<majorMapper, major> {

    @Resource
    private majorMapper majorMapper;

}
