package com.mango.core.dao;

import com.mango.core.entity.blog.BlogsCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ProjectName: qudian-novel
 * @Package: com.qudian.core.dao
 * @ClassName: NovelMapper
 * @Description:
 * @Author: 李维涛
 * @CreateDate: 2020/2/17 22:27
 */
@Mapper
public interface BlogsCategoryMapper {

    /**
     * 根据文章标题和作者查询
     * @return
     */
    List<BlogsCategory> findBlogsCategoryList();
}
