package com.mango.provider;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mango.core.service.BlogsService;
import com.mango.core.service.HomePageService;
import com.mango.core.spider.NovelSpiderHttpGet;
import com.mango.remote.pojo.ArticleTitle;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MangoProviderApplication.class)
public class MangoProviderApplicationTests {

    @Autowired
    private HomePageService homePageService;

    @Autowired
    private BlogsService blogsService;

    @Test
    /**
     * 测试Jsoup方法
     */
    public void testJsoupProcess() throws IOException {
        String url = "http://www.bxwx8.la/bsort/0/1.htm";
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();
             CloseableHttpResponse httpResponse = httpClient.execute(new NovelSpiderHttpGet(url))) {
            String result = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Document doc = Jsoup.connect(url).get();
        System.out.println("=========================================");


        //查询table下所有tr元素
        Elements select = doc.select("tr td.odd,tr td.even");
        Elements lastPage = doc.select("a.last");
        System.out.println("lastpage" + lastPage.toString());

//		System.out.println("查询table下所有tr元素:"+select.toString());


        int size = select.size();
        System.out.println("总条数:" + size);
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < select.size(); i++) {
            int j = 0;
            if (i % 6 == 0) {
                j = i;
                System.out.println("标题" + i + "内容:" + select.get(i).text());
                Elements eleUrl = select.get(i).select("td a");
                String strPrjUrl = eleUrl.attr("href");
                System.out.println("href:" + strPrjUrl);
                System.out.println("最新章节" + j + 1 + "内容:" + select.get(j + 1).text());
                System.out.println("作者" + j + 2 + "内容:" + select.get(j + 2).text());
                System.out.println("字数" + j + 3 + "内容:" + select.get(j + 3).text());
                System.out.println("更新" + j + 4 + "内容:" + select.get(j + 4).text());
                System.out.println("状态" + j + 5 + "内容:" + select.get(j + 5).text());
            }
        }

    }

    @Test
    public void test1() {
        homePageService.getBookRooms();
    }


    @Test
    public void testCSDN() {
        String url = "https://blog.csdn.net/ThinkWon/article/details/104390612";
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();
             CloseableHttpResponse httpResponse = httpClient.execute(new NovelSpiderHttpGet(url))) {
//                String result = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
            //<div style="display:none;">
            //    <img src="" onerror='setTimeout(function(){if(!/(csdn.net|iteye.com|baiducontent.com|googleusercontent.com|360webcache.com|sogoucdn.com|bingj.com|baidu.com)$/.test(window.location.hostname)){window.location.href="\x68\x74\x74\x70\x73\x3a\x2f\x2f\x77\x77\x77\x2e\x63\x73\x64\x6e\x2e\x6e\x65\x74"}},3000);'>
            //</div>
//            Document doc = Jsoup.connect(url).get();
//            //获取标签中的onerror属性
//            //            Elements div_imh = doc.select("div img[onerror]");
//            // // 删除所有图片的 onerror 属性
//            doc.select("img").removeAttr("onerror");
//            Elements titile = doc.select("head meta[name=csdn-baidu-search]");
//            ArticleTitle articleTitle  = JSON.parseObject(titile.attr("content"),new TypeReference<ArticleTitle>(){});
//            String f = new File("").getAbsolutePath();
//            String filePath = f + "/target/tmp/"+articleTitle.getKeyword()+".html";// 此处路径注意，我是没在任何 package 中创建
//            System.out.println(filePath);
//            PrintWriter writer = new PrintWriter(filePath);
//            writer.write(doc.toString());
//            writer.flush();
//            writer.close();

//            System.out.println(titile.attr("content"));//{"autorun":true,"install":true,"keyword":"Java基础知识面试题（2020最新版）_数据库_ThinkWon的博客-CSDN博客"}
//            System.out.println(doc.toString());

            //csdn java板块
            String javaUrl = "http://blog.csdn.net/nav/java";
            Document javaDoc = Jsoup.connect(javaUrl).get();
            //获取用户id
            Elements select = javaDoc.select("div.list_con div.title h2 a");
            String userId = null;
            for (Element element : select) {
                String href = element.attr("href");
                String s = href.replaceAll("https://blog.csdn.net/", "");
                userId = s.substring(0, s.indexOf("/"));
                //获取用户的列表
                String userBlogListUrl = "https://blog.csdn.net/" + userId;
                Document userBlogDoc = Jsoup.connect(userBlogListUrl).get();
                Elements userBlogListElements = userBlogDoc.select("div");
                for (Element listElement : userBlogListElements) {
                    String articleId = listElement.attr("data-articleid");
                    if (!StringUtils.isEmpty(articleId)) {
                        try {
                            String articleIdUrl = "https://blog.csdn.net/" + userId + "/article/details/" + articleId;
                            Document doc = Jsoup.connect(articleIdUrl).get();
                            //获取标签中的onerror属性
                            //            Elements div_imh = doc.select("div img[onerror]");
                            // // 删除所有图片的 onerror 属性
                            doc.select("img").removeAttr("onerror");
                            Elements titile = doc.select("head meta[name=csdn-baidu-search]");
                            doc.select("img").removeAttr("onerror");
                            ArticleTitle articleTitle = JSON.parseObject(titile.attr("content"), new TypeReference<ArticleTitle>() {
                            });
//                        String f = new File("").getAbsolutePath();
                            String titileName = articleTitle.getKeyword().replaceAll("//", "").replaceAll(" ", "");
                            String filePath = "/Users/macbookpro/Downloads/csdn/java/" + "用户:" + userId + "文章名称:" + titileName + ".html";// 此处路径注意，我是没在任何 package 中创建
                            System.out.println(filePath);
                            PrintWriter writer = new PrintWriter(filePath);
                            writer.write(doc.toString());
                            writer.flush();
                            writer.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testGetJavaNewBlogs() {
        try {
            blogsService.getJavaNewBlogs();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetJavaBlogsByUser() {
        try {
            blogsService.getJavaBlogsByUser();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void xiguaVide() throws IOException {

//        String response = APIMthod.sendGetByHttpCient("https://www.ixigua.com/");
//        System.out.println(response);
        Map<String, String> cookies = Jsoup.connect("https://80s.tw/").execute().cookies();
        System.out.println(cookies);
        Document doc = Jsoup.connect("https://80s.tw/")
                .header("Accept", "*/*")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Content-Type", "text/html; charset=utf-8")
                .header("Host", "222.223.235.164:8090")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36")
                .cookie("cookie","BAIDU_SSP_lcr=https://www.baidu.com/link?url=iq1qO_qlSsQ9KZqXcwKXgiAkWlMah-JxtEhHiEufxRC&wd=&eqid=838e57980009d438000000025e59b9c9; Hm_lvt_caa88905362b7957005130b28e579d36=1582938574; Hm_lpvt_caa88905362b7957005130b28e579d36=1582938574")
                .timeout(100000).get();

        System.out.println(doc.toString());


    }
}
