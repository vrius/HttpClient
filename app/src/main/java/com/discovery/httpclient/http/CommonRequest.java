package com.discovery.httpclient.http;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 基础请求类
 */
public class CommonRequest implements Serializable {
    private static final long serialVersionUID = 5561138003630664433L;
    public static final String TAG = "RWJ CommonRequest";
    private Map<String, Object> mMap;
    private HttpTypeEnum httpType = HttpTypeEnum.GET;
    private ContentTypeEnum contentType = ContentTypeEnum.APPLICATION_FORM;
    private MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");
    private String baseUrl;
    private String json;
    private long requestTimeout = 10000L;
    private long connetionTimeout = 10000L;

    public enum HttpTypeEnum {
        GET(0X01), POST(0X02);
        private int type;

        HttpTypeEnum(int type) {
            this.type = type;
        }
    }

    public enum ContentTypeEnum {
        APPLICATION_FORM(0x11), APPLICATION_JSON(0x12), APPLICATION_FILE(0x13);
        private int type;

        ContentTypeEnum(int type) {
            this.type = type;
        }
    }

    public CommonRequest(String url){
        this.baseUrl = url;
        mMap = new HashMap<>();
    }

    public CommonRequest(String url,HttpTypeEnum httpType){
        this.baseUrl = url;
        this.httpType = httpType;
        mMap = new HashMap<>();
    }

    public CommonRequest(String url, HttpTypeEnum httpType, ContentTypeEnum contentType) {
        this.baseUrl = url;
        this.httpType = httpType;
        this.contentType = contentType;
        mMap = new HashMap<>();
        if (contentType == ContentTypeEnum.APPLICATION_FORM) {
            this.mediaType = MediaType.parse("application/json; charset=UTF-8");
        } else if (contentType == ContentTypeEnum.APPLICATION_JSON) {
            this.mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");
        } else if (contentType == ContentTypeEnum.APPLICATION_FILE) {
            this.mediaType = MediaType.parse("application/form-data; charset=UTF-8");
        }
        // TODO: 18-6-14
    }

    public void putParam(String key, String value) {
        if (contentType == ContentTypeEnum.APPLICATION_JSON) {
            throw new RuntimeException("CommonRequest Type inconsistency Exception!");
        }
        mMap.put(key, value);
    }

    public void putParam(Map<String, String> map) {
        if (contentType == ContentTypeEnum.APPLICATION_JSON) {
            throw new RuntimeException("CommonRequest:Type inconsistency Exception!");
        }
        if (map != null && map.size() > 0) {
            Set<Map.Entry<String, String>> entrySet = map.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                mMap.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getFullUrl() {
        if (httpType != HttpTypeEnum.GET) {
            throw new RuntimeException("CommonRequest:Current request is not get!");
        }
        Set<Map.Entry<String, Object>> entrySet = mMap.entrySet();
        StringBuilder buff = new StringBuilder();
        buff.append("?");
        int count = 0;
        for (Map.Entry<String, Object> entry : entrySet) {
            buff.append(count == 0 ? entry.getKey() + "=" + entry.getValue() : "&" + entry.getKey()
                    + "=" + entry.getValue());
            count++;
        }
        return baseUrl + buff.toString();
    }

    public RequestBody getBody() {
        if (httpType != HttpTypeEnum.POST) {
            throw new RuntimeException("CommonRequest:Current request is not post!");
        }

        if (contentType == ContentTypeEnum.APPLICATION_FORM) {
            FormBody.Builder body = new FormBody.Builder();
            Set<Map.Entry<String, Object>> entrySet = mMap.entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                body.add(entry.getKey(), String.valueOf(entry.getValue()));
            }
            return body.build();
        }

        if (contentType == ContentTypeEnum.APPLICATION_JSON) {
            RequestBody body = RequestBody.create(mediaType, json);
            return body;
        }

        return null;
    }

    public String getJson() {
        if (contentType != ContentTypeEnum.APPLICATION_JSON) {
            throw new RuntimeException("CommonRequest:Type inconsistency Exception!");
        }
        return json;
    }

    public void setJson(String json) {
        if (contentType != ContentTypeEnum.APPLICATION_JSON) {
            throw new RuntimeException("CommonRequest:Type inconsistency Exception!");
        }
        this.json = json;
    }

    public HttpTypeEnum getHttpType() {
        return httpType;
    }

    public ContentTypeEnum getContentType() {
        return contentType;
    }

    public Map<String, Object> getMap() {
        return mMap;
    }

    public long getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(long requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public long getConnetionTimeout() {
        return connetionTimeout;
    }

    public void setConnetionTimeout(long connetionTimeout) {
        this.connetionTimeout = connetionTimeout;
    }
}
