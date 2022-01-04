package com.example.mapper;

import com.example.entity.students;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface studentsMapper extends BaseMapper<students> {
    students getStudentid(@Param("username") String username);

    students getStu(@Param("studentid") Long studentid);

}
