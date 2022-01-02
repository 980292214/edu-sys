package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("t_inform")
public class inform extends Model<inform> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 通知编号 
      */
    private String informid;

    /**
      * 通知标题 
      */
    private String title;

    /**
      * 通知内容 
      */
    private String content;

    /**
      * 通知时间 
      */
    private String time;

    /**
      * 发布人工号 
      */
    private String adminid;

    /**
      * 通知附件 
      */
    private String attachment;

}