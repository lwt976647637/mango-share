package com.mango.core.dao;

import com.mango.core.entity.blog.Blogs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface BlogsMapper {
    void insert(Blogs blogs);

    /**
     * 根据文章标题和作者查询
     * @return
     */
    Blogs findByUserIdAndTitle(@Param("userId")String userId , @Param("title") String title);

    /**
     * 根据分类模糊查询博客列表
     * @param categoryType
     * @return
     */
    List<Blogs> findBlogsByName(String categoryType);

    /**
     * 更新博客状态
     * @param blogs
     */
    void updateStatus(Blogs blogs);

    void clearContent();
}
