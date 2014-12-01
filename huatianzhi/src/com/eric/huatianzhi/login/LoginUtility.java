package com.eric.huatianzhi.login;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.eric.huatianzhi.BaseActivity;
import com.eric.huatianzhi.MainActivity;
import com.eric.huatianzhi.R;
import com.eric.huatianzhi.beans.JoinedWeddingBean;
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

	public abstract void doLogin(String invitionCode);

	protected void doJiaqiLogin(String nickName, String invitationCode) {
		activity.showProgressDialog();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("Login", loginId);
		params.put("Password", "");
		params.put("LoginType", loginType);
		params.put("InvitationCode", invitationCode);
		params.put("DisplayName", nickName);
		JSONObject paramsJson = new JSONObject(params);
		MLog.d(TAG, "jaqilogin params:" + paramsJson.toString());
		activity.addRequest(new JsonObjectRequest(URLConstants.LOGIN_URL,
				paramsJson, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						MLog.d(TAG, response.toString());
						String replyEnum = response.optString("ReplyEnum");
						if ("SUCCESS".equals(replyEnum)) {
							activity.getMyApplication().setLoginId(
									response.optInt("UserID"));
							JSONArray ja = response
									.optJSONArray("JoinedWedding");
							if (ja.length() > 0) {
								List<JoinedWeddingBean> jwbList = new ArrayList<JoinedWeddingBean>();
								for (int i = 0; i < ja.length(); i++) {
									JSONObject j = ja.optJSONObject(i);
									jwbList.add(JoinedWeddingBean.fromJson(j));
								}
								Intent i = new Intent(activity,
										MainActivity.class);
								i.putExtra("joinedWeddingList",
										(Serializable) jwbList);
								activity.startActivity(i);
								activity.finish();
							} else {
								Toast.makeText(
										activity,
										activity.getString(R.string.no_joined_wedding),
										Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(
									activity,
									activity.getString(R.string.login_error_pls_try_later),
									Toast.LENGTH_LONG).show();
						}
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
