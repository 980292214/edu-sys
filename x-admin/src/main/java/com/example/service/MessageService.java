package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Message;
import com.example.mapper.MessageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MessageService extends ServiceImpl<MessageMapper, Message> {

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private UserService userService;

//    @Resource
//    private studentService studentService;
//
//    public List<Message> findByForeign() {
//        //将这句话当成查询就好了
//        LambdaQueryWrapper<Message> queryWrapper = Wrappers.<Message>lambdaQuery().eq(Message::getForeignId, 0).orderByDesc(Message::getId);
//        List<Message> list = list(queryWrapper);
//        for (Message Message : list) {
//            student one = studentService.getOne(Wrappers.<student>lambdaQuery().eq(student::getName, Message.getUsername()));
//            Long parentId = Message.getParentId();
//            list.stream().filter(c -> c.getId().equals(parentId)).findFirst().ifPresent(Message::setParentMessage);
//        }
//        return list;
//    }

}
