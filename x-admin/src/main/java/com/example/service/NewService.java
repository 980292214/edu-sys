package com.example.service;

import com.example.entity.New;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.NewMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NewService extends ServiceImpl<NewMapper, New> {

    @Resource
    private NewMapper newMapper;

    public List<New> download(){
        return newMapper.download();
    }

    public int downloadnum(Long id){
        return newMapper.downloadnum(id);
    }

}
