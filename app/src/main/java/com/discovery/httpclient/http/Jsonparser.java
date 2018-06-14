package com.discovery.httpclient.http;


import com.discovery.httpclient.utils.TLog;
import com.google.gson.Gson;

/**
 * Created by ruanwenjiang
 * Desc ${TODO}.
 * 该类会进行真正的数据解析
 */

public class Jsonparser {
    public static final String TAG = "RWJ Jsonparser";
    public Jsonparser() {
    }

    public static IResponse parse(String json, IResponse response) throws ParseException {
        //IResponse iResponse = JSON.parseObject(json, response.getClass());
        Gson gson = new Gson();
        IResponse iResponse = gson.fromJson(json,response.getClass());
        if (iResponse == null && response instanceof BaseResponse) {
            iResponse = response;
            ((BaseResponse) response).native_code=IResponse.RESPONSE_DATA_ERROR;
            TLog.d(TAG,"Json parse error");
        }
        return iResponse;
    }
}
