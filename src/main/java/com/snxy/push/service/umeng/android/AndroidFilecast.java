package com.snxy.push.service.umeng.android;

//   filecast-文件播，多个device_token可通过文件形式批量发送
public class AndroidFilecast extends AndroidNotification {
	public AndroidFilecast(String appkey,String appMasterSecret) {
			setAppMasterSecret(appMasterSecret);
			setPredefinedKeyValue("appkey", appkey);
			this.setPredefinedKeyValue("type", "filecast");	
	}
	
	public void setFileId(String fileId)  {
    	setPredefinedKeyValue("file_id", fileId);
    }
}