package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("t_select")
public class select extends Model<select> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 学号 
      */
    private String studentid;

    /**
      * 课程编号 
      */
    private String courseid;

    /**
      * 教师编号 
      */
    private String teacherid;

    /**
      * 课程成绩 
      */
    private String grade;

}