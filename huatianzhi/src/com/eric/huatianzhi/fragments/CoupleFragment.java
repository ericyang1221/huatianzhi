package com.eric.huatianzhi.fragments;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.eric.huatianzhi.R;
import com.eric.huatianzhi.beans.JoinedWeddingBean;
import com.eric.huatianzhi.utils.MLog;
import com.eric.volley.VolleySingleton;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class CoupleFragment extends BaseFragment {
	private final String TAG = "PhotoAlbumFragment";
	private ListView lv;
	private CoupleAdapter mAdapter;
	private LayoutInflater inflater;
	private List<JoinedWeddingBean> jwbList;

	public CoupleFragment(List<JoinedWeddingBean> jwbList) {
		this.jwbList = jwbList;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		registerFunc();
		this.inflater = inflater;
		View layout = inflater.inflate(R.layout.couple, container, false);
		lv = (ListView) layout.findViewById(R.id.list);
		initAdapter();
		getMainActivity().setTabIcon(R.id.nav_album);
		return layout;
	}

	private void registerFunc() {
		final SlidingMenu menu = new SlidingMenu(getBaseActivity());
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindWidthRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(getBaseActivity(), SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.login_menu);
		getMainActivity().setTitleLeft(0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				menu.showMenu();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		MLog.d(TAG, "onResume");
		lv.setAdapter(mAdapter);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	private class UrlBean {
		private String urlLeft;
		private String urlRight;

		public String getUrlLeft() {
			return urlLeft;
		}

		public void setUrlLeft(String urlLeft) {
			this.urlLeft = urlLeft;
		}

		public String getUrlRight() {
			return urlRight;
		}

		public void setUrlRight(String urlRight) {
			this.urlRight = urlRight;
		}
	}

	private class CoupleAdapter extends BaseAdapter {
		private ImageLoader imageLoader = VolleySingleton.getInstance()
				.getImageLoader();
		private List<UrlBean> dataList;

		public CoupleAdapter(List<UrlBean> dataList) {
			super();
			this.dataList = dataList;
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.couple_item, null);
				holder.imageViewLeft = (NetworkImageView) convertView
						.findViewById(R.id.thumbnail_left);
				holder.imageViewRight = (NetworkImageView) convertView
						.findViewById(R.id.thumbnail_right);
				holder.leftView = convertView
						.findViewById(R.id.couple_item_left);
				holder.rightView = convertView
						.findViewById(R.id.couple_item_right);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			MLog.d(TAG,
					"position: " + position + "    url: "
							+ dataList.get(position));
			UrlBean ub = dataList.get(position);
			if (ub != null) {
				String left = ub.getUrlLeft();
				String right = ub.getUrlRight();
				if (left != null) {
					if ("add_new".equals(left)) {
						Options opts = new BitmapFactory.Options();
						opts.inSampleSize = 2;
						Bitmap bitmap = BitmapFactory.decodeResource(
								getResources(), R.drawable.add_new,opts);
						holder.imageViewLeft.setLocalImageBitmap(bitmap);
						holder.leftView
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
									}

								});
					} else {
						holder.imageViewLeft.setImageUrl(ub.getUrlLeft(),
								imageLoader);
						holder.imageViewLeft.setLocalImageBitmap(null);
						holder.leftView
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										PhotoAlbumFragment photoAlbumFragment = new PhotoAlbumFragment(
												jwbList.get(position * 2));
										go(photoAlbumFragment);
									}

								});
					}
				} else {
					holder.leftView.setVisibility(View.GONE);
				}
				if (right != null) {
					if ("add_new".equals(right)) {
						Bitmap bitmap = BitmapFactory.decodeResource(
								getResources(), R.drawable.add_new);
						holder.imageViewRight.setLocalImageBitmap(bitmap);
						holder.rightView
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
									}

								});
					} else {
						holder.imageViewRight.setImageUrl(ub.getUrlLeft(),
								imageLoader);
						holder.imageViewRight.setLocalImageBitmap(null);
						holder.rightView
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										PhotoAlbumFragment photoAlbumFragment = new PhotoAlbumFragment(
												jwbList.get(position * 2 + 1));
										go(photoAlbumFragment);
									}

								});
					}
				} else {
					holder.rightView.setVisibility(View.INVISIBLE);
				}
			}
			return convertView;
		}
	}

	class ViewHolder {
		public NetworkImageView imageViewLeft;
		public NetworkImageView imageViewRight;
		public View leftView;
		public View rightView;
	}

	private void initAdapter() {
		List<UrlBean> ubList = new ArrayList<UrlBean>();
		if (jwbList != null && jwbList.size() > 0) {
			int len = jwbList.size();
			boolean hasNull = false;
			for (int i = 0; i < len; i = i + 2) {
				UrlBean ub = new UrlBean();
				ub.setUrlLeft(jwbList.get(i).getCoverPhotoURL());
				int ii = i + 1;
				if (ii < len) {
					ub.setUrlRight(jwbList.get(ii).getCoverPhotoURL());
				} else {
					ub.setUrlRight("add_new");
					hasNull = true;
				}
				ubList.add(ub);
			}
			if (!hasNull) {
				UrlBean ub = new UrlBean();
				ub.setUrlLeft("add_new");
				ubList.add(ub);
			}
		}
		mAdapter = new CoupleAdapter(ubList);
	}
}
