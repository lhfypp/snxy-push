package com.snxy.push.service.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: zhangfeng233
 * @Date: 2018/11/14 14:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class PushConfigBean {
    private String androidAppKey;
    private String androidAppMasterSecret;
    private String iosAppKey;
    private String iosAppMasterKey;

    private Boolean androidProductionMode;
    //   @Value("${push.iosProductionMode}")
    private Boolean iosProductionMode;
}
