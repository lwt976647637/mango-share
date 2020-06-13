package com.mango.core.entity;

import lombok.Data;

import java.util.Date;

/**
 * @ProjectName: qudian-novel
 * @Package: com.qudian.core.entity
 * @ClassName: NovelBase
 * @Description:
 * @Author: 李维涛
 * @CreateDate: 2020/2/17 22:28
 */
@Data
public class NovelBase {
    private Long id;
    /**
     * 书名
     */
    private String name;

    /**
     * 作者名
     */
    private String author;

    /**
     * 封面的链接
     */
    private String img;

    /**
     * 章节链接
     */
    private String chapterUrl;

    /**
     * 字数
     */
    private String wordCount;

    /**
     * 小说更新时间
     */
    private String editTime;

    /**
     * 小说的状态：1 连载 2 完结
     */
    private Integer status;

    /**
     * 最新章节
     */
    private String newChapter;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}



