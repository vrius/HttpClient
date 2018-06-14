package com.discovery.httpclient.http;

import android.content.Context;

public abstract class BaseManager extends Observable {

	/**
	 * create manager
	 * 
	 * @param context
	 * @return
	 */
	public static BaseManager createManager(Context context) {
		return HttpManager.getInstance();
	}

	protected abstract void cancel(Observer o);

	protected abstract class BaseTask extends ServiceCommonTask<Void> {

		protected CommonRequest mRequest;
		protected BaseResponse mResponse;
		public    Observer     mObserver;

		public BaseTask(Context context, CommonRequest request,
						BaseResponse response, Observer observer) {
			super(context, 0);
			mRequest = request;
			mResponse = response;
			mObserver = observer;
			if (observer != null)
				addObserver(observer);
		}

		public BaseTask(Context context, int tipId, CommonRequest request,
						BaseResponse response, Observer observer) {
			super(context, tipId);
			mRequest = request;
			mResponse = response;
			mObserver = observer;
			if (observer != null)
				addObserver(observer);
		}

		@Override
		protected Void doInBackground(Void... params) { // 从数据库获取数据
			mResponse.native_code = IResponse.RESPONSE_SUCC;
			return null;
		}

		@Override
		protected void cancel() {
			super.cancel();
			mObserver = null;
		}

		@Override
		protected void onCancel() {
			mResponse.native_code = IResponse.RESPONSE_USER_CANCEL;
			if (mObserver != null && mObserver instanceof AsyncTaskObserver)
				((AsyncTaskObserver) mObserver).onPostExecute(mResponse);
			if (mObserver != null)
				notifyObserver(mObserver, mResponse);
			cancel();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (mObserver instanceof AsyncTaskObserver)
				((AsyncTaskObserver) mObserver).onPreExecute(mRequest);
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (mObserver != null && mObserver instanceof AsyncTaskObserver)
				((AsyncTaskObserver) mObserver).onPostExecute(mResponse);
			if (mObserver != null)
				notifyObserver(mObserver, mResponse);
		}
	}



}
