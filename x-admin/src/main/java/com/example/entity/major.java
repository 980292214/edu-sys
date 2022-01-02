package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("t_major")
public class major extends Model<major> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 专业编号 
      */
    private String majorid;

    /**
      * 专业名称 
      */
    private String majorname;

    /**
      * 专业介绍 
      */
    private String majordesc;

    /**
      * 院系编号 
      */
    private String departid;

}