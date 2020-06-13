package com.mango.core.dao;

import com.mango.core.entity.NovelBase;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ProjectName: qudian-novel
 * @Package: com.qudian.core.dao
 * @ClassName: NovelMapper
 * @Description:
 * @Author: 李维涛
 * @CreateDate: 2020/2/17 22:27
 */
@Mapper
public interface NovelBaseMapper {
    void insert(NovelBase novelBase);
}
