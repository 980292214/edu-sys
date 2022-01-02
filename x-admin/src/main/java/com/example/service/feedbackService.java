package com.example.service;

import com.example.entity.feedback;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.feedbackMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class feedbackService extends ServiceImpl<feedbackMapper, feedback> {

    @Resource
    private feedbackMapper feedbackMapper;

}
