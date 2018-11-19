package com.snxy.push.service.umeng.ios;

import java.util.List;
import java.util.stream.Collectors;

public class IOSListcast extends IOSNotification {
	public IOSListcast(String appkey, String appMasterSecret){
			setAppMasterSecret(appMasterSecret);
			setPredefinedKeyValue("appkey", appkey);
			this.setPredefinedKeyValue("type", "listcast");
	}
	
	public void setDeviceToken(List<String> tokens) {
		if(tokens==null||tokens.size()==0){
			throw new IllegalArgumentException("tokens不能为空");
		}
		if(tokens.size()>500){
			throw new IllegalArgumentException("tokens超过上限500");
		}
		String strToken=tokens.parallelStream().collect(Collectors.joining(","));
    	setPredefinedKeyValue("device_tokens", strToken);
    }
}
