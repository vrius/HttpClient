package com.discovery.httpclient.http;




/**
 * 具有和AysncTask功能的二个方法?
 */
public interface AsyncTaskObserver extends Observer {
	/**
	 * 任务执行前的动作
	 */
	void onPreExecute(CommonRequest request);
	
	/**
	 * 任务执行后的动作
	 */
	void onPostExecute(BaseResponse response);
}



