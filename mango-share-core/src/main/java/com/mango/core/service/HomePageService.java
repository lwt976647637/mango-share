package com.mango.core.service;

import java.io.IOException;

public interface HomePageService {
    /**
     * 获取小说图库
     */
    public void getBookRooms();

    /**
     * 根据小说地址保存基本信息
     * @param url
     * @throws IOException
     */
    void saveNovelBaseByUrl(String url) throws Exception;
}
