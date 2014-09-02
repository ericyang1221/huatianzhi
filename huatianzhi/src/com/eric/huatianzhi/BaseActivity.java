package com.eric.huatianzhi;

import android.app.Activity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class BaseActivity extends Activity {
	protected RequestQueue mRequestQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRequestQueue = Volley.newRequestQueue(this);
	}
	
	protected void addRequest(@SuppressWarnings("rawtypes") Request request){
		if(mRequestQueue!=null){
			mRequestQueue.add(request);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(mRequestQueue!=null){
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
	
	
}
