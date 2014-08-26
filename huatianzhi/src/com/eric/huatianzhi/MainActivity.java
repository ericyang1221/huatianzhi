package com.eric.huatianzhi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.eric.huatianzhi.login.QQLoginUtility;
import com.eric.huatianzhi.login.SinaWeiboLoginUtility;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.qqlogin).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				QQLoginUtility.getInstance(MainActivity.this).doLogin();
			}

		});
		findViewById(R.id.weibologin).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SinaWeiboLoginUtility.getInstance(MainActivity.this).doLogin();
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
        
        SinaWeiboLoginUtility.getInstance(MainActivity.this).onActivityResult(requestCode, resultCode, data);
    }
}
