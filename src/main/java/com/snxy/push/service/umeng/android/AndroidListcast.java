package com.snxy.push.service.umeng.android;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lvhf
 * @date 2017/11/29
 */
//   listcast-列播，要求不超过500个device_token
public class AndroidListcast extends AndroidNotification {
	public AndroidListcast(String appkey, String appMasterSecret){
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