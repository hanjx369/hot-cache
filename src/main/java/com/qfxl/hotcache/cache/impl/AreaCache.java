package com.qfxl.hotcache.cache.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.qfxl.hotcache.cache.AbstractCache;
import com.qfxl.hotcache.domain.AreaCity;
import com.qfxl.hotcache.domain.AreaCounty;
import com.qfxl.hotcache.domain.AreaProvince;
import com.qfxl.hotcache.model.AreaProvinceVO;
import com.qfxl.hotcache.service.AreaCityService;
import com.qfxl.hotcache.service.AreaCountyService;
import com.qfxl.hotcache.service.AreaProvinceService;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

/**
 * 地区缓存
 *
 * @Author 清风徐来
 * @Date 2025-02-23 00:39
 */
@Slf4j
@Component
public class AreaCache extends AbstractCache<String, AreaProvinceVO> {

    @Resource
    private AreaProvinceService areaProvinceService;
    @Resource
    private AreaCityService areaCityService;
    @Resource
    private AreaCountyService areaCountyService;

    // 缓存KEY
    private final static String AREA_CACHE_KEY = "area:cache:key";

    @SneakyThrows
    @Override
    public void init() {
        long startTime = System.currentTimeMillis();

        List<AreaProvince> provinces = areaProvinceService.list();
        if (provinces.isEmpty()) {
            return;
        }

        List<CompletableFuture<Void>> futures = provinces.stream().map(
                province -> CompletableFuture.runAsync(() -> {
                    AreaProvinceVO areaProvinceVO = buildProvinceTree(province);
                    redisTemplate.opsForValue().set(AREA_CACHE_KEY + province.getProvinceId(), JSON.toJSONString(areaProvinceVO));
                })
        ).toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .get(15, TimeUnit.MINUTES);

        long endTime = System.currentTimeMillis();
        log.info("共加载省份: {} 个, 用时: {} ms", provinces.size(), endTime - startTime);

    }

    private AreaProvinceVO buildProvinceTree(AreaProvince areaProvince) {
        List<AreaProvinceVO.AreaCityVO> cities = areaCityService.lambdaQuery()
                .eq(AreaCity::getProvinceId, areaProvince.getProvinceId())
                .list().stream().map(this::buildCityTree).toList();

        return AreaProvinceVO.builder()
                .provinceId(areaProvince.getProvinceId())
                .provinceName(areaProvince.getName())
                .cities(cities)
                .build();
    }

    private AreaProvinceVO.AreaCityVO buildCityTree(AreaCity areaCity) {
        List<AreaProvinceVO.AreaCountyVO> counties = areaCountyService.lambdaQuery()
                .eq(AreaCounty::getCityId, areaCity.getCityId())
                .list().stream().map(county ->
                        AreaProvinceVO.AreaCountyVO.builder()
                                .countyId(county.getCountyId())
                                .countyName(county.getName())
                                .build()).toList();

        return AreaProvinceVO.AreaCityVO.builder()
                .cityId(areaCity.getCityId())
                .cityName(areaCity.getName())
                .counties(counties)
                .build();
    }

    @Override
    public AreaProvinceVO get(String provinceId) {
        if (!redisTemplate.hasKey(AREA_CACHE_KEY + provinceId)) {
            return null;
        }

        return JSONObject.parseObject(redisTemplate.opsForValue().get(AREA_CACHE_KEY + provinceId).toString(), AreaProvinceVO.class);
    }

    @Override
    public void clear() {
        redisTemplate.delete(redisTemplate.keys(AREA_CACHE_KEY + "*"));
    }

}
