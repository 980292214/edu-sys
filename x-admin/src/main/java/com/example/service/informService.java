package com.example.service;

import com.example.entity.inform;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.informMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class informService extends ServiceImpl<informMapper, inform> {

    @Resource
    private informMapper informMapper;

}
