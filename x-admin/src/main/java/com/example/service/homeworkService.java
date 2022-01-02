package com.example.service;

import com.example.entity.homework;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.homeworkMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class homeworkService extends ServiceImpl<homeworkMapper, homework> {

    @Resource
    private homeworkMapper homeworkMapper;

}
