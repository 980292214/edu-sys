package com.example.service;

import com.example.entity.admin;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.adminMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class adminService extends ServiceImpl<adminMapper, admin> {

    @Resource
    private adminMapper adminMapper;

}
