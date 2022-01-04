package com.example.mapper;

import com.example.entity.select;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface selectMapper extends BaseMapper<select> {
//    @Select(value="SELECT * FROM t_select AS s " +
//            "INNER JOIN t_course AS c ON s.`courseid` = c.id " +
//            "INNER JOIN t_teachers AS t ON c.`teacherid`=t.`teacherid` " +
//            "WHERE s.`studentid`=#{studentId,jdbcType=VARCHAR}")
    public List findByStudentId(@Param("studentId") String studentId);
}
