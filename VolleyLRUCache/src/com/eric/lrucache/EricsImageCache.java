package com.eric.lrucache;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class EricsImageCache implements ImageCache {
	private final String TAG = "EricsImageCache";
	private static EricsImageCache imageCache;
	private LruCache<String, Bitmap> mMemoryLruCache = new LruCache<String, Bitmap>(
			20 * 1024 * 1024);
	private DiskLruCache mDiskLruCache = null;
	private final boolean forcedEveryCacheToDisk = true;

	private EricsImageCache(Context context) {
		try {
			File cacheDir = Util.getDiskCacheDir(context, "bitmap");
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			mDiskLruCache = DiskLruCache.open(cacheDir,
					Util.getAppVersion(context), 1, 50 * 1024 * 1024);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized static EricsImageCache getInstance(Context context) {
		if (imageCache == null) {
			imageCache = new EricsImageCache(context);
		}
		return imageCache;
	}

	@Override
	public Bitmap getBitmap(String url) {
		String key = Util.md5(url);
		Bitmap b = mMemoryLruCache.get(key);
		if (b == null) {
			Log.d(TAG, "no bitmap found in memory cache.");
			b = Util.getBitmapFromDiskCache(key, mDiskLruCache);
			if (b != null) {
				Log.d(TAG, "bitmap found in disk cache.");
				mMemoryLruCache.put(key, b);
			}else{
				Log.d(TAG, "no bitmap found in disk cache.");
			}
		}else{
			Log.d(TAG, "bitmap found in memory cache.");
		}
		return b;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		String key = Util.md5(url);
		mMemoryLruCache.put(key, bitmap);
		if (forcedEveryCacheToDisk) {
			cacheToDisk(key, bitmap);
		}
	}

	public void onDestory() {
		if (!forcedEveryCacheToDisk) {
			Map<String, Bitmap> bMap = mMemoryLruCache.snapshot();
			for (Map.Entry<String, Bitmap> entry : bMap.entrySet()) {
				String key = entry.getKey();
				Bitmap b = entry.getValue();
				cacheToDisk(key, b);
			}
		}
		mMemoryLruCache.evictAll();
	}

	private void cacheToDisk(String key, Bitmap bitmap) {
		DiskLruCache.Editor editor = null;
		try {
			editor = mDiskLruCache.edit(key);
			if (Util.writeBitmapToFile(bitmap, editor)) {
				editor.commit();
				mDiskLruCache.flush();
				Log.d(TAG, "image put on disk cache " + key);
			} else {
				editor.abort();
				Log.d(TAG, "ERROR on: image put on disk cache " + key);
			}
		} catch (IOException e) {
			if (editor != null) {
				try {
					editor.abort();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}
	}
}
