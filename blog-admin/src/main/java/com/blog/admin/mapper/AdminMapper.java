package com.blog.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.admin.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
}