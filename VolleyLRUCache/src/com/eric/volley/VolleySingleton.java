package com.eric.volley;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.eric.EricsApplication;
import com.eric.lrucache.EricsImageCache;

public class VolleySingleton {
	private static VolleySingleton mInstance = null;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	private VolleySingleton() {
		mRequestQueue = Volley.newRequestQueue(EricsApplication.getAppContext());
		mImageLoader = new ImageLoader(this.mRequestQueue,
				EricsImageCache.getInstance(EricsApplication.getAppContext()));
	}

	public static VolleySingleton getInstance() {
		if (mInstance == null) {
			mInstance = new VolleySingleton();
		}
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		return this.mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		return this.mImageLoader;
	}
}
