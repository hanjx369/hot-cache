package com.qfxl.hotcache.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 省份信息VO
 *
 * @Author 清风徐来
 * @Date 2025-02-23 00:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AreaProvinceVO {

    // 省份编码
    private String provinceId;

    // 省份名称
    private String provinceName;

    // 城市列表
    private List<AreaCityVO> cities;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class AreaCityVO {

        // 城市编码
        private String cityId;

        // 城市名称
        private String cityName;

        // 区县列表
        private List<AreaCountyVO> counties;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class AreaCountyVO {

        // 区县编码
        private String countyId;

        // 区县名称
        private String countyName;

    }

}
