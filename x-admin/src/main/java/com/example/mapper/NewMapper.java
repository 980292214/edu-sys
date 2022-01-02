package com.example.mapper;

import com.example.entity.New;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewMapper extends BaseMapper<New> {

    public List<New> download();


    public int downloadnum(Long id);
}
