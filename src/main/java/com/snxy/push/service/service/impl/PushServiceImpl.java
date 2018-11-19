package com.snxy.push.service.service.impl;

import com.snxy.push.service.config.PushConfigBean;
import com.snxy.push.service.service.MessageInfo;
import com.snxy.push.service.service.PushService;
import com.snxy.push.service.umeng.AndroidMessageBody;
import com.snxy.push.service.umeng.BaseNotification;
import com.snxy.push.service.umeng.IOSMessageBody;
import com.snxy.push.service.umeng.PushClient;
import com.snxy.push.service.umeng.android.AndroidListcast;
import com.snxy.push.service.umeng.android.AndroidNotification;
import com.snxy.push.service.umeng.android.AndroidUnicast;
import com.snxy.push.service.umeng.ios.IOSListcast;
import com.snxy.push.service.umeng.ios.IOSNotification;
import com.snxy.push.service.umeng.ios.IOSUnicast;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: zhangfeng233
 * @Date: 2018/11/14 11:59
 */
@Service
@Slf4j
public class PushServiceImpl implements PushService {
    //推送成功返回的状态码是200
    private static final int RESPONSE_CODE_SUCCESS = 200;
    // 列播大小限制是500
    private final static int MAX_DEVICE_SIZE = 500;
    @Resource
    private PushConfigBean pushConfigBean;


    @Override
    //使用客户端发送请求
    public boolean push(BaseNotification notification) {
        HttpResponse httpResponse;
        boolean result = false;
        String respStr = "";
        try {
            notification.setPredefinedKeyValue("timestamp", getCurrentTimestamp());//add 2017-11-30
            //发送请求
            httpResponse = PushClient.send(notification);
            //获取返回信息
            respStr = EntityUtils.toString(httpResponse.getEntity(), Charset.forName("UTF-8"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            //  throw new BizException(PUSH_ERROR_MSG);
            return false;
        }

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == RESPONSE_CODE_SUCCESS) {
            result = true;
            log.info("发送成功。。。");
            log.info(respStr);
        } else {
            log.error("response:[{}]", respStr);
            //    throw new BizException(PUSH_ERROR_MSG);
        }

        return result;
    }

    @Override
    //苹果单播列播
    public boolean pushIOSListOrUnicast(IOSMessageBody iosMessageBody, String... deviceTokens) {
        //TODO
        if (deviceTokens == null || deviceTokens.length == 0) {
            return false;
        }
        if (deviceTokens.length == 1) {
            return this.pushIOSUnicast(iosMessageBody, deviceTokens[0]);
        }

        if (deviceTokens.length <= MAX_DEVICE_SIZE) { //列播大小限制是500
            return this.pushIOSListcastIn500(iosMessageBody, Arrays.asList(deviceTokens));
        }
        List<List<String>> listTokens = this.splitCollection(500, deviceTokens);
        for (List<String> tokens : listTokens) {
            this.pushIOSListcastIn500(iosMessageBody, tokens);
        }
        return true;
    }

    @Override
    //安卓单播列播
    public boolean pushAndroidListOrUnicast(AndroidMessageBody androidMessageBody, String... deviceTokens) {
        if (deviceTokens == null || deviceTokens.length == 0) {
            return false;
        }
        if (deviceTokens.length == 1) {
            return this.pushAndroidrUnicast(androidMessageBody, deviceTokens[0]);
        }

        if (deviceTokens.length <= MAX_DEVICE_SIZE) {
            return this.pushAndroidListcastIn500(androidMessageBody, Arrays.asList(deviceTokens));
        }
        List<List<String>> listTokens = this.splitCollection(MAX_DEVICE_SIZE, deviceTokens);
        for (List<String> tokens : listTokens) {
            this.pushAndroidListcastIn500(androidMessageBody, tokens);
        }
        return true;
    }

    @Override
    //发送请求，自行判断发送类型，设备类型，调用相关接口进行发送
    public boolean pushMessage(MessageInfo messageInfo) {
        List<MessageInfo.DeviceInfo> iosDeviceInfos = new ArrayList<>();
        List<MessageInfo.DeviceInfo> androidDeviceInfos = new ArrayList<>();
        //遍历参数中的配置信息，存入对应的集合中
        messageInfo.getDeviceInfos().parallelStream().forEach(deviceInfo -> {
            if ("ios".equals(deviceInfo.getPhoneType())) {
                iosDeviceInfos.add(deviceInfo);
            } else {
                androidDeviceInfos.add(deviceInfo);
            }
        });
        if (iosDeviceInfos.size() > 0) {        //ios的配置集合长度大于0则进行操作
            //添加消息实体
            IOSMessageBody iosMessageBody = IOSMessageBody.builder()
                                        .subtitle(messageInfo.getTitle())
                                        .body(messageInfo.getRemark())
                                        .title(messageInfo.getTicker())
                                        .moduleType(messageInfo.getModuleType())
                                        .noticeId(messageInfo.getNoticeId())
                                        .childId(messageInfo.getChildId())
                                        .noticeDate(messageInfo.getNoticeDate())
                                        .build();
            //遍历集合，根据deviceTokens的长度选择单播还是列播
            iosDeviceInfos.parallelStream().forEach(iosDeviceInfo -> {
                this.pushIOSListOrUnicast(iosMessageBody,iosDeviceInfos.parallelStream()
                        .map(MessageInfo.DeviceInfo::getDeviceToken).collect(Collectors.toList())
                        .toArray(new String[iosDeviceInfos.size()]));
            });
        }
        if (androidDeviceInfos.size() > 0) {        ////ios的配置集合长度大于0则进行操作
            //添加消息实体
            AndroidMessageBody androidMessageBody = AndroidMessageBody.builder()
                                                .title(messageInfo.getTitle())
                                                .text(messageInfo.getRemark())
                                                .ticker(messageInfo.getTicker())
                                                .moduleType(messageInfo.getModuleType())
                                                .noticeId(messageInfo.getNoticeId())
                                                .childId(messageInfo.getChildId())
                                                .noticeDate(messageInfo.getNoticeDate())
                                                .build();
            androidDeviceInfos.parallelStream().forEach(androidDeviceInfo ->{
                this.pushAndroidListOrUnicast(androidMessageBody,androidDeviceInfos.parallelStream()
                        .map(MessageInfo.DeviceInfo::getDeviceToken).collect(Collectors.toList())
                        .toArray(new String[androidDeviceInfos.size()]));
            });



        }
        return true;
    }


    //ios单播
    private boolean pushIOSUnicast(IOSMessageBody imb, String deviceToken) {
        IOSUnicast unicast = new IOSUnicast(pushConfigBean.getIosAppKey(), pushConfigBean.getIosAppMasterKey());

        unicast.setDeviceToken(deviceToken);
        this.setIosCast(unicast, imb);

        return this.push(unicast);
    }
    //ios列播，不多于500个deviceToken
    private boolean pushIOSListcastIn500(IOSMessageBody imb, List<String> deviceTokens) {
        IOSListcast listcast = new IOSListcast(pushConfigBean.getIosAppKey(), pushConfigBean.getIosAppMasterKey());

        listcast.setDeviceToken(deviceTokens);

        this.setIosCast(listcast, imb);

        return this.push(listcast);
    }
    //安卓单播
    private boolean pushAndroidrUnicast(AndroidMessageBody amb, String deviceToken) {
        log.info("AndroidAppKey[{}]", pushConfigBean.getAndroidAppKey());
        AndroidUnicast unicast = new AndroidUnicast(pushConfigBean.getAndroidAppKey(), pushConfigBean.getAndroidAppMasterSecret());
        unicast.setDeviceToken(deviceToken);
        this.setAndroidCast(unicast, amb);
        return this.push(unicast);
    }
    //安卓列播，不多于500个deviceToken
    private boolean pushAndroidListcastIn500(AndroidMessageBody amb, List<String> deviceTokens) {
        log.info("AndroidAppKey[{}]", pushConfigBean.getAndroidAppKey());
        AndroidListcast listcast = new AndroidListcast(pushConfigBean.getAndroidAppKey(), pushConfigBean.getAndroidAppMasterSecret());
        listcast.setDeviceToken(deviceTokens);
        this.setAndroidCast(listcast, amb);

        return this.push(listcast);
    }

    //ios：把messagebody的属性添加到notification
    private void setIosCast(IOSNotification iosNotification, IOSMessageBody imb) {
        if (imb.getAlert() == null || "".equals(imb.getAlert())) {
            iosNotification.setAlert(imb.getAlertJsonObject());
        } else {
            iosNotification.setAlert(imb.getAlert());
        }
        iosNotification.setBadge(imb.getBadge());
        iosNotification.setSound(imb.getSound());
        Map<String, String> extraDataMap = imb.getExtraDataMap();
        for (Map.Entry<String, String> kv : extraDataMap.entrySet()) {
            iosNotification.setCustomizedField(kv.getKey(), kv.getValue());//自定义字段
        }
        //设置测试模式还是正式模式！！！！有问题
        if (pushConfigBean.getIosProductionMode()) {
            iosNotification.setProductionMode();
        } else {
            iosNotification.setTestMode();
        }
    }

    //安卓：把messagebody的属性添加到notification，unicast继承了notification
    private void setAndroidCast(AndroidNotification androidNotification, AndroidMessageBody amb) {
        androidNotification.setTicker(amb.getTicker());
        androidNotification.setTitle(amb.getTitle());
        androidNotification.setText(amb.getText());

        if (amb.getAfterOpen() != null) {
            switch (amb.getAfterOpen()) {
                case go_app:
                    androidNotification.goAppAfterOpen();
                    break;
                case go_url:
                    androidNotification.goUrlAfterOpen(amb.getGoUrl());
                    break;
//                case go_activity:
//                    break;
                default:
                    androidNotification.goCustomAfterOpen(amb.getCustomJsonObject());//调整goAppAfterOpen();
                    break;
            }

        } else { //默认跳转到自定义
            androidNotification.goCustomAfterOpen(amb.getCustomJsonObject());//调整goAppAfterOpen();
        }
        androidNotification.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        //设置测试模式还是正式模式！！！！有问题
        if (pushConfigBean.getAndroidProductionMode()) {
            androidNotification.setProductionMode();
        } else {
            androidNotification.setTestMode();
        }
    }


    //获取当前时间
    private static String getCurrentTimestamp() {
        return Integer.toString((int) (System.currentTimeMillis() / 1000));
    }

    //把deviceTokens  每500个组成一个集合，把集合组成大集合
    private List<List<String>> splitCollection(int size, String... strParas) {
        List<List<String>> result = new ArrayList<>();
        if (strParas.length <= size) {
            result.add(Arrays.asList(strParas));
            return result;
        }
        int count = (strParas.length + size - 1) / size;
        for (int i = 0; i < count; i++) {
            List<String> temp = null;
            if (i == count - 1) {
                temp = Arrays.asList(Arrays.copyOfRange(strParas, i * size, strParas.length));
            } else {
                temp = Arrays.asList(Arrays.copyOfRange(strParas, i * size, (i + 1) * size));
            }
            result.add(temp);
        }

        return result;
    }
}
