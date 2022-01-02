package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("t_course")
public class course extends Model<course> {
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
      * 课程名称 
      */
    private String coursename;

    /**
      * 教师编号 
      */
    private String teacherid;

    /**
      * 学时 
      */
    private String period;

    /**
      * 学分 
      */
    private String credit;

    /**
      * 课程描述 
      */
    private String coursedesc;

    /**
      * 上课地点 
      */
    private String place;

    /**
      * 上课时间 
      */
    private String time;

    /**
      * 修读方式 
      */
    private String mode;

    /**
      * 课程人数 
      */
    private String number;

    /**
      * 限选专业 
      */
    private String limit;

    /**
      * 审核状态 
      */
    private String state;

}