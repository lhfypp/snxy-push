package com.snxy.push.service.service;

import com.snxy.push.service.umeng.AndroidMessageBody;
import com.snxy.push.service.umeng.BaseNotification;
import com.snxy.push.service.umeng.IOSMessageBody;

/**
 * @Author: zhangfeng233
 * @Date: 2018/11/13 17:09
 */
public interface PushService {

    boolean push(BaseNotification notification);

    //苹果推送，列播或单点
    boolean pushIOSListOrUnicast(IOSMessageBody iosMessageBody, String... deviceTokens );

    //安卓推送，列播或单点
    boolean pushAndroidListOrUnicast(AndroidMessageBody androidMessageBody, String... deviceTokens );


    boolean pushMessage(MessageInfo messageInfo);

}
