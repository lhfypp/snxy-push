package com.snxy.push.service.umeng.android;

import com.google.common.collect.Maps;
import com.snxy.push.service.umeng.AndroidMessageBody;
import lombok.ToString;

import java.util.Map;

//   unicast-单播
public class AndroidUnicast extends AndroidNotification {

    ///begin
    public AndroidUnicast(String appkey,String appMasterSecret) {
        setAppMasterSecret(appMasterSecret);
        setPredefinedKeyValue("appkey", appkey);
        this.setPredefinedKeyValue("type", "unicast");
    }

    public void setDeviceToken(String token)  {
        setPredefinedKeyValue("device_tokens", token);
    }

    ///end

//    private AndroidUnicast(String appkey, String appMasterSecret, AndroidMessageBody androidMessageBody, Map<String, String> dataMap) {
//        super(appkey, appMasterSecret, androidMessageBody);
//        initRequestParam(dataMap);
//    }
//
//    private void initRequestParam(Map<String, String> dataMap) {
//        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
//            if (StringUtil.isNotBlank(entry.getValue())) {
//                setPredefinedKeyValue(entry.getKey(), entry.getValue());
//            }
//        }
//    }

    public static AndroidUnicastBuilder builder() {
        return new AndroidUnicastBuilder();
    }

    @ToString
    public static class AndroidUnicastBuilder {

        private String appkey;
        private String appMasterSecret;

    ////    private String type = PushTypeEnum.PUSH_TYPE_UNI_CAST.getDescription();

        private String deviceToken;
        private String displayType;
        private String timestamp;
        private String productionMode;

        private AndroidMessageBody androidMessageBody;

        public AndroidUnicastBuilder appkey(String appkey) {
            this.appkey = appkey;
            return this;
        }

        public AndroidUnicastBuilder appMasterSecret(String appMasterSecret) {
            this.appMasterSecret = appMasterSecret;
            return this;
        }

        public AndroidUnicastBuilder deviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
            return this;
        }

        public AndroidUnicastBuilder displayType(String displayType) {
            this.displayType = displayType;
            return this;
        }

        public AndroidUnicastBuilder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public AndroidUnicastBuilder productionMode(String productionMode) {
            this.productionMode = productionMode;
            return this;
        }

        public AndroidUnicastBuilder messageBody(AndroidMessageBody androidMessageBody) {
            this.androidMessageBody = androidMessageBody;
            return this;
        }

        public AndroidUnicast build() {
            Map<String, String> dataMap = buildMap();
            return new AndroidUnicast(appkey, appMasterSecret);
           // return new AndroidUnicast(appkey, appMasterSecret, androidMessageBody, dataMap);
        }

        private Map<String, String> buildMap() {
            Map<String, String> map = Maps.newHashMap();
            map.put("appkey", appkey);
      ////      map.put("type", type);
            map.put("timestamp", timestamp);
            map.put("device_tokens", deviceToken);
            map.put("display_type", displayType);
            map.put("production_mode", productionMode);
            return map;

        }

    }
}
