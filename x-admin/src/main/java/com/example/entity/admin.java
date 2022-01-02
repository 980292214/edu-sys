package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("t_admin")
public class admin extends Model<admin> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 管理员工号 
      */
    private String adminid;

    /**
      * 年龄 
      */
    private String age;

    /**
      * 电子邮件 
      */
    private String email;

    /**
      * 姓名 
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

}