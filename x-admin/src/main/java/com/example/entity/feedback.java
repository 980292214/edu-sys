package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("t_feedback")
public class feedback extends Model<feedback> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 课程编号 
      */
    private String courseid;

    /**
      * 学号 
      */
    private String studentid;

    /**
      * 反馈内容 
      */
    private String content;

}