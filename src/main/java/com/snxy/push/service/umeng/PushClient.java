package com.snxy.push.service.umeng;

import com.snxy.common.http.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;

import java.nio.charset.Charset;

@Slf4j
public class PushClient {
    private static final String REQUEST_PATH = "http://msg.umeng.com";
    private static final String API_PATH = "/api/send";
    private static final String REQUEST_METHOD = "POST";

    private PushClient() {

    }

    public static HttpResponse send(BaseNotification notification) throws Exception {
        String reqUrl = REQUEST_PATH + API_PATH;
        String sign = sign(reqUrl, notification);
        reqUrl = reqUrl + "?sign=" + sign;
        String requestBody= notification.getRequestBody();
        if(log.isDebugEnabled()){
            log.debug("message request body:"+requestBody);
        }
        return HttpClientUtil.postJson(reqUrl,requestBody);
    }

    private static String sign(String url, BaseNotification notification) {
        String requestBody = notification.getRequestBody();
        String plainText = REQUEST_METHOD + url + requestBody + notification.getAppMasterSecret();
        return DigestUtils.md5Hex(plainText.getBytes(Charset.forName("UTF-8")));
    }
}
