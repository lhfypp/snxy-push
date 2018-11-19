package com.snxy.push.service.web;

import com.snxy.common.response.ResultData;
import com.snxy.push.service.service.MessageInfo;
import com.snxy.push.service.service.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: zhangfeng233
 * @Date: 2018/11/14 13:56
 */
@Slf4j
@RestController
@RequestMapping("/uPush")
public class PushController {
    @Resource
    PushService pushService;

    @RequestMapping("/send")
    public ResultData send(MessageInfo messageInfo){
        boolean b = pushService.pushMessage(messageInfo);
        return ResultData.success("");
    }

}
