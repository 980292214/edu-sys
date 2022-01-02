package com.example.service;

import com.example.entity.exam;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.examMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class examService extends ServiceImpl<examMapper, exam> {

    @Resource
    private examMapper examMapper;

}
