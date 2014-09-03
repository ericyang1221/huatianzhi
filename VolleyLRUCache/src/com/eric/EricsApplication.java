package com.eric;

import android.app.Application;
import android.content.Context;

public class EricsApplication extends Application {
	protected static Context mAppContext;

	@Override
	public void onCreate() {
		super.onCreate();
		this.setAppContext(getApplicationContext());
	}

	public static Context getAppContext() {
		return mAppContext;
	}

	public void setAppContext(Context mAppContext) {
		EricsApplication.mAppContext = mAppContext;
	}
}
