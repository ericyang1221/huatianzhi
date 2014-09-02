package com.eric.huatianzhi;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.eric.huatianzhi.utils.cache.EricsImageCache;
import com.huewu.pla.lib.MultiColumnPullToRefreshListView;
import com.huewu.pla.lib.MultiColumnPullToRefreshListView.OnRefreshListener;
import com.huewu.pla.lib.internal.PLA_AdapterView;

public class PullToRefreshSampleActivity extends Activity {
	private RequestQueue mQueue;

	private class MySimpleAdapter extends BaseAdapter {
		private LayoutInflater mInflater = getLayoutInflater();
		private ImageLoader imageLoader = new ImageLoader(mQueue,
				EricsImageCache.getInstance(PullToRefreshSampleActivity.this));
		private List<String> dataList;

		public MySimpleAdapter(List<String> dataList) {
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.sample_item, null);
				holder.imageView = (NetworkImageView) convertView
						.findViewById(R.id.thumbnail);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.imageView.setDefaultImageResId(R.drawable.ic_launcher);
			holder.imageView.setErrorImageResId(R.drawable.ic_launcher);
			holder.imageView
					.setImageUrl(
							dataList.get(position),
							imageLoader);
			return convertView;
		}
	}

	class ViewHolder {
		public NetworkImageView imageView;
	}

	private PLA_AdapterView<ListAdapter> mAdapterView = null;
	private MySimpleAdapter mAdapter = null;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.eric.huatianzhi.R.layout.sample_pull_to_refresh_act);
		mQueue = Volley.newRequestQueue(this);
		// mAdapterView = (PLA_AdapterView<Adapter>) findViewById(R.id.list);
		mAdapterView = (PLA_AdapterView<ListAdapter>) findViewById(R.id.list);
		final MultiColumnPullToRefreshListView lv = (MultiColumnPullToRefreshListView) mAdapterView;
		lv.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				System.out.println("xxxxxxxxxxxxxx");
				// 5秒后完成
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						lv.onRefreshComplete();
					}
				}, 1000);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		initAdapter();
		mAdapterView.setAdapter(mAdapter);
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
		mAdapter = new MySimpleAdapter(l);
	}

}// end of class
