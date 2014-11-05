package com.eric.huatianzhi.login;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.eric.huatianzhi.BaseActivity;
import com.eric.huatianzhi.R;
import com.eric.huatianzhi.utils.MLog;
import com.eric.huatianzhi.utils.URLConstants;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class QQLoginUtility {
	private static final Integer QQLOGIN_TYPE = 2;
	private static final String TAG = "QQLoginUtility";
	private static Tencent mTencent;
	private static String mAppid = "1102297284";
	private static QQLoginUtility qqLoginUtility;
	private UserInfo mInfo;
	private BaseActivity activity;
	private String openId = null;

	private QQLoginUtility(BaseActivity activity) {
		mTencent = Tencent.createInstance(mAppid,
				activity.getApplicationContext());
		this.activity = activity;
	}

	public static QQLoginUtility getInstance(BaseActivity activity) {
		if (qqLoginUtility == null) {
			qqLoginUtility = new QQLoginUtility(activity);
		}
		return qqLoginUtility;
	}

	public void doLogin() {
		if (!mTencent.isSessionValid()) {
			IUiListener listener = new BaseUiListener() {
				@Override
				protected void doComplete(JSONObject values) {
					super.doComplete(values);
					updateUserInfo();
					updateLoginButton();
				}
			};
			mTencent.login(activity, "all", listener);
		} else {
			mTencent.logout(activity);
			updateUserInfo();
			updateLoginButton();
		}
	}

	private void doJiaqiLogin(String nickName) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("Login", openId);
		params.put("Password", "");
		params.put("LoginType", QQLOGIN_TYPE);
		params.put("InvitationCode", "3");
		params.put("DisplayName", nickName);
		JSONObject paramsJson = new JSONObject(params);
		MLog.d(TAG, "jaqilogin params:" + paramsJson.toString());
		activity.addRequest(new JsonObjectRequest(URLConstants.LOGIN_URL,
				paramsJson, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						MLog.d(TAG,response.toString());
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						MLog.e(TAG, error.toString());
						Toast.makeText(activity, activity
								.getString(R.string.login_error_pls_try_later),
								Toast.LENGTH_LONG).show();;
					}
				}));
	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			MLog.d(TAG, "onComplete: " + response.toString());
			doComplete((JSONObject) response);
		}

		protected void doComplete(JSONObject values) {
			openId = values.optString("openid");
		}

		@Override
		public void onError(UiError e) {
			MLog.e(TAG, "onError: " + e.errorDetail);
			openId = null;
		}

		@Override
		public void onCancel() {
			MLog.d(TAG, "onCancel");
			openId = null;
		}
	}

	private void updateUserInfo() {
		if (mTencent != null && mTencent.isSessionValid()) {
			IUiListener listener = new IUiListener() {

				@Override
				public void onError(UiError e) {
				}

				@Override
				public void onComplete(final Object response) {
					MLog.d(TAG,
							"updateUserInfo onComplete: " + response.toString());
					String nickName = ((JSONObject) response)
							.optString("nickname");
					doJiaqiLogin(nickName);
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
			// mNewLoginButton.setTextColor(Color.RED);
			// mNewLoginButton.setText("退出帐号");
		} else {
			// mNewLoginButton.setTextColor(Color.BLUE);
			// mNewLoginButton.setText("登录");
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (mTencent != null) {
			mTencent.onActivityResult(requestCode, resultCode, data);
		}
	}
}
