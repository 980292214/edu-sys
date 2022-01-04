package com.example.mapper;

import com.example.entity.teachers;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface teachersMapper extends BaseMapper<teachers> {
    teachers getTea(@Param("teacherid") Long teacherid);

}
