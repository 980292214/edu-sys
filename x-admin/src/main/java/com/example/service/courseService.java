package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.course;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.courseMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class courseService extends ServiceImpl<courseMapper, course> {

    @Resource
    private courseMapper courseMapper;

    public List getCourses(String teacherId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("teacherid",teacherId);
        return courseMapper.selectList(wrapper);
    }
}
