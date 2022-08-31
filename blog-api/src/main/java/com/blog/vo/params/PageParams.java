package com.blog.vo.params;

import lombok.Data;

@Data
public class PageParams {

    //请求文章列表时，前端向后端传输的前端信息，用于分页
    private int page = 1;
    private int pageSize = 10;

    //请求某个分类/标签下面的文章时，用于显示传递参数
    private Long categoryId;
    private Long tagId;

    //查看归档页表
    private String year;
    private String month;
    //月如果是个位，前面要加上0
    public String getMonth() {
        if (this.month != null && this.month.length() == 1) {
            return "0" + this.month;
        }
        return this.month;
    }
}
