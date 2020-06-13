package com.mango.core.entity.blog;

import lombok.Data;

import java.util.Date;

@Data
public class Blogs {

    private Long id;
    private String userId;

    private String title;
    private String url;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private String titleType;

    private String content;

}
