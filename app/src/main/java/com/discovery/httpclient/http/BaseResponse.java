package com.discovery.httpclient.http;


public class BaseResponse implements IResponse, ISqlite {
    private static final long serialVersionUID = 6688782956294038971L;

    //网来请求本地赋予求情码
    public int native_code;

    public int getNative_code() {
        return native_code;
    }

    public void setNative_code(int native_code) {
        this.native_code = native_code;
    }

    @Override
    public IResponse parse(String responseContent) throws ParseException {
        return Jsonparser.parse(responseContent, this);
    }

    @Override
    public boolean saveToDB(String body, CommonRequest request) {
        return false;
    }

    @Override
    public String getByDB(CommonRequest request) {
        return null;
    }
}
