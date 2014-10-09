package com.eric.huatianzhi;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.eric.huatianzhi.fragments.PhotoAlbumFragment;
import com.eric.huatianzhi.fragments.PhotoDetailFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends BaseActivity implements OnClickListener {
	private View navAlbum;
	private View navInvitation;
	private View navFriend;
	private View navInfo;
	private ImageView albumImg;
	private ImageView invitationImg;
	private ImageView friendImg;
	private ImageView infoImg;
	private TextView albumText;
	private TextView invitationText;
	private TextView friendText;
	private TextView infoText;
	private PhotoAlbumFragment photoAlbumFragment;
	private PhotoDetailFragment photoDetailFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SlidingMenu menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.RIGHT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindWidthRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.menu);

		initViews();
		setTabSelection(R.id.nav_album);
	}

	private void initViews() {
		navAlbum = findViewById(R.id.nav_album);
		navInvitation = findViewById(R.id.nav_invitation);
		navFriend = findViewById(R.id.nav_friend);
		navInfo = findViewById(R.id.nav_info);
		navAlbum.setOnClickListener(this);
		navInvitation.setOnClickListener(this);
		navFriend.setOnClickListener(this);
		navInfo.setOnClickListener(this);

		albumImg = (ImageView) findViewById(R.id.albumImg);
		invitationImg = (ImageView) findViewById(R.id.invitationImg);
		friendImg = (ImageView) findViewById(R.id.friendImg);
		infoImg = (ImageView) findViewById(R.id.infoImg);

		albumText = (TextView) findViewById(R.id.albumText);
		invitationText = (TextView) findViewById(R.id.invitationText);
		friendText = (TextView) findViewById(R.id.friendText);
		infoText = (TextView) findViewById(R.id.infoText);
	}

	@Override
	public void onClick(View v) {
		setTabSelection(v.getId());
	}

	private void setTabSelection(int index) {
		clearSelection();
		clearStack();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		int pink = Color.parseColor("#df4f74");
		switch (index) {
		case R.id.nav_album:
			albumImg.setImageResource(R.drawable.album_selected);
			albumText.setTextColor(pink);
			photoAlbumFragment = new PhotoAlbumFragment();
			transaction.replace(R.id.content, photoAlbumFragment);
			break;
		case R.id.nav_invitation:
			invitationImg.setImageResource(R.drawable.invitation_selected);
			invitationText.setTextColor(pink);
			break;
		case R.id.nav_friend:
			friendImg.setImageResource(R.drawable.friends_selected);
			friendText.setTextColor(pink);
			break;
		case R.id.nav_info:
			infoImg.setImageResource(R.drawable.info_selected);
			infoText.setTextColor(pink);
		default:
			break;
		}
		transaction.commit();
		fragmentManager.executePendingTransactions();
	}

	private void clearSelection() {
		int gray = Color.parseColor("#666666");
		albumImg.setImageResource(R.drawable.album_unselected);
		albumText.setTextColor(gray);
		invitationImg.setImageResource(R.drawable.invitation_unselected);
		invitationText.setTextColor(gray);
		friendImg.setImageResource(R.drawable.friends_unselected);
		friendText.setTextColor(gray);
		infoImg.setImageResource(R.drawable.info_unselected);
		infoText.setTextColor(gray);
	}

	private void clearStack() {
		fragmentManager.popBackStack(null,
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}
}
