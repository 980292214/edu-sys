package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("t_students")
public class students extends Model<students> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 班级编号 
      */
    private String classe;

    /**
      * 院系 
      */
    private String depart;

    /**
      * 邮箱 
      */
    private String email;

    /**
      * 专业 
      */
    private String major;

    /**
      * 联系方式 
      */
    private String phone;

    /**
      * 姓名 
      */
    private String name;

    /**
      * 密码 
      */
    private String password;

    /**
      * 照片 
      */
    private byte photo;

    /**
      * 年龄 
      */
    private String age;

    /**
      * 入学时间 
      */
    private String starttime;

    /**
      * 学号 
      */
    private String studentid;

    /**
      * 性别 
      */
    private String sex;

}