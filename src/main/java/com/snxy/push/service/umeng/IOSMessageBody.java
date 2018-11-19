package com.snxy.push.service.umeng;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 推送信息的通知内容-ios
 *
 * @author zhuchangbin
 * @date 2017/10/17
 */
@Data
@Builder
public class IOSMessageBody {

    /*
     *  为兼容低版本？设置该字段值，则不需要json对象
     */
      private  String alert;
    /*
     * 默认值 0
     */
    private Integer badge;
    /*
     * 默认值 default
     */
    private String sound;



    private String title;
    private String subtitle;
    private String body;



    public  JSONObject getAlertJsonObject(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title",title);
        jsonObject.put("subtitle",subtitle);
        jsonObject.put("body",body);
        return  jsonObject;
    }
    private Integer moduleType;
    private Long noticeId;
    private Long childId;
    private String noticeDate;

    public Map<String, String> getExtraDataMap() {
        Map<String, String> dataMap = Maps.newHashMap();
        dataMap.put("moduleType", moduleType.toString());
        dataMap.put("noticeId", noticeId.toString());
        dataMap.put("childId", childId==null? null:childId.toString());
        dataMap.put("noticeDate", noticeDate);
        return dataMap;
    }
}
