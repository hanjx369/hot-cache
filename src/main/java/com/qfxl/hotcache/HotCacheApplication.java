package com.qfxl.hotcache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.qfxl.hotcache.mapper")
public class HotCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotCacheApplication.class, args);
    }

}
