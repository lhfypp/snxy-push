package com.snxy.push.service;

import com.snxy.push.service.service.MessageInfo;
import com.snxy.push.service.service.PushService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PushServiceApplicationTests {

    @Test
    public void contextLoads() {
    }
    @Resource
    PushService pushService;
    @Test
    public void test1(){
        MessageInfo messageInfo = MessageInfo.builder()
                .ticker("通知栏提示文字")
                .title("通知标题")
                .remark("通知文字描述")
                .build();
        MessageInfo.DeviceInfo deviceInfo = MessageInfo.DeviceInfo.builder()
                                    .deviceToken("AvJ6ZFIOBCrikHSypa1LeaWS-i2i7kKcEcgxGkiOO_Ev")
                                    .phoneType("android")
                                    .build();
        ArrayList<MessageInfo.DeviceInfo> deviceInfos = new ArrayList<>();
        deviceInfos.add(deviceInfo);

        messageInfo.setDeviceInfos(deviceInfos);

        pushService.pushMessage(messageInfo);

    }
}
