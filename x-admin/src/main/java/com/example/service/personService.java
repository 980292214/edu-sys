package com.example.service;

import com.example.entity.person;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.personMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class personService extends ServiceImpl<personMapper, person> {

    @Resource
    private personMapper personMapper;

}
