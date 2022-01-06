package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("t_homework")
public class homework extends Model<homework> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 作业编号 
      */


    /**
      * 课程编号 
      */
    private String courseid;

    /**
      * 学号 
      */
    private String studentid;

    /**
      * 作业内容 
      */
    private String content;

    /**
      * 作业附件 
      */
    private String attachment;

    /**
      * 截至日期 
      */
    private String deadline;

    /**
      * 提交状态 
      */
    private String state;

    private String demand;

    /**
     * 提交状态
     */
    private String files;

}