package com.example.mapper;

import com.example.entity.leave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface leaveMapper extends BaseMapper<leave> {
    public List findByTeacherId(@Param("teacherId") String teacherId);
}
