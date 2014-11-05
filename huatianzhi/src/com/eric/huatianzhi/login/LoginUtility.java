package com.eric.huatianzhi.login;

import java.util.HashMap;

import org.json.JSONObject;

import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.eric.huatianzhi.BaseActivity;
import com.eric.huatianzhi.R;
import com.eric.huatianzhi.utils.MLog;
import com.eric.huatianzhi.utils.URLConstants;

public abstract class LoginUtility {
	private static final String TAG = "LoginUtility";
	protected BaseActivity activity;
	protected Integer loginType;
	protected String loginId;

	protected LoginUtility(BaseActivity activity) {
		this.activity = activity;
	}

	public abstract void doLogin();

	protected void doJiaqiLogin(String nickName) {
		activity.showProgressDialog();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("Login", loginId);
		params.put("Password", "");
		params.put("LoginType", loginType);
		params.put("InvitationCode", "3");
		params.put("DisplayName", nickName);
		JSONObject paramsJson = new JSONObject(params);
		MLog.d(TAG, "jaqilogin params:" + paramsJson.toString());
		activity.addRequest(new JsonObjectRequest(URLConstants.LOGIN_URL,
				paramsJson, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						MLog.d(TAG, response.toString());
						activity.dismissProgressDialog();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						MLog.e(TAG, error.toString());
						showLoginErrorToast(activity
								.getString(R.string.login_error_pls_try_later));
						activity.dismissProgressDialog();
					}
				}));
	}

	protected void showLoginErrorToast(String msg) {
		Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
	}
}
