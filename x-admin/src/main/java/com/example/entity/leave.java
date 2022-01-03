package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("t_leave")
public class leave extends Model<leave> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 请假编号 
      */
//    private String leaveid;

    /**
      * 请假开始时间 
      */
    private String starttime;

    /**
      * 请假结束时间 
      */
    private String endtime;

    /**
      * 请假原因 
      */
    private String reason;

    /**
      * 请假附件 
      */
    private String attachment;

    /**
      * 学号 
      */
    private String studentid;

    /**
      * 教师编号 
      */
    private String teacherid;

    /**
      * 请假审批状态 
      */
    private String state;

}