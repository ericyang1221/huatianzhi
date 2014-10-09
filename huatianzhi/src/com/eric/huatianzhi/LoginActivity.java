package com.eric.huatianzhi;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.eric.huatianzhi.login.QQLoginUtility;
import com.eric.huatianzhi.login.SinaWeiboLoginUtility;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class LoginActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		findViewById(R.id.qqlogin).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				QQLoginUtility.getInstance(LoginActivity.this).doLogin();
			}

		});
		findViewById(R.id.weibologin).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SinaWeiboLoginUtility.getInstance(LoginActivity.this).doLogin();
			}

		});
		
		findViewById(R.id.look_around).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i = new Intent(LoginActivity.this,MainActivity.class);
				startActivity(i);
				finish();
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		SinaWeiboLoginUtility.getInstance(LoginActivity.this).onActivityResult(
				requestCode, resultCode, data);
		QQLoginUtility.getInstance(LoginActivity.this).onActivityResult(
				requestCode, resultCode, data);
	}
}
