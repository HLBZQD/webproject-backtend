package com.hlb.webproject_wp.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.hlb.webproject_wp.mapper")
public class MyBatisPlusConfig {
    // MybatisPlusInterceptor with PaginationInnerInterceptor is auto-configured
    // by MybatisPlusInnerInterceptorAutoConfiguration from the Spring Boot starter.
    // Do NOT define a custom MybatisPlusInterceptor bean here — it would
    // override the auto-configured one and disable pagination.
}
