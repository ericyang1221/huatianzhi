package com.eric.huatianzhi.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.eric.huatianzhi.BaseActivity;
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

public class SinaWeiboLoginUtility extends LoginUtility {
	private final String TAG = "SinaWeiboLoginUtility";
	private final Integer SINA_LOGIN_TYPE = 1;
	private static SinaWeiboLoginUtility sinaWeiboLoginUtility;
	private SsoHandler mSsoHandler;
	private WeiboAuth mWeiboAuth;
	private Oauth2AccessToken mAccessToken;
	public static final String APP_KEY = "2584622791";
	public static final String REDIRECT_URL = "http://www.sina.com";
	public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";

	protected SinaWeiboLoginUtility(BaseActivity activity) {
		super(activity);
		mWeiboAuth = new WeiboAuth(activity, APP_KEY, REDIRECT_URL, SCOPE);
		loginType = SINA_LOGIN_TYPE;
	}

	public static SinaWeiboLoginUtility getInstance(BaseActivity activity) {
		if (sinaWeiboLoginUtility == null) {
			sinaWeiboLoginUtility = new SinaWeiboLoginUtility(activity);
		}
		return sinaWeiboLoginUtility;
	}

	@Override
	public void doLogin(String invitationCode) {
		mSsoHandler = new SsoHandler(activity, mWeiboAuth);
		mSsoHandler.authorize(new AuthListener(invitationCode));
	}

	class AuthListener implements WeiboAuthListener {
		String invitationCode;

		public AuthListener(String invitationCode) {
			this.invitationCode = invitationCode;
		}

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
						MLog.d(TAG, response);
						if (!TextUtils.isEmpty(response)) {
							User user = User.parse(response);
							String nickName = user.name;
							loginId = user.idstr;
							MLog.d(TAG, "userName:" + nickName + "  userId:"
									+ loginId);
							doJiaqiLogin(nickName, invitationCode);
						}
					}

					@Override
					public void onWeiboException(WeiboException e) {
						MLog.e(TAG, e.toString());
					}
				};
				long uid = Long.parseLong(mAccessToken.getUid());
				mUsersAPI.show(uid, mListener);
			} else {
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
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
}
