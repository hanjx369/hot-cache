package com.qfxl.hotcache.cache;

import com.qfxl.hotcache.cache.impl.AreaCache;
import com.qfxl.hotcache.cache.impl.DictCache;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 缓存启动
 *
 * @Author 清风徐来
 * @Date 2025-02-23 01:35
 */
@Component
public class CacheRunner implements InitializingBean {

    @Resource
    private AreaCache areaCache;
    @Resource
    private DictCache dictCache;

    @Override
    public void afterPropertiesSet() throws Exception {
        areaCache.init();
        dictCache.init();
    }
}
