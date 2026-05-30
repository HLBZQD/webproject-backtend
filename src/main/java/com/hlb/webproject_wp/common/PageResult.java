package com.hlb.webproject_wp.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageResult<T> extends Result<T> {
    private long total;
    private long page;
    private long size;
    private long pages;

    public static <T> PageResult<T> success(T data, long total, long page, long size) {
        PageResult<T> r = new PageResult<>();
        r.setCode(200);
        r.setMessage("success");
        r.setData(data);
        r.total = total;
        r.page = page;
        r.size = size;
        r.pages = (total + size - 1) / size;
        return r;
    }
}
