package com.snxy.push.service.umeng.ios;

public class IOSFilecast extends IOSNotification {
	public IOSFilecast(String appkey,String appMasterSecret){
			setAppMasterSecret(appMasterSecret);
			setPredefinedKeyValue("appkey", appkey);
			this.setPredefinedKeyValue("type", "filecast");	
	}
	
	public void setFileId(String fileId)  {
    	setPredefinedKeyValue("file_id", fileId);
    }
}
