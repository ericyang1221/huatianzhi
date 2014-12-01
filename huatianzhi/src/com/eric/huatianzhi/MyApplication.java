package com.eric.huatianzhi;

import com.eric.EricsApplication;

public class MyApplication extends EricsApplication {
	private int loginId;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public int getLoginId() {
		return loginId;
	}

	public void setLoginId(int loginId) {
		this.loginId = loginId;
	}

}
