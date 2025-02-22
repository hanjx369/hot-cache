package com.qfxl.hotcache.cache;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 缓存抽象类
 *
 * @Author 清风徐来
 * @Date 2025-02-23 01:47
 */
@Slf4j
@Component
@RequiredArgsConstructor
public abstract class AbstractCache<T, R> implements IHotCache<T, R> {

    // redis模板
    @Resource
    protected RedisTemplate<String, Object> redisTemplate;

    // 线程池
    protected final static ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);

    @Override
    public void reload() {
        clear();
        init();
    }

    @PreDestroy
    private void shutdownThreadPool() {
        try {
            EXECUTOR_SERVICE.shutdown();
            if (EXECUTOR_SERVICE.awaitTermination(15, TimeUnit.MINUTES)) {
                log.info("{} 线程池自动销毁", this.getClass().getName());
                EXECUTOR_SERVICE.shutdownNow();
            }
        } catch (Exception e) {
            log.error("{} 线程池自动销毁失败", this.getClass().getName());
            EXECUTOR_SERVICE.shutdownNow();
        }
    }

}
