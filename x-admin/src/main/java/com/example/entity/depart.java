package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("t_depart")
public class depart extends Model<depart> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 院系编号 
      */
    private String departid;

    /**
      * 院系名称 
      */
    private String departname;

    /**
      * 院系主任 
      */
    private String director;

    /**
      * 院系介绍 
      */
    private String departdesc;

}