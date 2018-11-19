package com.snxy.push.service.umeng.android;

import com.alibaba.fastjson.JSONObject;
import com.snxy.push.service.umeng.BaseNotification;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * @author zhuchangbin
 * @date 2017/10/16
 */
@Slf4j
public class AndroidNotification extends BaseNotification {

    private static final HashSet<String> PAYLOAD_KEYS;

    private static final HashSet<String> BODY_KEYS;

    private static final List<String> ROOT_FIELD;

    static {
        PAYLOAD_KEYS = new HashSet<>(Collections.singletonList("display_type"));

        BODY_KEYS = new HashSet<>(Arrays.asList("ticker", "title", "text", "builder_id", "icon", "largeIcon", "img", "play_vibrate", "play_lights", "play_sound",
                "sound", "after_open", "url", "activity", "custom"));

        ROOT_FIELD = Arrays.asList("payload", "body", "policy", "extra");
    }

//    protected AndroidNotification() {
//    }
//
//    public AndroidNotification(String appkey, String appMasterSecret, AndroidMessageBody messageBody) {
//        super(appkey, appMasterSecret);
//        initRequestMessageBodyParam(messageBody);
//    }
//
//    private void initRequestMessageBodyParam(AndroidMessageBody messageBody) {
//        Map<String, String> dataMap = messageBody.getDataMap();
//        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
//            if (StringUtil.isNotBlank(entry.getValue())) {
//                setPredefinedKeyValue(entry.getKey(), entry.getValue());
//            }
//        }
//    }

    @Override
    public boolean setPredefinedKeyValue(String key, Object value) {
        if (ROOT_KEYS.contains(key)) {
            rootJson.put(key, value);
        } else if (PAYLOAD_KEYS.contains(key)) {
            JSONObject payloadJson = null;
            if (rootJson.containsKey("payload")) {
                payloadJson = rootJson.getJSONObject("payload");
            } else {
                payloadJson = new JSONObject();
                rootJson.put("payload", payloadJson);
            }
            payloadJson.put(key, value);
        } else if (BODY_KEYS.contains(key)) {
            JSONObject bodyJson = null;
            JSONObject payloadJson = null;
            if (rootJson.containsKey("payload")) {
                payloadJson = rootJson.getJSONObject("payload");
            } else {
                payloadJson = new JSONObject();
                rootJson.put("payload", payloadJson);
            }
            if (payloadJson.containsKey("body")) {
                bodyJson = payloadJson.getJSONObject("body");
            } else {
                bodyJson = new JSONObject();
                payloadJson.put("body", bodyJson);
            }
            bodyJson.put(key, value);
        } else if (POLICY_KEYS.contains(key)) {
            JSONObject policyJson = null;
            if (rootJson.containsKey("policy")) {
                policyJson = rootJson.getJSONObject("policy");
            } else {
                policyJson = new JSONObject();
                rootJson.put("policy", policyJson);
            }
            policyJson.put(key, value);
        } else {
            if (ROOT_FIELD.contains(key)) {
                log.warn("You don't need to set value for {}, just set values for the sub keys in it.", key);
            } else {
                log.warn("unknown key:[{}], value:[{}]", key, value);
            }
        }
        return true;
    }

    public boolean setExtraField(String key, String value)   {
        JSONObject payloadJson = null;
        JSONObject extraJson = null;
        if (rootJson.containsKey("payload")) {
            payloadJson = rootJson.getJSONObject("payload");
        } else {
            payloadJson = new JSONObject();
            rootJson.put("payload", payloadJson);
        }

        if (payloadJson.containsKey("extra")) {
            extraJson = payloadJson.getJSONObject("extra");
        } else {
            extraJson = new JSONObject();
            payloadJson.put("extra", extraJson);
        }
        extraJson.put(key, value);
        return true;
    }

    //
    public void setDisplayType(DisplayType d) {
        setPredefinedKeyValue("display_type", d.getValue());
    }

    ///通知栏提示文字
    public void setTicker(String ticker){
        setPredefinedKeyValue("ticker", ticker);
    }

    ///通知标题
    public void setTitle(String title) {
        setPredefinedKeyValue("title", title);
    }

    ///通知文字描述
    public void setText(String text) {
        setPredefinedKeyValue("text", text);
    }

    ///用于标识该通知采用的样式。使用该参数时, 必须在SDK里面实现自定义通知栏样式。
    public void setBuilderId(Integer builder_id) {
        setPredefinedKeyValue("builder_id", builder_id);
    }

    ///状态栏图标ID, R.drawable.[smallIcon],如果没有, 默认使用应用图标。
    public void setIcon(String icon)  {
        setPredefinedKeyValue("icon", icon);
    }

    ///通知栏拉开后左侧图标ID
    public void setLargeIcon(String largeIcon){
        setPredefinedKeyValue("largeIcon", largeIcon);
    }

    ///通知栏大图标的URL链接。该字段的优先级大于largeIcon。该字段要求以http或者https开头。
    public void setImg(String img) {
        setPredefinedKeyValue("img", img);
    }

    ///收到通知是否震动,默认为"true"
    public void setPlayVibrate(Boolean play_vibrate) {
        setPredefinedKeyValue("play_vibrate", play_vibrate.toString());
    }

    ///收到通知是否闪灯,默认为"true"
    public void setPlayLights(Boolean play_lights) {
        setPredefinedKeyValue("play_lights", play_lights.toString());
    }

    ///收到通知是否发出声音,默认为"true"
    public void setPlaySound(Boolean play_sound)  {
        setPredefinedKeyValue("play_sound", play_sound.toString());
    }

    ///通知声音，R.raw.[sound]. 如果该字段为空，采用SDK默认的声音
    public void setSound(String sound)  {
        setPredefinedKeyValue("sound", sound);
    }

    ///收到通知后播放指定的声音文件
    public void setPlaySound(String sound){
        setPlaySound(true);
        setSound(sound);
    }

    public enum DisplayType {

        /**
         * 通知:消息送达到用户设备后，由友盟SDK接管处理并在通知栏上显示通知内容。
         */
        NOTIFICATION {
            @Override
            public String getValue() {
                return "notification";
            }
        },

        /**
         * 消息:消息送达到用户设备后，消息内容透传给应用自身进行解析处理。
         */
        MESSAGE {
            @Override
            public String getValue() {
                return "message";
            }
        };

        public abstract String getValue();
    }

    public enum AfterOpenAction {
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
         * 打开custom
         */
        go_custom
    }

    ///点击"通知"的后续行为，默认为打开app。
    public void goAppAfterOpen()  {
        setAfterOpenAction(AfterOpenAction.go_app);
    }

    public void goUrlAfterOpen(String url)  {
        setAfterOpenAction(AfterOpenAction.go_url);
        setUrl(url);
    }

    public void goActivityAfterOpen(String activity) {
        setAfterOpenAction(AfterOpenAction.go_activity);
        setActivity(activity);
    }

    public void goCustomAfterOpen(String custom) {
        setAfterOpenAction(AfterOpenAction.go_custom);
        setCustomField(custom);
    }

    public void goCustomAfterOpen(JSONObject custom){
        setAfterOpenAction(AfterOpenAction.go_custom);
        setCustomField(custom);
    }

    ///点击"通知"的后续行为，默认为打开app。原始接口
    public void setAfterOpenAction(AfterOpenAction action) {
        setPredefinedKeyValue("after_open", action.toString());
    }

    public void setUrl(String url) {
        setPredefinedKeyValue("url", url);
    }

    public void setActivity(String activity){
        setPredefinedKeyValue("activity", activity);
    }

    ///can be a string of json
    public void setCustomField(String custom) {
        setPredefinedKeyValue("custom", custom);
    }

    public void setCustomField(JSONObject custom){
        setPredefinedKeyValue("custom", custom);
    }
}
