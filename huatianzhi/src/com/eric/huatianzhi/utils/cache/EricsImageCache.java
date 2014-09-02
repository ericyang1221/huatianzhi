package com.eric.huatianzhi.utils.cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.eric.huatianzhi.utils.MLog;

public class EricsImageCache implements ImageCache {
	private final String TAG = "EricsImageCache";
	private static EricsImageCache imageCache;
	private LruCache<String, Bitmap> mMemoryLruCache = new LruCache<String, Bitmap>(
			10 * 1024 * 1024);
	private DiskLruCache mDiskLruCache = null;
	private final int IO_BUFFER_SIZE = 8 * 1024;
	private final int COMPRESS_QUALITY = 80;
	private final boolean forcedEveryCacheToDisk = false;

	private EricsImageCache(Context context) {
		try {
			File cacheDir = getDiskCacheDir(context, "bitmap");
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context),
					1, 50 * 1024 * 1024);
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
		String key = md5(url);
		Bitmap b = mMemoryLruCache.get(key);
		if (b == null) {
			b = getBitmapFromDiskCache(key);
			if (b != null) {
				mMemoryLruCache.put(key, b);
			}
		}
		return b;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		String key = md5(url);
		mMemoryLruCache.put(key, bitmap);
		if(forcedEveryCacheToDisk){
			cacheToDisk(key,bitmap);
		}
	}

	public void onDestory() {
		if(!forcedEveryCacheToDisk){
			Map<String, Bitmap> bMap = mMemoryLruCache.snapshot();
			for (Map.Entry<String, Bitmap> entry : bMap.entrySet()) {
				String key = entry.getKey();
				Bitmap b = entry.getValue();
				cacheToDisk(key,b);
			}
		}
		mMemoryLruCache.evictAll();
	}
	
	private void cacheToDisk(String key,Bitmap bitmap){
		DiskLruCache.Editor editor = null;
		try {
			editor = mDiskLruCache.edit(key);
			if (writeBitmapToFile(bitmap, editor)) {
				editor.commit();
				mDiskLruCache.flush();
				MLog.d(TAG, "image put on disk cache " + key);
			} else {
				editor.abort();
				MLog.d(TAG, "ERROR on: image put on disk cache " + key);
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

	private Bitmap getBitmapFromDiskCache(String key) {
		Bitmap bitmap = null;
		DiskLruCache.Snapshot snapshot = null;
		try {
			snapshot = mDiskLruCache.get(key);
			if (snapshot == null) {
				return null;
			}
			final InputStream in = snapshot.getInputStream(0);
			if (in != null) {
				final BufferedInputStream buffIn = new BufferedInputStream(in,
						IO_BUFFER_SIZE);
				bitmap = BitmapFactory.decodeStream(buffIn);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (snapshot != null) {
				snapshot.close();
			}
		}
		return bitmap;

	}

	private boolean writeBitmapToFile(Bitmap bitmap, DiskLruCache.Editor editor)
			throws IOException, FileNotFoundException {
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(editor.newOutputStream(0),
					IO_BUFFER_SIZE);
			return bitmap.compress(CompressFormat.JPEG, COMPRESS_QUALITY, out);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	private File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}

	private int getAppVersion(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}

	private String md5(String s) {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(s.getBytes("UTF-8"));
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1, digest);
			return bigInt.toString(16);
		} catch (NoSuchAlgorithmException e) {
			throw new AssertionError();
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError();
		}
	}
}
