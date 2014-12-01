package com.eric.huatianzhi.beans;

import org.json.JSONObject;

public class AlbumBean {
	private int itemId;
	private int nLike;
	private String url;
	private long lastUpdate;
	private String thumbURL;

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getnLike() {
		return nLike;
	}

	public void setnLike(int nLike) {
		this.nLike = nLike;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getThumbURL() {
		return thumbURL;
	}

	public void setThumbURL(String thumbURL) {
		this.thumbURL = thumbURL;
	}

	public static AlbumBean fromJson(JSONObject j) {
		if (j != null) {
			AlbumBean ab = new AlbumBean();
			ab.setItemId(j.optInt("ItemID"));
			ab.setnLike(j.optInt("NLike"));
			ab.setUrl(j.optString("URL"));
			ab.setLastUpdate(j.optLong("LastUpdate"));
			ab.setThumbURL(j.optString("ThumbURL"));
			return ab;
		} else {
			return null;
		}
	}
}
