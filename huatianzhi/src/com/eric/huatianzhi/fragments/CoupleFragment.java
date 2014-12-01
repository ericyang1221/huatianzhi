package com.eric.huatianzhi.fragments;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.eric.huatianzhi.R;
import com.eric.huatianzhi.beans.JoinedWeddingBean;
import com.eric.huatianzhi.utils.MLog;
import com.eric.volley.VolleySingleton;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class CoupleFragment extends BaseFragment {
	private final String TAG = "PhotoAlbumFragment";
	private PLA_AdapterView<ListAdapter> mAdapterView = null;
	private WaterFallAdapter mAdapter = null;
	private LayoutInflater inflater;
	private List<JoinedWeddingBean> jwbList;

	public CoupleFragment(List<JoinedWeddingBean> jwbList) {
		this.jwbList = jwbList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		registerFunc();
		this.inflater = inflater;
		View layout = inflater.inflate(R.layout.couple, container, false);
		mAdapterView = (PLA_AdapterView<ListAdapter>) layout
				.findViewById(R.id.list);
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
		mAdapterView.setAdapter(mAdapter);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	private class WaterFallAdapter extends BaseAdapter {
		private ImageLoader imageLoader = VolleySingleton.getInstance()
				.getImageLoader();
		private List<String> dataList;

		public WaterFallAdapter(List<String> dataList) {
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
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.couple_item, null);
				holder.imageView = (NetworkImageView) convertView
						.findViewById(R.id.thumbnail);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.imageView.setDefaultImageResId(R.drawable.ic_launcher);
			holder.imageView.setErrorImageResId(R.drawable.ic_launcher);
			MLog.d(TAG,
					"position: " + position + "    url: "
							+ dataList.get(position));
			holder.imageView.setImageUrl(dataList.get(position), imageLoader);
			return convertView;
		}
	}

	class ViewHolder {
		public NetworkImageView imageView;
	}

	private void initAdapter() {
		List<String> l = new ArrayList<String>();
		if (jwbList != null && jwbList.size() > 0) {
			for (int i = 0; i < jwbList.size(); i++) {
				JoinedWeddingBean jwb = jwbList.get(i);
				
			}
		}
		l.add("http://pic12.nipic.com/20110108/1592733_103230329000_2.jpg");
		l.add("http://pic12.nipic.com/20110108/1592733_103230329000_2.jpg");
		l.add("http://pic12.nipic.com/20110108/1592733_103230329000_2.jpg");
		mAdapter = new WaterFallAdapter(l);
	}
}
