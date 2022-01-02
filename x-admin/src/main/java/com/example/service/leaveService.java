package com.example.service;

import com.example.entity.leave;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.leaveMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class leaveService extends ServiceImpl<leaveMapper, leave> {

    @Resource
    private leaveMapper leaveMapper;

}
