package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("t_exam")
public class exam extends Model<exam> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 考试编号 
      */
    private String examid;

    /**
      * 考试时间 
      */
    private String time;

    /**
      * 考试地点 
      */
    private String place;

    /**
      * 课程编号 
      */
    private String courseid;

    /**
      * 班级编号 
      */
    private String classid;

    /**
      * 教师编号 
      */
    private String teacherid;

    /**
      * 审核状态 
      */
    private String state;

}