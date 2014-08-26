package com.eric.huatianzhi.login;

import org.json.JSONObject;

import android.app.Activity;

import com.eric.huatianzhi.utils.MLog;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class QQLoginUtility {
	private static final String TAG = "QQLoginUtility";
	private static QQAuth mQQAuth;
	private static Tencent mTencent;
	private static String mAppid = "222222";
	private static QQLoginUtility qqLoginUtility;
	private UserInfo mInfo;
	private Activity activity;

	private QQLoginUtility(Activity activity) {
		mQQAuth = QQAuth.createInstance(mAppid, activity);
		mTencent = Tencent.createInstance(mAppid, activity);
		this.activity = activity;
	}

	public static QQLoginUtility getInstance(Activity activity) {
		if (qqLoginUtility == null) {
			qqLoginUtility = new QQLoginUtility(activity);
		}
		return qqLoginUtility;
	}

	public void doLogin() {
		if (!mQQAuth.isSessionValid()) {
			IUiListener listener = new BaseUiListener() {
				@Override
				protected void doComplete(JSONObject values) {
					updateUserInfo();
					updateLoginButton();
				}
			};
			// mQQAuth.login(this, "all", listener);
			mTencent.login(activity, "all", listener);
		} else {
			mQQAuth.logout(activity);
			updateUserInfo();
			updateLoginButton();
		}
	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			MLog.d(TAG, response.toString());
			doComplete((JSONObject) response);
		}

		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError e) {
			MLog.e(TAG, e.errorDetail);
		}

		@Override
		public void onCancel() {
			MLog.d(TAG, "onCancel");
		}
	}

	private void updateUserInfo() {
		if (mQQAuth != null && mQQAuth.isSessionValid()) {
			IUiListener listener = new IUiListener() {

				@Override
				public void onError(UiError e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onComplete(final Object response) {
					MLog.d(TAG, response.toString());
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub

				}
			};
			mInfo = new UserInfo(activity, mQQAuth.getQQToken());
			mInfo.getUserInfo(listener);

		} else {
			// mUserInfo.setText("");
			// mUserInfo.setVisibility(android.view.View.GONE);
			// mUserLogo.setVisibility(android.view.View.GONE);
		}
	}

	private void updateLoginButton() {
		if (mQQAuth != null && mQQAuth.isSessionValid()) {
			// mNewLoginButton.setTextColor(Color.RED);
			// mNewLoginButton.setText("退出帐号");
		} else {
			// mNewLoginButton.setTextColor(Color.BLUE);
			// mNewLoginButton.setText("登录");
		}
	}
}
