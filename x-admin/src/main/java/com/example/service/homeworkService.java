package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.homework;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.homeworkMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class homeworkService extends ServiceImpl<homeworkMapper, homework> {

    @Resource
    private homeworkMapper homeworkMapper;

    public Object commit(homework homework) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("courseid",homework.getCourseid());
        wrapper.eq("studentid",homework.getStudentid());
        homework.setState("已提交");
        int rows = homeworkMapper.update(homework, wrapper);
        return rows;
    }
}
