package com.mango.task;

import com.mango.core.service.BlogsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MangoTaskApplication.class)
public class MangoTaskApplicationTests {

	@Autowired
	private BlogsService blogsService;

	@Test
	public void contextLoads() {
		try {
			blogsService.getBlogsByURL("https://blog.csdn.net/weixin_37645032/article/details/103742683");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testBlogsByCategory() {
		try {
			blogsService.getBlogsByCategory("https://so.csdn.net/so/search/s.do?t=blog&o=&q=");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	@Test
	public void testTemplate() {
		try {
			blogsService.getHTML("https://ibaotu.com/ppt/3-0-0-0-3-1.html?chan=bd&label=ppt&plan=A3-bd&kwd=7675&unit=13085&bd_vid=11658243849429247380");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
