package com.discovery.httpclient.http;


import android.content.Context;

public interface IManager {
   void send(Context context, CommonRequest request, BaseResponse response, Observer observer);
}
