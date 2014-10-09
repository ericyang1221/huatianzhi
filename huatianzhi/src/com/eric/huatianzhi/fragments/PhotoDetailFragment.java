package com.eric.huatianzhi.fragments;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.eric.huatianzhi.R;
import com.eric.huatianzhi.utils.MLog;
import com.eric.volley.VolleySingleton;

public class PhotoDetailFragment extends BaseFragment implements
		OnPageChangeListener {
	private final String TAG = "PhotoAlbumFragment";
	private ListView lv = null;
	private CommentAdapter commentAdapter = null;
	private PhotoPagerAdapter photoPagerAdapter = null;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View layout = inflater.inflate(R.layout.photo_detail, container, false);
		lv = (ListView) layout.findViewById(R.id.comment_list);
		initCommentAdapter();
		lv.setAdapter(commentAdapter);
		initPhotoAdapter();
		View view = inflater.inflate(R.layout.photo_detail_gallery, null);
		ViewPager viewPager = (ViewPager) view.findViewById(R.id.topViewPager);
		viewPager.setAdapter(photoPagerAdapter);
		viewPager.setOnPageChangeListener(PhotoDetailFragment.this);
		lv.addHeaderView(view);
		return layout;
	}

	@Override
	public void onResume() {
		super.onResume();
		MLog.d(TAG, "onResume");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	private class CommentAdapter extends BaseAdapter {
		private List<String> dataList;

		public CommentAdapter(List<String> dataList) {
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

		public void updateData(List<String> dataList) {
			this.dataList = dataList;
			this.notifyDataSetChanged();
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = new TextView(getBaseActivity());
				holder.textView = (TextView) convertView;
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.textView.setText(dataList.get(position));
			return convertView;
		}
	}

	class ViewHolder {
		public TextView textView;
	}

	private class PhotoPagerAdapter extends PagerAdapter {
		private List<View> viewList;

		public PhotoPagerAdapter(List<View> viewList) {
			this.viewList = viewList;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;
		}

		@Override
		public int getCount() {

			return viewList.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(viewList.get(position));

		}

		@Override
		public int getItemPosition(Object object) {

			return super.getItemPosition(object);
		}

		@Override
		public CharSequence getPageTitle(int position) {

			return "";
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(viewList.get(position));
			return viewList.get(position);
		}

	};

	private void initCommentAdapter() {
		List<String> l = new ArrayList<String>();
		for (int i = 0; i < 50; i++) {
			l.add("" + i);
		}
		commentAdapter = new CommentAdapter(l);
	}

	private void initPhotoAdapter() {
		List<View> l = new ArrayList<View>();
		NetworkImageView v1 = new NetworkImageView(getBaseActivity());
		v1.setAdjustViewBounds(true);
		v1.setScaleType(ScaleType.CENTER_CROP);
		v1.setImageUrl(
				"http://pic12.nipic.com/20110108/1592733_103230329000_2.jpg",
				VolleySingleton.getInstance().getImageLoader());
		l.add(v1);
		NetworkImageView v2 = new NetworkImageView(getBaseActivity());
		v2.setImageUrl(
				"http://bbs.szonline.net/UploadFile/album/2012/1/508245/10/20120110093131_13505.jpg",
				VolleySingleton.getInstance().getImageLoader());
		l.add(v2);
		NetworkImageView v3 = new NetworkImageView(getBaseActivity());
		v3.setImageUrl("http://photocdn.sohu.com/20090331/Img263119749.jpg",
				VolleySingleton.getInstance().getImageLoader());
		l.add(v3);
		NetworkImageView v4 = new NetworkImageView(getBaseActivity());
		v4.setImageUrl(
				"http://photo.dizo.com.cn/article/2011/4/23/2e18546f-f64c-4d92-83b2-90a8fb7000b1.jpg",
				VolleySingleton.getInstance().getImageLoader());
		l.add(v4);
		NetworkImageView v5 = new NetworkImageView(getBaseActivity());
		v5.setImageUrl(
				"http://pic-shopping.bcia.com.cn/s/thump/110223/dc90cb1d8aba4f45774c8d6fdba94fdc_2_322_243.jpg",
				VolleySingleton.getInstance().getImageLoader());
		l.add(v5);
		NetworkImageView v6 = new NetworkImageView(getBaseActivity());
		v6.setImageUrl(
				"http://imgt1.bdstatic.com/it/u=4081395060,2324339507&fm=90&gp=0.jpg",
				VolleySingleton.getInstance().getImageLoader());
		l.add(v6);
		NetworkImageView v7 = new NetworkImageView(getBaseActivity());
		v7.setImageUrl(
				"http://imgt4.bdstatic.com/it/u=2961223305,208614355&fm=21&gp=0.jpg",
				VolleySingleton.getInstance().getImageLoader());
		l.add(v7);
		NetworkImageView v8 = new NetworkImageView(getBaseActivity());
		v8.setImageUrl(
				"http://www.meihua.info/today/post/image.axd?picture=kuxia3.jpg",
				VolleySingleton.getInstance().getImageLoader());
		l.add(v8);
		photoPagerAdapter = new PhotoPagerAdapter(l);
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onPageScrolled(int position, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {
		List<String> l = new ArrayList<String>();
		for (int i = position; i < 50 + position; i++) {
			l.add("" + i);
		}
		commentAdapter.updateData(l);
	}
}
