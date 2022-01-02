package com.example.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@TableName("t_resource")
public class resource extends Model<resource> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 资源分类编号 
      */
    private Integer categoryId;

    /**
      * 资源描述 
      */
    private String descn;

    /**
      * 运行效果图 
      */
    private String image;

    /**
      * 资源名称 
      */
    private String name;

    /**
      * 上传者 
      */
    private String uploader;

}