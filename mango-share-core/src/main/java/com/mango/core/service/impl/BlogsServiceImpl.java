package com.mango.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mango.core.dao.BlogsCategoryMapper;
import com.mango.core.dao.BlogsMapper;
import com.mango.core.entity.blog.Blogs;
import com.mango.core.entity.blog.BlogsCategory;
import com.mango.core.service.BlogsService;
import com.mango.core.entity.blog.BlogDetailResponse;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BlogsServiceImpl implements BlogsService {

    private static Logger logger = LoggerFactory.getLogger(BlogsServiceImpl.class);

    @Autowired
    private BlogsMapper blogsMapper;

    @Autowired
    private BlogsCategoryMapper blogsCategoryMapper;


    @Override
    public void getJavaBlogsByUser() throws IOException {

        String[] userAgent = {
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:6.0) Gecko/20100101 Firefox/6.0",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:2.0.1) Gecko/20100101 Firefox/4.0.1",
                "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
                "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
                "Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; en) Presto/2.8.131 Version/11.11",
                "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
                "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SE 2.X MetaSr 1.0; SE 2.X MetaSr 1.0; .NET CLR 2.0.50727; SE 2.X MetaSr 1.0)",
                "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)"
        };

        String randomAgent = userAgent[threadSleep("1-11")];
        logger.info("[当前得到用户代理为]" + randomAgent);
        this.init(randomAgent);
        //1、获取Java模块博客文章
        String javaUrl = "https://www.csdn.net/nav/java";
//        int threadSleep = threadSleep("1-6");
//        logger.info("博客列表休眠:"+threadSleep);
        Document javaDoc = Jsoup.connect(javaUrl)
                .header("Accept", "*/*")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("Host", "222.223.235.164:8090")
                .header("User-Agent", randomAgent)
                .timeout(100000).get();
        //获取用户id
        Elements select = javaDoc.select("div.list_con div.title h2 a");
        String userId;
        Blogs blogs;
        for (Element element : select) {
            String href = element.attr("href");
            String s = href.replaceAll("https://blog.csdn.net/", "");
            userId = s.substring(0, s.indexOf("/"));
            //获取用户的列表
            long startTime = System.currentTimeMillis();
            for (int i = 1; ; i++) {
                threadSleep("1-3");
                String userBlogListUrl = "https://blog.csdn.net/" + userId + "/article/list/" + i;
                List<String> articleIds = new ArrayList<>();
                int userSleep = threadSleep("1-6");
//                logger.info("用户博客列表休眠:"+userSleep);
                Document userBlogDoc = Jsoup.connect(userBlogListUrl)
                        .header("Accept", "*/*")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .header("Content-Type", "application/json;charset=UTF-8")
                        .header("Host", "222.223.235.164:8090")
                        .header("User-Agent", randomAgent)
//                .proxy("58.243.50.184",53281)
                        .timeout(100000).get();

                Elements userBlogListElements = userBlogDoc.select("div");
                for (Element listElement : userBlogListElements) {
                    String articleId = listElement.attr("data-articleid");
                    if (!StringUtils.isEmpty(articleId)) {
                        try {
                            articleIds.add(articleId);
                            String articleIdUrl = "https://blog.csdn.net/" + userId + "/article/details/" + articleId;
                            Document doc = Jsoup.connect(articleIdUrl)
                                    .header("Accept", "*/*")
                                    .header("Accept-Encoding", "gzip, deflate, br")
                                    .header("Accept-Language", "zh-CN,zh;q=0.9")
                                    .header("Content-Type", "application/json;charset=UTF-8")
                                    .header("Host", "222.223.235.164:8090")
                                    .header("User-Agent", randomAgent)
                                    .timeout(100000).get();

                            Elements titile = doc.select("h1.title-article");
                            doc.select("img").removeAttr("onerror");
                            String titileName = titile.text().replaceAll(" ", "").replaceAll("——", "")
                                    .replaceAll("（", "").replaceAll("]", "")
                                    .replaceAll("】", "").replaceAll("【", "").replaceAll("/", "-");
                            //校验当前博客是否保存

                            blogs = blogsMapper.findByUserIdAndTitle(userId, titileName);
                            if (null == blogs) {
                                logger.info(titileName);
                                blogs = new Blogs();
                                blogs.setTitle(titileName);
                                blogs.setUserId(userId);
                                blogs.setCreateTime(new Date());
                                blogs.setUpdateTime(new Date());
                                blogs.setUrl(articleIdUrl);
                                doc.select("div.user-info.d-flex.flex-column.profile-intro-name-box").remove();
                                doc.select("div.profile-intro-name-boxOpration").remove();
                                doc.select("div.recommend-box").remove();
                                doc.select("div#asideNewArticle").remove();
                                doc.select("div.article-info-box").remove();
                                doc.select("div.person-messagebox").remove();
                                doc.select("div.more-toolbox").remove();
                                doc.select("div.article-header-box").remove();
                                doc.select("div.data-info.d-flex.item-tiling").remove();
                                doc.select("div.grade-box.clearfix").remove();
                                doc.select("div.slide-content-box").remove();
                                doc.select("script#toolbar-tpl-scriptId").remove();
                                doc.select("div#asideCategory").remove();
                                doc.select("div.csdn-tracking-statistics.mb8.box-shadow").remove();
                                doc.select("div.aside-box.custom-box").remove();
                                doc.select("div.column-advert-box").remove();
                                doc.select("div#asideHotArticle").remove();
                                doc.select("div#asideArchive").remove();
                                doc.select("div#asideNewComments").remove();
                                doc.select("div.recommend-right.align-items-stretch.clearfix").remove();
                                doc.select("div#asideFooter").remove();
                                doc.select("div#asideProfile").remove();
                                doc.select("div.tool-box.vertical").remove();
                                doc.select("div.comment-box").remove();
                                doc.select("div.template-box").remove();
                                doc.select("body[style=background-color:transparent]").remove();
                                doc.select("a.btn-readmore.no-login").remove();
                                blogs.setStatus(0);
                                blogs.setContent(doc.toString());
                                blogsMapper.insert(blogs);
////                                //获取分类列表
////                                List<BlogsCategory> blogsCategoryList = blogsCategoryMapper.findBlogsCategoryList();
////                                for (BlogsCategory blogsCategory : blogsCategoryList) {
////                                    //匹配文章列表
////                                    String filePath = "/Users/macbookpro/csdn/" + blogsCategory.getCategoryType() + "/";
////                                    File dir = new File(filePath);
////                                    if (!dir.isDirectory()) {
////                                        dir.mkdirs();
////                                    }
////                                    try {
////                                        if(titileName.contains(blogsCategory.getCategoryType())){
////                                            logger.info(titileName+",包含:"+blogsCategory.getCategoryType());
////                                            PrintWriter writer = new PrintWriter(titileName);
////                                            writer.write(doc.toString());
////                                            writer.flush();
////                                            writer.close();
////                                            //更新博客状态
////                                            blogs.setStatus(1);
////                                            blogsMapper.updateStatus(blogs);
////                                        }
////
////                                    } catch (Exception e) {
////                                        e.printStackTrace();
////                                    }
//
//
//                                }

                            }


                        } catch (Exception e) {
                        }


                    }

                }
                if (0 == articleIds.size()) {
                    logger.info("同步当前用户全部博客耗时:" + (System.currentTimeMillis() - startTime));
                    break;
                }
                articleIds.clear();
            }

        }
    }

    /**
     * 增加个人博客点击量
     */
    private void init(String randomAgent) {
        try {
            List<String> articleIds = new ArrayList<>();
            String userId = "weixin_37645032";
            //获取用户的列表
            for (int i = 1; ; i++) {
                String userBlogListUrl = "https://blog.csdn.net/" + userId + "/article/list/" + i;
                int threadSleep = threadSleep("1-3");
                logger.info("个人博客休眠:" + threadSleep);
                Document userBlogDoc = Jsoup.connect(userBlogListUrl)
                        .header("Accept", "*/*")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .header("Content-Type", "application/json;charset=UTF-8")
                        .header("Host", "222.223.235.164:8090")

                        .header("User-Agent", randomAgent)
//                .proxy("58.243.50.184",53281)
                        .timeout(100000).get();
                Elements userBlogListElements = userBlogDoc.select("div");
                for (Element listElement : userBlogListElements) {
                    String articleId = listElement.attr("data-articleid");
                    if (!StringUtils.isEmpty(articleId)) {
                        try {
                            articleIds.add(articleId);
                            String articleIdUrl = "https://blog.csdn.net/" + userId + "/article/details/" + articleId;
//                            int sleep = threadSleep("1-3");
//                            logger.info("个人博客休眠:" + sleep);
                            Document doc = Jsoup.connect(articleIdUrl)
                                    .header("Accept", "*/*")
                                    .header("Accept-Encoding", "gzip, deflate, br")
                                    .header("Accept-Language", "zh-CN,zh;q=0.9")
                                    .header("Content-Type", "application/json;charset=UTF-8")
                                    .header("User-Agent", randomAgent)
//                .proxy("58.243.50.184",53281)
                                    .timeout(100000).get();
                            doc.select("img").removeAttr("onerror");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
                if (0 == articleIds.size()) {
                    break;
                }
                articleIds.clear();
            }
            logger.info("个人博客初始化完成!");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getJavaNewBlogs() throws IOException {
        //1、获取Java模块博客文章
        String javaUrl = "https://www.csdn.net/nav/java/";
        Document javaDoc = Jsoup.connect(javaUrl)
                .header("Accept", "*/*")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
                .timeout(10000).get();
        javaDoc.select("img").removeAttr("onerror");
        System.out.println(javaDoc);
        //获取用户id
        Elements blogs = javaDoc.select("div.list_con div.title h2 a");
        logger.info("获取最新博客列表数量:" + blogs.size());
        for (Element element : blogs) {
            try {
                String href = element.attr("href");
                Document doc = Jsoup.connect(href).get();
                doc.select("img").removeAttr("onerror");
                Elements titile = doc.select("h1.title-article");
                String filePath = "/Users/macbookpro/Downloads/csdn/java/" + titile.text() + ".html";
                PrintWriter writer = new PrintWriter(filePath);
                writer.write(doc.toString());
                writer.flush();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void getBlogsByURL(String url) throws IOException {
        Document doc = Jsoup.connect(url)
                .header("Accept", "*/*")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko")
//                .proxy("58.243.50.184",53281)
                .timeout(100000).get();
        doc.select("img").removeAttr("onerror");

        Elements titile = doc.select("h1.title-article");
        doc.select("img").removeAttr("onerror");
        String titileName = titile.text().replaceAll(" ", "").replaceAll("——", "")
                .replaceAll("（", "").replaceAll("]", "")
                .replaceAll("】", "").replaceAll("【", "").replaceAll("/", "-");


        doc.select("div.user-info.d-flex.flex-column.profile-intro-name-box").remove();
        doc.select("div.profile-intro-name-boxOpration").remove();
        doc.select("div.recommend-box").remove();
        doc.select("div#asideNewArticle").remove();
        doc.select("div.article-info-box").remove();
        doc.select("div.person-messagebox").remove();
        doc.select("div.more-toolbox").remove();
        doc.select("div.article-header-box").remove();
        doc.select("div.data-info.d-flex.item-tiling").remove();
        doc.select("div.grade-box.clearfix").remove();
        doc.select("div.slide-content-box").remove();
        doc.select("script#toolbar-tpl-scriptId").remove();
        doc.select("div#asideCategory").remove();
        doc.select("div.csdn-tracking-statistics.mb8.box-shadow").remove();
        doc.select("div#asideHotArticle").remove();
        doc.select("div#asideArchive").remove();
        doc.select("div.recommend-right.align-items-stretch.clearfix").remove();
        doc.select("div#asideFooter").remove();
        doc.select("div#asideProfile").remove();
        doc.select("div.tool-box.vertical").remove();
        doc.select("div.comment-box").remove();
        doc.select("div.template-box").remove();
        doc.select("body[style=background-color:transparent]").remove();

        System.out.println(doc);

    }

    @Override
    public void getBlogsByCategory() {
        String[] userAgent = {
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:6.0) Gecko/20100101 Firefox/6.0",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:2.0.1) Gecko/20100101 Firefox/4.0.1",
                "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
                "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
                "Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; en) Presto/2.8.131 Version/11.11",
                "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
                "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SE 2.X MetaSr 1.0; SE 2.X MetaSr 1.0; .NET CLR 2.0.50727; SE 2.X MetaSr 1.0)",
                "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)"
        };

        String randomAgent = userAgent[threadSleep("1-11")];
        this.init(randomAgent);
        //获取分类列表
        List<BlogsCategory> blogsCategoryList = blogsCategoryMapper.findBlogsCategoryList();
        for (BlogsCategory blogsCategory : blogsCategoryList) {
            //匹配文章列表
            List<Blogs> blogs = blogsMapper.findBlogsByName(blogsCategory.getCategoryType());
            String filePath = "/home/csdn/" + blogsCategory.getCategoryType() + "/";
            File dir = new File(filePath);
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
            for (Blogs blog : blogs) {
                try {
                    String fileName = filePath + blog.getUserId() + ":" + blog.getTitle() + ".html";
                    PrintWriter writer = new PrintWriter(fileName);
                    writer.write(blog.getContent());
                    writer.flush();
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //更新博客状态
                blog.setStatus(1);
                blogsMapper.updateStatus(blog);

            }


        }


    }

    @Override
    public void clearBlogContent() {
        blogsMapper.clearContent();
    }

    @Override
    public void getBlogsByCategory(String url) throws IOException {
        String[] userAgent = {
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:6.0) Gecko/20100101 Firefox/6.0",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:2.0.1) Gecko/20100101 Firefox/4.0.1",
                "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
                "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
                "Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; en) Presto/2.8.131 Version/11.11",
                "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
                "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SE 2.X MetaSr 1.0; SE 2.X MetaSr 1.0; .NET CLR 2.0.50727; SE 2.X MetaSr 1.0)",
                "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)"
        };
        String randomAgent = userAgent[threadSleep("1-11")];
        //获取分类列表
        List<BlogsCategory> blogsCategoryList = blogsCategoryMapper.findBlogsCategoryList();
        for (BlogsCategory blogsCategory : blogsCategoryList) {
            logger.info("~~~~~~~~~~~~~~当前博客分类：" + blogsCategory.getCategoryType() + "~~~~~~~~~~~~~~开始同步~~~~~~~~~~~~~~~~~~~~~");
            this.init(randomAgent);
            try {
                for (int i = 1; i <= 10; i++) {
                    try {
                        String blogSearchUrl = url + blogsCategory.getCategoryType() + "&p=" + i + "&viparticle=&domain=&o=&s=&u=&l=&rbg=0";
                        Document doc = Jsoup.connect(blogSearchUrl)
                                .header("Accept", "*/*")
                                .header("Accept-Encoding", "gzip, deflate, br")
                                .header("Accept-Language", "zh-CN,zh;q=0.9")
                                .header("Content-Type", "application/json;charset=UTF-8")
                                .header("User-Agent", randomAgent)
                                .timeout(100000).get();
                        doc.select("div.limit_width").remove();
                        doc.select("dd.author-time").remove();
                        doc.select("dd.search-detail").remove();
                        Elements elements = doc.select("dl.search-list.J_search");
                        for (Element element : elements) {
                            try {
                                String el = element.attr("data-report-view");
                                BlogDetailResponse blogDetailResponse = JSON.parseObject(el, new TypeReference<BlogDetailResponse>() {
                                });
                                String s = blogDetailResponse.getDest().replaceAll("https://blog.csdn.net/", "");
                                String userId = s.substring(0, s.indexOf("/"));
                                Document detailDoc = Jsoup.connect(blogDetailResponse.getDest())
                                        .header("Accept", "*/*")
                                        .header("Accept-Encoding", "gzip, deflate, br")
                                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                                        .header("Content-Type", "application/json;charset=UTF-8")
                                        .header("Host", "222.223.235.164:8090")
                                        .header("User-Agent", randomAgent)
                                        .timeout(100000).get();

                                Elements titile = detailDoc.select("h1.title-article");
                                detailDoc.select("img").removeAttr("onerror");
                                String titileName = titile.text().replaceAll(" ", "").replaceAll("——", "")
                                        .replaceAll("（", "").replaceAll("]", "")
                                        .replaceAll("】", "").replaceAll("【", "").replaceAll("/", "-");
                                detailDoc.select("div.user-info.d-flex.flex-column.profile-intro-name-box").remove();
                                detailDoc.select("div.profile-intro-name-boxOpration").remove();
                                detailDoc.select("div.recommend-box").remove();
                                detailDoc.select("div#asideNewArticle").remove();
                                detailDoc.select("div.article-info-box").remove();
                                detailDoc.select("div.person-messagebox").remove();
                                detailDoc.select("div.more-toolbox").remove();
                                detailDoc.select("div.article-header-box").remove();
                                detailDoc.select("div.data-info.d-flex.item-tiling").remove();
                                detailDoc.select("div.grade-box.clearfix").remove();
                                detailDoc.select("div.slide-content-box").remove();
                                detailDoc.select("script#toolbar-tpl-scriptId").remove();
                                detailDoc.select("div#asideCategory").remove();
                                detailDoc.select("div.csdn-tracking-statistics.mb8.box-shadow").remove();
                                detailDoc.select("div.aside-box.custom-box").remove();
                                detailDoc.select("div.column-advert-box").remove();
                                detailDoc.select("div#asideHotArticle").remove();
                                detailDoc.select("div#asideArchive").remove();
                                detailDoc.select("div#asideNewComments").remove();
                                detailDoc.select("div.recommend-right.align-items-stretch.clearfix").remove();
                                detailDoc.select("div#asideFooter").remove();
                                detailDoc.select("div#asideProfile").remove();
                                detailDoc.select("div.tool-box.vertical").remove();
                                detailDoc.select("div.comment-box").remove();
                                detailDoc.select("div.template-box").remove();
                                detailDoc.select("body[style=background-color:transparent]").remove();
                                detailDoc.select("a.btn-readmore.no-login").remove();
                                String filePath = "/home/csdn/" + blogsCategory.getCategoryType() + "/";
                                File dir = new File(filePath);
                                if (!dir.isDirectory()) {
                                    dir.mkdirs();
                                }
                                //校验当前博客是否保存
                                Blogs blogs = blogsMapper.findByUserIdAndTitle(userId, titileName);
                                if (null == blogs) {
                                    logger.info(titileName);
                                    blogs = new Blogs();
                                    blogs.setTitle(titileName);
                                    blogs.setUserId(userId);
                                    blogs.setCreateTime(new Date());
                                    blogs.setUpdateTime(new Date());
                                    blogs.setUrl(blogDetailResponse.getDest());
                                    blogs.setStatus(0);
                                    blogs.setContent("");
                                    blogsMapper.insert(blogs);
                                    try {
                                        String fileName = filePath + userId + titileName + ".html";
                                        PrintWriter writer = new PrintWriter(fileName);
                                        writer.write(detailDoc.toString());
                                        writer.flush();
                                        writer.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (Exception e) {

                            }

                        }
                    } catch (Exception e) {

                    }

                }

            } catch (Exception e) {

            }

        }


    }

    @Override
    public void getHTML(String url) {
        String[] userAgent = {
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:6.0) Gecko/20100101 Firefox/6.0",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:2.0.1) Gecko/20100101 Firefox/4.0.1",
                "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
                "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
                "Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; en) Presto/2.8.131 Version/11.11",
                "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
                "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SE 2.X MetaSr 1.0; SE 2.X MetaSr 1.0; .NET CLR 2.0.50727; SE 2.X MetaSr 1.0)",
                "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)"
        };
        String randomAgent = userAgent[threadSleep("1-11")];
        try {
            Document doc = Jsoup.connect(url)
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept-Language", "zh-CN,zh;q=0.9")
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .header("User-Agent", randomAgent)
                    .timeout(100000).get();
            System.out.println(doc.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 睡眠n-m秒
     *
     * @param time 1-6
     */

    public int threadSleep(String time) {
        //1-6
        String[] split = time.split("-");

        int first = Integer.parseInt(split[0]);

        int second = Integer.parseInt(split[1]);

        try {

            int i = first + (int) (Math.random() * (second - first));
            Thread.sleep(i * 1000);
            return i;
        } catch (InterruptedException e) {

            e.printStackTrace();

        }
        return 0;
    }
}
