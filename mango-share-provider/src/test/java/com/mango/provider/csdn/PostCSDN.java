package com.mango.provider.csdn;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostCSDN {
    private static final String PRE_URL = "https://blog.csdn.net/";
    private static final String SUF_URL = "/article/list/";
    private static final String PRE_HTML = "<!DOCTYPE html><html><head><meta charset='UTF-8'><title>个人博客</title></head><body>";
    private static final String SUF_HTML = "</body></html>";

    public static void main(String[] args) throws MalformedURLException, IOException {
        List<String[]> allData = new ArrayList<>();
        String userName = "qq_33811662"; // 填写个人博客名，具体看浏览器地址
        for (int i = 1;; i++) {
            String url = PRE_URL + userName + SUF_URL + i;
            System.out.println("爬取的url:"+url);
            Scanner scanner = new Scanner(new URL(url).openStream());
            StringBuffer sb = new StringBuffer();
            while (scanner.hasNextLine())
                sb.append(scanner.nextLine());
            List<String[]> list = new ArrayList<>();
            String text = sb.toString();
            String regex = "<div class=\"article-item-box(.*?)<p class=\"content\">";
            String regexInner = "<a href=\"([^\"]+)\".*</span>(.*)</a>";
            Matcher m = Pattern.compile(regex).matcher(text);
            while (m.find()) {
                Matcher m2 = Pattern.compile(regexInner).matcher(m.group(1));
                if (m2.find())
                    list.add(new String[] { m2.group(1), m2.group(2).trim() });
            }
            if (0 == list.size())
                break;
            allData.addAll(list);
            scanner.close();
        }
        System.out.println(allData.size());
        allData.forEach(e -> System.out.println(e[0] + "," + e[1]));
        StringBuilder outContent = new StringBuilder(PRE_HTML);
        String f = new File("").getAbsolutePath();
        String filePath = f + "\\bin\\my_blog.html";// 此处路径注意，我是没在任何 package 中创建
        PrintWriter writer = new PrintWriter(filePath);
        writer.write(outContent.toString());
        writer.flush();
        writer.close();

    }
}
