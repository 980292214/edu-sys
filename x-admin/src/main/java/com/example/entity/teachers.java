package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("t_teachers")
public class teachers extends Model<teachers> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 年龄 
      */
    private String age;

    /**
      * 院系 
      */
    private String departid;

    /**
      * 邮箱 
      */
    private String email;

    /**
      * 职称 
      */
    private String job;

    /**
      * 名字 
      */
    private String name;

    /**
      * 密码 
      */
    private String password;

    /**
      * 联系方式 
      */
    private String phone;

    /**
      * 照片 
      */
    private byte photo;

    /**
      * 性别 
      */
    private String sex;

    /**
      * 教师编号 
      */
    private Long teacherid;

}