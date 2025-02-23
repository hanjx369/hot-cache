package com.qfxl.hotcache;

import com.alibaba.fastjson2.JSON;
import com.qfxl.hotcache.cache.IHotCache;
import com.qfxl.hotcache.model.AreaProvinceVO;
import com.qfxl.hotcache.model.DictVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class HotCacheApplicationTests {

    @Resource(name = "areaCache")
    private IHotCache<String, AreaProvinceVO> areaCache;

    @Resource(name = "dictCache")
    private IHotCache<String, DictVO> dictCache;

    @Test
    void contextLoads() {
        String provinceId = "110000";
        AreaProvinceVO areaProvinceVO = areaCache.get(provinceId);
        log.info("测试结果: {}", JSON.toJSONString(areaProvinceVO));

        String dictType = "sys_user_sex";
        DictVO dictVO = dictCache.get(dictType);
        log.info("测试结果: {}", JSON.toJSONString(dictVO));
    }

}
