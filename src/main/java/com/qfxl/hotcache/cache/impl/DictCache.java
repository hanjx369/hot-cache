package com.qfxl.hotcache.cache.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.qfxl.hotcache.cache.AbstractCache;
import com.qfxl.hotcache.domain.SysDictData;
import com.qfxl.hotcache.domain.SysDictType;
import com.qfxl.hotcache.model.DictVO;
import com.qfxl.hotcache.service.SysDictDataService;
import com.qfxl.hotcache.service.SysDictTypeService;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 字典缓存
 *
 * @Author 清风徐来
 * @Date 2025-02-23 01:24
 */
@Slf4j
@Component
public class DictCache extends AbstractCache<String, DictVO> {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private SysDictTypeService dictTypeService;
    @Resource
    private SysDictDataService dictDataService;

    private final static String DICT_CACHE_KEY = "dict:cache:key";

    @SneakyThrows
    @Override
    public void init() {
        long startTime = System.currentTimeMillis();

        List<SysDictType> dictTypes = dictTypeService.list();
        if (dictTypes.isEmpty()) {
            return;
        }

        List<CompletableFuture<Void>> futures = dictTypes.stream().map(
                dictType -> CompletableFuture.runAsync(() -> {
                    DictVO dictVO = buildDictTree(dictType);
                    redisTemplate.opsForValue().set(DICT_CACHE_KEY + dictType.getDictType(), JSON.toJSONString(dictVO));
                })
        ).toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .get(15, TimeUnit.MINUTES);

        long endTime = System.currentTimeMillis();
        log.info("共加载字典: {} 个, 用时: {} ms", dictTypes.size(), endTime - startTime);
    }

    private DictVO buildDictTree(SysDictType dictType) {
        List<DictVO.DictDataVO> dictDataList = dictDataService.lambdaQuery()
                .eq(SysDictData::getDictType, dictType.getDictType())
                .list().stream().map(dictData -> DictVO.DictDataVO.builder()
                        .dictValue(dictData.getDictValue())
                        .dictLabel(dictData.getDictLabel())
                        .build()).toList();

        return DictVO.builder()
                .dictType(dictType.getDictType())
                .dictName(dictType.getDictName())
                .dictDataList(dictDataList)
                .build();
    }

    @Override
    public DictVO get(String dictType) {
        if (!redisTemplate.hasKey(DICT_CACHE_KEY + dictType)) {
            return null;
        }

        return JSONObject.parseObject(redisTemplate.opsForValue().get(DICT_CACHE_KEY + dictType).toString(), DictVO.class);
    }

    @Override
    public void clear() {
        redisTemplate.delete(redisTemplate.keys(DICT_CACHE_KEY + "*"));
    }

}
