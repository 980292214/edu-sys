package com.example.service;

import com.example.entity.students;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.studentsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class studentsService extends ServiceImpl<studentsMapper, students> {

    @Resource
    private studentsMapper studentsMapper;

}
