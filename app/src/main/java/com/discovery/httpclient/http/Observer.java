package com.discovery.httpclient.http;


/**
 * 观察者
 */
public interface Observer {
	void update(Observable o, BaseResponse response);
}

