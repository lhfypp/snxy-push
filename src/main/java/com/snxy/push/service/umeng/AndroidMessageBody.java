package com.snxy.push.service.umeng;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 推送信息的通知内容-android
 *
 * @author zhuchangbin
 * @date 2017/10/16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AndroidMessageBody {

    private String ticker;
    private String title;
    private String text;
    private AfterOpenAction afterOpen;

    //start 为go_custom定义json对象
    private Integer moduleType;
    private Long noticeId;
    private Long childId;
//   private Long classId;
    private String noticeDate;
   //end

    //go_url 跳转目标页面
    private String goUrl;

    public String getCustomJsonString(){
        return  getCustomJsonObject().toJSONString();
    }
    public JSONObject getCustomJsonObject(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("moduleType",moduleType);
        jsonObject.put("noticeId",noticeId);
       jsonObject.put("childId",childId);
        //      jsonObject.put("classId",classId);
        jsonObject.put("noticeDate",noticeDate);
        return  jsonObject;
    }
    public static enum AfterOpenAction {
        /**
         * 打开app
         */
        go_app,
        /**
         * 打开url
         */
        go_url,
        /**
         * 打开activity
         */
        go_activity,
        /**
         * 打开custom(自定义)
         */
        go_custom
    }
}
