package com.qfxl.hotcache.cache;

import jakarta.annotation.Resource;
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
public class CacheRunner implements InitializingBean {

    @Resource
    private List<IHotCache<?, ?>> hotCacheList;

    @Override
    public void afterPropertiesSet() throws Exception {
        hotCacheList.forEach(IHotCache::init);
    }
}
