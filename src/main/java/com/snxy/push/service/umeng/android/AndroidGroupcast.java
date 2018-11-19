package com.snxy.push.service.umeng.android;

import com.alibaba.fastjson.JSONObject;

//   groupcast-组播，按照filter筛选用户群, 请参照filter参数
public class AndroidGroupcast extends AndroidNotification {
	public AndroidGroupcast(String appkey,String appMasterSecret){
			setAppMasterSecret(appMasterSecret);
			setPredefinedKeyValue("appkey", appkey);
			this.setPredefinedKeyValue("type", "groupcast");	
	}
	
	public void setFilter(JSONObject filter)  {
    	setPredefinedKeyValue("filter", filter);
    }
}
