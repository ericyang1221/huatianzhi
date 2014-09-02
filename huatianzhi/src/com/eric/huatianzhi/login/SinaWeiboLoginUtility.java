package com.eric.huatianzhi.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.eric.huatianzhi.utils.AccessTokenKeeper;
import com.eric.huatianzhi.utils.MLog;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;

public class SinaWeiboLoginUtility {
	private final String TAG = "SinaWeiboLoginUtility";
	private static SinaWeiboLoginUtility sinaWeiboLoginUtility;
	private SsoHandler mSsoHandler;
	private WeiboAuth mWeiboAuth;
	private Oauth2AccessToken mAccessToken;
	private Activity activity;
	public static final String APP_KEY = "2584622791";
	public static final String REDIRECT_URL = "http://www.sina.com";
	public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";

	private SinaWeiboLoginUtility(Activity activity) {
		mWeiboAuth = new WeiboAuth(activity, APP_KEY, REDIRECT_URL, SCOPE);
		this.activity = activity;
	}

	public static SinaWeiboLoginUtility getInstance(Activity activity) {
		if (sinaWeiboLoginUtility == null) {
			sinaWeiboLoginUtility = new SinaWeiboLoginUtility(activity);
		}
		return sinaWeiboLoginUtility;
	}

	public void doLogin() {
		mSsoHandler = new SsoHandler(activity, mWeiboAuth);
		mSsoHandler.authorize(new AuthListener());
	}

	class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				AccessTokenKeeper.writeAccessToken(activity, mAccessToken);
				MLog.d(TAG, "auth success.");
				UsersAPI mUsersAPI = new UsersAPI(mAccessToken);
				RequestListener mListener = new RequestListener() {
					@Override
					public void onComplete(String response) {
						if (!TextUtils.isEmpty(response)) {
							User user = User.parse(response);
							MLog.d(TAG, user.name);
						}
					}

					@Override
					public void onWeiboException(WeiboException arg0) {
						// TODO Auto-generated method stub
						
					}
				};
				long uid = Long.parseLong(mAccessToken.getUid());
				mUsersAPI.show(uid, mListener);
			} else {
				// 以下几种情况，您会收到 Code：
				// 1. 当您未在平台上注册的应用程序的包名与签名时；
				// 2. 当您注册的应用程序包名与签名不正确时；
				// 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
				String code = values.getString("code");
				String message = "auth failed.";
				if (!TextUtils.isEmpty(code)) {
					message = message + "\nObtained the code: " + code;
				}
				MLog.e(TAG, message);
			}
		}

		@Override
		public void onCancel() {
			MLog.d(TAG, "Auth onCancel");
		}

		@Override
		public void onWeiboException(WeiboException e) {
			MLog.e(TAG, "Auth exception : " + e.getMessage());
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// SSO 授权回调
		// 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
}
