package com.mango.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.mango.core.service.HomePageService;
import com.mango.core.dao.NovelBaseMapper;
import com.mango.core.entity.NovelBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class HomePageServiceImpl implements HomePageService {
    private static Logger logger = LoggerFactory.getLogger(HomePageServiceImpl.class);

    private static final String NOVEL_URL_PRE = "http://www.bxwx8.la/bsort/0/";
    private static final String NOVEL_URL_SUF = ".htm";

    @Autowired
    private NovelBaseMapper novelBaseMapper;


    @Override
    public void getBookRooms() {


        try {
            //1.初始化第一页获取总页数
            String url = NOVEL_URL_PRE + "1" + NOVEL_URL_SUF;
            Document doc = Jsoup.connect(url).get();
            //2.获取总页数
            Elements lastPage = doc.select("a.last");
            Element element = lastPage.get(0);
            String totalPage = element.text();
            //3.封装链接地址
            for (int i = 1; i <= Integer.valueOf(totalPage); i++) {
                url = NOVEL_URL_PRE + i + NOVEL_URL_SUF;
                saveNovelBaseByUrl(url);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void saveNovelBaseByUrl(String url) throws Exception {
        Document content = Jsoup.connect(url).get();
        Elements elements = content.select("tr td.odd,tr td.even");
        for (int i = 0; i < elements.size(); i++) {
            int j;
            if (i % 6 == 0) {
                j = i;
                NovelBase novelBase = new NovelBase();
                novelBase.setName(elements.get(i).text());
                novelBase.setAuthor(elements.get(j + 2).text());
                novelBase.setChapterUrl(elements.get(i).select("td a").attr("href"));
                novelBase.setWordCount(elements.get(j + 3).text());
                novelBase.setStatus(elements.get(j + 5).text().equals("连载") ? 1 : 2);
                novelBase.setEditTime(elements.get(j + 4).text());
                novelBase.setNewChapter(elements.get(j + 1).text());
                novelBase.setCreateTime(new Date());
                novelBase.setUpdateTime(new Date());
                logger.info(JSON.toJSONString(novelBase));
                novelBaseMapper.insert(novelBase);
            }
        }
        Thread.sleep(5000L);
    }

}
