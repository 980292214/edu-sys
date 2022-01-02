package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("t_category")
public class category extends Model<category> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 资源编号 
      */
    private Integer categoryId;

    /**
      * 资源分类描述 
      */
    private String descn;

    /**
      * 资源分类名称 
      */
    private String name;

}