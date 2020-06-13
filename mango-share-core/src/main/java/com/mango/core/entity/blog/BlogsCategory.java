package com.mango.core.entity.blog;

import lombok.Data;

import java.util.Date;

@Data
public class BlogsCategory {

    private Long id;
    private String categoryType;
    private Integer sort;

    private Date createTime;

    private Date updateTime;

}
