package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.select;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.selectMapper;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class selectService extends ServiceImpl<selectMapper, select> {

    @Resource
    private selectMapper selectMapper;


    public List findByStudentId(String studentId) {

        List courses = selectMapper.findByStudentId(studentId);
        return courses;
    }

    public List noselectedCourse(String studentId,String major){
        List courses = selectMapper.noselectedCourse(studentId,major);
        return courses;
    }
}
