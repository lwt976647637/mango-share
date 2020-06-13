package com.mango.core.service;

import java.io.IOException;

/**
 * csdn博客
 */
public interface BlogsService {
    /**
     * 通过Java模块获取用户的全部博客-比较杂
     * @throws IOException
     */
    void getJavaBlogsByUser() throws IOException;

    /**
     * 获取java模块最新的博客文章
     */
    void getJavaNewBlogs() throws IOException;

    /**
     * 根据url获取指定博客
     * @param url
     * @throws IOException
     */
    void getBlogsByURL(String url)throws IOException;

    /**
     * 按博客分类进行同步
     */
    void getBlogsByCategory();

    /**
     * 清理博客数据
     */
    void clearBlogContent();

    /**
     * 根据博客分类获取博客列表
     * @param url
     */
    void getBlogsByCategory(String url) throws IOException;

    void getHTML(String s);
}
