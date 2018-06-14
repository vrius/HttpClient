package com.discovery.httpclient.http;

import java.io.Serializable;

/**
 * Created by ruanwenjiang
 * Desc ${TODO}.
 */


public interface IResponse extends Serializable {
    int RESPONSE_FAIL = -1;
    int RESPONSE_USER_CANCEL = -2;
    int RESPONSE_DATA_ERROR = -3;
    int RESPONSE_NO_NETWORK = -4;
    int RESPONSE_NETWORK_ERROR = -5;
    int RESPONSE_SUCC = 200;

    IResponse parse(String var1) throws ParseException;
}
