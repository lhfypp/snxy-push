package com.snxy.push.service.umeng;

import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author zhuchangbin
 * @date 2017/10/16
 */
public abstract class BaseNotification {

    protected JSONObject rootJson = new JSONObject();

    protected static final HashSet<String> ROOT_KEYS;
    protected static final HashSet<String> POLICY_KEYS;


    static {
        ROOT_KEYS = new HashSet<>(Arrays.asList("appkey", "timestamp", "type", "device_tokens", "alias", "alias_type", "file_id",
                "filter", "production_mode", "feedback", "description", "thirdparty_id"));
        POLICY_KEYS = new HashSet<>(Arrays.asList("start_time", "expire_time", "max_send_num"));
    }

    private String appkey;
    private String appMasterSecret;

    public BaseNotification() {
    }

    public BaseNotification(String appMasterSecret) {
        this.appMasterSecret = appMasterSecret;
    }

    public BaseNotification(String appkey, String appMasterSecret) {
        this.appkey = appkey;
        this.appMasterSecret = appMasterSecret;
    }

    /**
     * 根据Json对象不同层级，设定参数
     *
     * @param key   key
     * @param value value
     * @return 是否成功
     */
    public abstract boolean setPredefinedKeyValue(String key, Object value);

    public String getAppMasterSecret() {
        return appMasterSecret;
    }
    public void setAppMasterSecret(String secret) {
        appMasterSecret = secret;
    }
    public String getRequestBody() {
        return rootJson.toString();
    }
    public String getPostBody(){
        return rootJson.toString();
    }
    protected void setProductionMode(Boolean prod) {
        setPredefinedKeyValue("production_mode", prod.toString());
    }

    ///正式模式
    public void setProductionMode()  {
        setProductionMode(true);
    }

    ///测试模式
    public void setTestMode() {
        setProductionMode(true);
    }

    ///发送消息描述，建议填写。
    public void setDescription(String description) {
        setPredefinedKeyValue("description", description);
    }

    ///定时发送时间，若不填写表示立即发送。格式: "YYYY-MM-DD hh:mm:ss"。
    public void setStartTime(String startTime)  {
        setPredefinedKeyValue("start_time", startTime);
    }
    ///消息过期时间,格式: "YYYY-MM-DD hh:mm:ss"。
    public void setExpireTime(String expireTime)  {
        setPredefinedKeyValue("expire_time", expireTime);
    }
    ///发送限速，每秒发送的最大条数。
    public void setMaxSendNum(Integer num){
        setPredefinedKeyValue("max_send_num", num);
    }


}
