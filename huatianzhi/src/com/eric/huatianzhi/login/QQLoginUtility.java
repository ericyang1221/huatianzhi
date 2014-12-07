package com.eric.huatianzhi.login;

import org.json.JSONObject;

import android.content.Intent;

import com.eric.huatianzhi.BaseActivity;
import com.eric.huatianzhi.R;
import com.eric.huatianzhi.utils.MLog;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class QQLoginUtility extends LoginUtility {
	private static final String TAG = "QQLoginUtility";
	private final Integer QQLOGIN_TYPE = 2;
	private static Tencent mTencent;
	private static String mAppid = "1102297284";
	private static QQLoginUtility qqLoginUtility;
	private UserInfo mInfo;

	protected QQLoginUtility(BaseActivity activity) {
		super(activity);
		mTencent = Tencent.createInstance(mAppid,
				activity.getApplicationContext());
		loginType = QQLOGIN_TYPE;
	}

	public static QQLoginUtility getInstance(BaseActivity activity) {
		MLog.d(TAG, "getInstance: " + qqLoginUtility);
		if (qqLoginUtility == null) {
			qqLoginUtility = new QQLoginUtility(activity);
		}
		return qqLoginUtility;
	}

	@Override
	public void doLogin(final String invitationCode) {
		if (!mTencent.isSessionValid()) {
			IUiListener listener = new BaseUiListener() {
				@Override
				protected void doComplete(JSONObject values) {
					super.doComplete(values);
					updateUserInfo(invitationCode);
					updateLoginButton();
				}
			};
			mTencent.login(activity, "all", listener);
		} else {
			mTencent.logout(activity);
			updateUserInfo(invitationCode);
			updateLoginButton();
		}
	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			MLog.d(TAG, "onComplete: " + response.toString());
			doComplete((JSONObject) response);
		}

		protected void doComplete(JSONObject values) {
			loginId = values.optString("openid");
		}

		@Override
		public void onError(UiError e) {
			MLog.e(TAG, "onError: " + e.errorDetail);
			loginId = null;
		}

		@Override
		public void onCancel() {
			MLog.d(TAG, "onCancel");
			loginId = null;
		}
	}

	private void updateUserInfo(final String invitationCode) {
		if (mTencent != null && mTencent.isSessionValid()) {
			IUiListener listener = new IUiListener() {

				@Override
				public void onError(UiError e) {
					showLoginErrorToast(activity
							.getString(R.string.login_error_pls_try_later));
				}

				@Override
				public void onComplete(final Object response) {
					MLog.d(TAG,
							"updateUserInfo onComplete: " + response.toString());
					String nickName = ((JSONObject) response)
							.optString("nickname");
					doJiaqiLogin(nickName, invitationCode);
				}

				@Override
				public void onCancel() {
				}
			};
			mInfo = new UserInfo(activity, mTencent.getQQToken());
			mInfo.getUserInfo(listener);

		} else {
			// mUserInfo.setText("");
			// mUserInfo.setVisibility(android.view.View.GONE);
			// mUserLogo.setVisibility(android.view.View.GONE);
		}
	}

	private void updateLoginButton() {
		if (mTencent != null && mTencent.isSessionValid()) {
		} else {
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (mTencent != null) {
			mTencent.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onDestory() {
		qqLoginUtility = null;
	}
}
