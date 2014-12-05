package com.eric.huatianzhi.beans;

import java.io.Serializable;

import org.json.JSONObject;

public class JoinedWeddingBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int weddingID;
	private int preWeddingPhotoAlbumID;
	private String coverPhotoURL;

	public JoinedWeddingBean() {
	}

	public JoinedWeddingBean(int weddingID, int preWeddingPhotoAlbumID,
			String coverPhotoURL) {
		this.weddingID = weddingID;
		this.preWeddingPhotoAlbumID = preWeddingPhotoAlbumID;
		this.coverPhotoURL = coverPhotoURL;
	}

	public int getWeddingID() {
		return weddingID;
	}

	public void setWeddingID(int weddingID) {
		this.weddingID = weddingID;
	}

	public int getPreWeddingPhotoAlbumID() {
		return preWeddingPhotoAlbumID;
	}

	public void setPreWeddingPhotoAlbumID(int preWeddingPhotoAlbumID) {
		this.preWeddingPhotoAlbumID = preWeddingPhotoAlbumID;
	}

	public String getCoverPhotoURL() {
		return coverPhotoURL;
	}

	public void setCoverPhotoURL(String coverPhotoURL) {
		this.coverPhotoURL = coverPhotoURL;
	}

	public static JoinedWeddingBean fromJson(JSONObject j) {
		if (j == null) {
			return null;
		} else {
			return new JoinedWeddingBean(j.optInt("WeddingID"),
					j.optInt("PreWeddingPhotoAlbumID"),
					j.optString("CoverPhotoURL"));
		}
	}
}
