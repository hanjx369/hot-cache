package com.qfxl.hotcache.cache;

/**
 * 缓存热启动接口
 *
 * @Author 清风徐来
 * @Date 2025-02-23 00:37
 */
public interface IHotCache<T, R> {

    /**
     * 初始化缓存
     */
    void init();

    /**
     * 获取数据
     *
     * @param requestParams 参数
     * @return 返回值
     */
    R get(T requestParams);

    /**
     * 清除缓存
     */
    void clear();

    /**
     * 重载
     */
    void reload();

}
