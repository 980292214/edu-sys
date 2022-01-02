package com.example.service;

import com.example.entity.teachers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.teachersMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class teachersService extends ServiceImpl<teachersMapper, teachers> {

    @Resource
    private teachersMapper teachersMapper;

}
