package com.eric.huatianzhi;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.eric.huatianzhi.fragments.PhotoAlbumFragment;

public class MainActivity extends BaseActivity implements OnClickListener {
	private View navAlbum;
	private View navInvitation;
	private View navFriend;
	private View navInfo;
	private PhotoAlbumFragment photoAlbumFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
		setTabSelection(R.id.nav_album);
	}

	private void initViews() {
		navAlbum = findViewById(R.id.nav_album);
		navInvitation = findViewById(R.id.nav_invitation);
		navFriend = findViewById(R.id.nav_friend);
		navInfo = findViewById(R.id.nav_info);
	}

	@Override
	public void onClick(View v) {
		setTabSelection(v.getId());
	}

	private void setTabSelection(int index) {
		clearSelection();
		clearStack();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		switch (index) {
		case R.id.nav_album:
			navAlbum.setBackgroundColor(Color.YELLOW);
			photoAlbumFragment = new PhotoAlbumFragment();
			transaction.replace(R.id.content, photoAlbumFragment);
			break;
		case R.id.nav_invitation:
			navInvitation.setBackgroundColor(Color.YELLOW);
			break;
		case R.id.nav_friend:
			navFriend.setBackgroundColor(Color.YELLOW);
			break;
		case R.id.nav_info:
			navInfo.setBackgroundColor(Color.YELLOW);
		default:
			break;
		}
		transaction.commit();
		fragmentManager.executePendingTransactions();
	}

	private void clearSelection() {
		navAlbum.setBackgroundColor(Color.WHITE);
		navInvitation.setBackgroundColor(Color.WHITE);
		navFriend.setBackgroundColor(Color.WHITE);
		navInfo.setBackgroundColor(Color.WHITE);
	}

	private void clearStack() {
		fragmentManager.popBackStack(null,
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}
}
