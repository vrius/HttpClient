package com.discovery.httpclient.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;


import com.discovery.httpclient.utils.NetUtils;
import com.discovery.httpclient.utils.TLog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HttpManager extends BaseManager implements IManager {
    public static final String TAG = "RWJ HttpManager";
    private static boolean isCompress = false;
    private static HttpManager mManager = new HttpManager();
    private LinkedList<HttpTask> mHttpTasks = new LinkedList<>();

    public LinkedList<HttpTask> getHttpTasks() {
        return mHttpTasks;
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static synchronized HttpManager getInstance() {
        return mManager;
    }

    /**
     * 设置是否对发送的数据进行压缩
     *
     * @param compress
     */
    public synchronized void setCompress(boolean compress) {
        isCompress = compress;
    }

    @Override
    public void cancel(Observer observer) {
        ArrayList<HttpTask> removeTasks = new ArrayList<>();
        for (HttpTask task : mHttpTasks) {
            if (task.mObserver == observer) {
                task.cancel();
                removeTasks.add(task);
            }
        }
        mHttpTasks.removeAll(removeTasks);
    }

    @Override
    public synchronized void deleteObserver(Observer o) {
        super.deleteObserver(o);
        cancel(o);
    }


    @Override
    public void send(Context context, CommonRequest request, BaseResponse response, Observer
            observer) {
        mHttpTasks.add(new HttpTask(context, request, response, observer).execute());
    }

    /*
     * 请求task
     */
    protected class HttpTask extends BaseTask {
        protected boolean isCanceled = false; // 是否为取消

        public HttpTask(Context context, CommonRequest request, BaseResponse response, Observer
                observer) {
            super(context, request, response, observer);
        }

        public HttpTask(Context context, int tipId, CommonRequest request, BaseResponse response,
                        Observer observer) {
            super(context, tipId, request, response, observer);
        }

        /**
         * 是否取消
         *
         * @return boolean
         * @throws
         */
        public boolean isCanceled() {
            return isCanceled;
        }

        @SuppressLint("NewApi")
        public HttpTask execute() {
            super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            return this;
        }

        @Override
        protected void cancel() {
            super.cancel();
            isCanceled = true;
            cancel(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (isCanceled) {
                TLog.d(TAG, "Request canceled");
                mResponse.native_code = IResponse.RESPONSE_USER_CANCEL;
                return null;
            }

            if (!NetUtils.isNetworkAvailable(mContext)) {
                TLog.d(TAG, "Request network error");
                mResponse.native_code = IResponse.RESPONSE_NO_NETWORK;
                return null;
            }

            try {
                CommonRequest.HttpTypeEnum type = mRequest.getHttpType();
                Request request;
                if (type == CommonRequest.HttpTypeEnum.POST) {
                    String baseUrl = mRequest.getBaseUrl();
                    RequestBody body = mRequest.getBody();
                    TLog.d(TAG, "POST Request url = " + baseUrl);
                    TLog.d(TAG, "POST Request param=" + mRequest.getMap().toString());
                    request = new Request.Builder().tag(mObserver).post(body).url(baseUrl).build();
                } else {
                    String fullUrl = mRequest.getFullUrl();
                    TLog.d(TAG, "GET Request url = " + fullUrl);
                    TLog.d(TAG, "GET Request param=" + mRequest.getMap().toString());
                    request = new Request.Builder().tag(mObserver).get().url(fullUrl).build();
                }
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(mRequest.getConnetionTimeout(), TimeUnit.MILLISECONDS)
                        .readTimeout(mRequest.getRequestTimeout(), TimeUnit.MILLISECONDS)
                        .build();
                Response response = client.newCall(request).execute();
                TLog.d(TAG, "Response message=" + response.toString());
                if (response.isSuccessful() && !isCanceled) {
                    String responseStr = response.body().string();
                    TLog.d(TAG, "Response string = " + responseStr);
                    BaseResponse bresponse = (BaseResponse) mResponse.parse(responseStr);
                    mResponse = bresponse;
                    mResponse.native_code = IResponse.RESPONSE_SUCC;
                } else {
                    mResponse.native_code = IResponse.RESPONSE_FAIL;
                    TLog.d(TAG, "Request error");
                }
            } catch (Exception e) {
                if (e instanceof ConnectException || e instanceof SocketTimeoutException) {
                    mResponse.native_code = IResponse.RESPONSE_NETWORK_ERROR;
                } else {
                    mResponse.native_code = IResponse.RESPONSE_DATA_ERROR;
                }
                TLog.d(TAG, "Request exception:" + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            TLog.d(TAG, "------------Http OnPostExecute-----------");
            if (!isCanceled) {
                super.onPostExecute(result);
            } else
                mHttpTasks.remove(this);
        }

        @Override
        public boolean equals(Object o) {
            HttpTask task = (HttpTask) o;
            if (task == null) {
                return false;
            }
            return mContext == task.mContext
                    && mRequest == task.mRequest
                    && mResponse == task.mResponse
                    && (mInit != null && task.mInit != null && mInit.equals(task.mInit));
        }
    }
}