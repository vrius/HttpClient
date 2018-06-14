package com.discovery.httpclient.http;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.discovery.httpclient.utils.TLog;


/**
 * common task
 */
public abstract class ServiceCommonTask<T> extends AsyncTask<T, Void, Void> implements OnCancelListener {
	public static final String TAG = "RWJ ServiceCommonTask";
	public ServiceCommonTask(Context context, String init){
		this(context, init, false);
	}
	
	public ServiceCommonTask(Context context, int init){
		this(context, init, false);
	}
	
	public ServiceCommonTask(Context context, String init, boolean isOutsideDismiss){
		mContext = context;
		mInit = init;
		this.isOutsideDismiss = isOutsideDismiss;
	}
	
	public ServiceCommonTask(Context context, int init, boolean isOutsideDismiss){
		mContext = context;
		this.isOutsideDismiss = isOutsideDismiss;
		if(init!=0){
			try {
				mInit = context.getString(init);
			} catch (Exception e) {
				mInit = "";
			}
		}
	}
	
	protected void cancel() {
		try {
			if(mDialog!=null) mDialog.dismiss();
			mContext = null;
			mDialog = null;
		} catch (Exception e) {
			TLog.d(TAG,e.getMessage());
		}
	}

	@Override
	protected void onPostExecute(Void result) {
		try {
			if(mDialog!=null) mDialog.dismiss();
		} catch (Exception e) {
			TLog.d(TAG,e.getMessage());
		}
	}

	@Override
	protected void onPreExecute() {
		if(!TextUtils.isEmpty(mInit) || isShowEmpty){
			mDialog = new ProgressDialog(mContext);
			if(!TextUtils.isEmpty(mInit))mDialog.setMessage(mInit);
			mDialog.setCanceledOnTouchOutside(isOutsideDismiss);
			mDialog.setOnCancelListener(this);
			mDialog.show();
		}
	}
	
	@Override
	public void onCancel(DialogInterface dialog) {
		onCancel();
	}
	
	protected abstract void onCancel();

	protected ProgressDialog mDialog;
	protected String         mInit;
	protected Context        mContext;
	protected boolean isShowEmpty = false;
	protected boolean isOutsideDismiss = true;
}
