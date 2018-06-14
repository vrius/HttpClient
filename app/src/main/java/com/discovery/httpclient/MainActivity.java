package com.discovery.httpclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.discovery.httpclient.http.BaseResponse;
import com.discovery.httpclient.http.CommonRequest;
import com.discovery.httpclient.http.HttpManager;
import com.discovery.httpclient.http.Observable;
import com.discovery.httpclient.http.Observer;
import com.discovery.httpclient.utils.Api;
import com.discovery.httpclient.utils.TLog;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class MainActivity extends AppCompatActivity implements Observer{

    private static final String TAG = "RWJ MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TLog.d(TAG,"发送请求");
                CommonRequest request = new CommonRequest(Api.baseUrl);
                Map<String, String> map = request.getMap();
                map.put("name","盗墓笔记");
                HttpManager.getInstance().send(MainActivity.this,request,new Bean(),MainActivity.this);
                //httpTasks.add(new t)
            }
        });
    }

    @Override
    public void update(Observable o, BaseResponse response) {
        if (response instanceof Bean){
            Bean bean = (Bean) response;
            TLog.d(TAG,bean.toString());
        }
    }


    public class Bean extends BaseResponse{
        /*{
            "lon":120.58531,
                "level":2,
                "address":"",
                "cityName":"",
                "alevel":4,
                "lat":31.29888
        }*/

        public int code;
        public String msg;
        public List<String> data;

        @Override
        public String toString() {
            return "Bean{" +
                    "code=" + code +
                    ", msg='" + msg + '\'' +
                    ", data=" + data +
                    '}';
        }
    }
}
