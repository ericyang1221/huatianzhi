package com.eric.huatianzhi.fragments;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.android.volley.toolbox.ImageLoader;
import com.eric.huatianzhi.R;
import com.eric.huatianzhi.utils.MLog;
import com.eric.huatianzhi.view.RoundImageView;
import com.eric.volley.VolleySingleton;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class PhotoAlbumFragment extends BaseFragment {
	private final String TAG = "PhotoAlbumFragment";
	private PLA_AdapterView<ListAdapter> mAdapterView = null;
	private WaterFallAdapter mAdapter = null;
	private LayoutInflater inflater;

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
		return layout;
	}
	
	private void registerFunc(){
		final SlidingMenu menu = new SlidingMenu(getBaseActivity());
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindWidthRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(getBaseActivity(), SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.login_menu);
		getMainActivity().setTitleLeft(0, new OnClickListener(){
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
				convertView = inflater.inflate(R.layout.album_item, null);
				holder.imageView = (RoundImageView) convertView
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
		public RoundImageView imageView;
	}

	private void initAdapter() {
		List<String> l = new ArrayList<String>();
		l.add("http://pic12.nipic.com/20110108/1592733_103230329000_2.jpg");
		l.add("http://bbs.szonline.net/UploadFile/album/2012/1/508245/10/20120110093131_13505.jpg");
		l.add("http://photocdn.sohu.com/20090331/Img263119749.jpg");
		l.add("http://photo.dizo.com.cn/article/2011/4/23/2e18546f-f64c-4d92-83b2-90a8fb7000b1.jpg");
		l.add("http://pic-shopping.bcia.com.cn/s/thump/110223/dc90cb1d8aba4f45774c8d6fdba94fdc_2_322_243.jpg");
		l.add("http://imgt1.bdstatic.com/it/u=4081395060,2324339507&fm=90&gp=0.jpg");
		l.add("http://imgt4.bdstatic.com/it/u=2961223305,208614355&fm=21&gp=0.jpg");
		l.add("http://www.meihua.info/today/post/image.axd?picture=kuxia3.jpg");
		mAdapter = new WaterFallAdapter(l);
	}
}
