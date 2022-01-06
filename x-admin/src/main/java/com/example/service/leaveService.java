package com.example.service;

import com.example.entity.leave;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.leaveMapper;
import com.example.mapper.selectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class leaveService extends ServiceImpl<leaveMapper, leave> {

    @Resource
    private leaveMapper leaveMapper;
    public List findByTeacherId(String teacherId) {

        List leaves = leaveMapper.findByTeacherId(teacherId);
        return leaves;
    }

    public List findByStudentId(String studentId) {

        List leaves = leaveMapper.findByStudentId(studentId);
        return leaves;
    }

    public Object permit(leave leave) {
        int i = leaveMapper.updateById(leave);
        return i;
    }

    public Object reject(leave leave) {
        int i = leaveMapper.updateById(leave);
        return i;
    }
}
