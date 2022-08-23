package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    //根据文章id查询标签列表
    List<Tag> findTagsByArticleId(Long atricleId);

    //查询到热门的标签的id
    List<Long> findHotsTagIds(int limit);

    //将热门标签的id转化为标签名字
    List<Tag> findTagsByIds(List<Long> tagIds);
}
