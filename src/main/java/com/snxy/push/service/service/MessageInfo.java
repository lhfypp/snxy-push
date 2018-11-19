package com.snxy.push.service.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by lvhf on 2018-01-03.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageInfo {

    private String ticker;  // 必填，通知栏提示文字
    private String title;   // 必填，通知标题
    private String remark;  // 必填，通知文字描述

    private Integer moduleType;
    private Long noticeId;
    private Long childId;
    private String noticeDate;

    private List<DeviceInfo> deviceInfos;
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeviceInfo{
        String deviceToken;     //必填，设备的唯一标识
        String phoneType;       //必填，设备类型：android、ios
//        Integer userSourceId;
    }
}
