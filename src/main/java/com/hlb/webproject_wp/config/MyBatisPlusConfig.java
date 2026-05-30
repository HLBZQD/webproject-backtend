package com.hlb.webproject_wp.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.hlb.webproject_wp.mapper")
public class MyBatisPlusConfig {
    // MybatisPlusInterceptor 与 PaginationInnerInterceptor 由自动配置处理
    // 通过 Spring Boot 启动器的 MybatisPlusInnerInterceptorAutoConfiguration.
    // 切勿在此处定义自定义 MybatisPlusInterceptor Bean — 否则会
    // 覆盖自动配置并禁用分页.
}
