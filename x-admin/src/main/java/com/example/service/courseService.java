package com.example.service;

import com.example.entity.course;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.courseMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class courseService extends ServiceImpl<courseMapper, course> {

    @Resource
    private courseMapper courseMapper;

}
