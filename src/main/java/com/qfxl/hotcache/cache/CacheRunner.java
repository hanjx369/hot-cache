package com.qfxl.hotcache.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 缓存启动
 *
 * @Author 清风徐来
 * @Date 2025-02-23 01:35
 */
@Component
@RequiredArgsConstructor
public class CacheRunner implements InitializingBean {

    private final List<IHotCache<?, ?>> hotCacheList;

    @Override
    public void afterPropertiesSet() throws Exception {
        hotCacheList.forEach(IHotCache::init);
    }
}
