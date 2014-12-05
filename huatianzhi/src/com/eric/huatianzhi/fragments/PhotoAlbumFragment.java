package com.eric.huatianzhi.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.eric.huatianzhi.R;
import com.eric.huatianzhi.beans.AlbumBean;
import com.eric.huatianzhi.beans.JoinedWeddingBean;
import com.eric.huatianzhi.utils.MLog;
import com.eric.huatianzhi.utils.URLConstants;
import com.eric.huatianzhi.view.RoundImageView;
import com.eric.volley.VolleySingleton;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class PhotoAlbumFragment extends BaseFragment {
	private final String TAG = "PhotoAlbumFragment";
	private PLA_AdapterView<ListAdapter> mAdapterView = null;
	private WaterFallAdapter mAdapter = null;
	private LayoutInflater inflater;
	private JoinedWeddingBean jwb;

	public PhotoAlbumFragment(JoinedWeddingBean jwb) {
		this.jwb = jwb;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		registerFunc();
		this.inflater = inflater;
		View layout = inflater.inflate(R.layout.photo_album, container, false);
		mAdapterView = (PLA_AdapterView<ListAdapter>) layout
				.findViewById(R.id.list);
		mAdapter = new WaterFallAdapter();
		mAdapterView.setAdapter(mAdapter);
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
		initAdapter();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	private class WaterFallAdapter extends BaseAdapter {
		private ImageLoader imageLoader = VolleySingleton.getInstance()
				.getImageLoader();
		private List<AlbumBean> dataList = new ArrayList<AlbumBean>();

		public WaterFallAdapter() {
			super();
		}

		public void updateData(List<AlbumBean> dataList) {
			if (dataList != null) {
				this.dataList = dataList;
				this.notifyDataSetChanged();
				MLog.d(TAG, "update data");
			} else {
				MLog.d(TAG, "dataList is null");
			}
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
				convertView = inflater.inflate(R.layout.album_item, null);
				holder.imageView = (RoundImageView) convertView
						.findViewById(R.id.thumbnail);
				holder.nLike = (TextView) convertView.findViewById(R.id.nlike);
				holder.nLike.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(final View v) {
						HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("LoginID", getMyApplication().getLoginId());
						params.put("ItemID", dataList.get(position).getItemId());
						JSONObject paramsJson = new JSONObject(params);
						MLog.d(TAG, "nlike params:" + paramsJson.toString());
						getBaseActivity().addRequest(
								new JsonObjectRequest(URLConstants.NLIKE_URL,
										paramsJson,
										new Response.Listener<JSONObject>() {
											@Override
											public void onResponse(
													JSONObject response) {
												MLog.d(TAG, response.toString());
												JSONObject jo = response
														.optJSONObject("ItemInfo");
												String nLike = jo
														.optString("NLike");
												((TextView) v).setText(nLike);
											}
										}, new Response.ErrorListener() {
											@Override
											public void onErrorResponse(
													VolleyError error) {
												MLog.e(TAG, error.toString());
											}
										}));
					}
				});
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// holder.imageView.setDefaultImageResId(R.drawable.ic_launcher);
			// holder.imageView.setErrorImageResId(R.drawable.ic_launcher);
			MLog.d(TAG,
					"position: " + position + "    url: "
							+ dataList.get(position));
			holder.imageView.setImageUrl(dataList.get(position).getThumbURL(),
					imageLoader);
			holder.nLike.setText(String.valueOf(dataList.get(position)
					.getnLike()));
			return convertView;
		}
	}

	class ViewHolder {
		public RoundImageView imageView;
		public TextView nLike;
	}

	private void initAdapter() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("LoginID", getMyApplication().getLoginId());
		params.put("AlbumID", jwb.getPreWeddingPhotoAlbumID());
		JSONObject paramsJson = new JSONObject(params);
		MLog.d(TAG, "viewAlbum params:" + paramsJson.toString());
		getBaseActivity().addRequest(
				new JsonObjectRequest(URLConstants.VIEW_ALBUM_URL, paramsJson,
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								MLog.d(TAG, response.toString());
								JSONArray ja = response
										.optJSONArray("PhotoInfos");
								List<AlbumBean> abList = new ArrayList<AlbumBean>();
								for (int i = 0; i < ja.length(); i++) {
									JSONObject j = ja.optJSONObject(i);
									AlbumBean ab = AlbumBean.fromJson(j);
									abList.add(ab);
								}
								mAdapter.updateData(abList);
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								MLog.e(TAG, error.toString());

							}
						}));
	}
}
