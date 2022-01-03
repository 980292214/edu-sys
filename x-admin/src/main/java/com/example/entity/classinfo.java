package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("t_class")
public class classinfo extends Model<classinfo> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 班级编号 
      */
    private String classid;

    /**
      * 辅导员 
      */
    private String instructor;

    /**
      * 班级人数 
      */
    private String classsize;

    /**
      * 专业编号 
      */
    private String majorid;

}