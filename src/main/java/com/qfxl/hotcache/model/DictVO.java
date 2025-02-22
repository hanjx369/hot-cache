package com.qfxl.hotcache.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 字典数据VO
 *
 * @Author 清风徐来
 * @Date 2025-02-23 01:25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DictVO {

    // 字典类型
    private String dictType;

    // 字典名称
    private String dictName;

    // 数据列表
    private List<DictDataVO> dictDataList;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class DictDataVO {

        // 字典数据值
        private String dictValue;

        // 字典数据标签
        private String dictLabel;

    }

}
