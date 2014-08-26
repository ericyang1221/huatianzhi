package com.eric.huatianzhi.utils;

import android.util.Log;

public class MLog {
	private static boolean debug = true;
	private static boolean error = true;
	private static boolean info = true;

	public static void d(String tag, String msg) {
		if (debug) {
			Log.d(tag, msg);
		}
	}
	
	public static void e(String tag, String msg) {
		if (error) {
			Log.e(tag, msg);
		}
	}
	
	public static void i(String tag, String msg) {
		if (info) {
			Log.i(tag, msg);
		}
	}
}
