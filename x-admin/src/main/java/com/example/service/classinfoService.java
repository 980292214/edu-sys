package com.example.service;

import com.example.entity.classinfo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.classinfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class classinfoService extends ServiceImpl<classinfoMapper, classinfo> {

    @Resource
    private classinfoMapper classinfoMapper;

}
