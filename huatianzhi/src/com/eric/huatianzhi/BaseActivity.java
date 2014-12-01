package com.eric.huatianzhi;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.eric.volley.VolleySingleton;

public class BaseActivity extends FragmentActivity {
	protected RequestQueue mRequestQueue;
	private MyApplication myApp;
	protected FragmentManager fragmentManager;
	private ProgressDialog pd;

	public MyApplication getMyApplication() {
		if (myApp == null) {
			myApp = (MyApplication) this.getApplication();
		}
		return myApp;
	}

	public FragmentManager getTheFragmentManager() {
		if (fragmentManager == null) {
			fragmentManager = this.getSupportFragmentManager();
		}
		return fragmentManager;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRequestQueue = VolleySingleton.getInstance().getRequestQueue();
		fragmentManager = getTheFragmentManager();
	}

	@SuppressWarnings("unchecked")
	public void addRequest(@SuppressWarnings("rawtypes") Request request) {
		if (mRequestQueue != null) {
			mRequestQueue.add(request);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void showProgressDialog() {
		if (pd == null) {
			pd = new ProgressDialog(this);
			pd.setTitle(getString(R.string.loading));
			pd.setIndeterminate(true);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		}
		pd.show();
	}

	public void dismissProgressDialog() {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
			pd = null;
		}
	}
}
